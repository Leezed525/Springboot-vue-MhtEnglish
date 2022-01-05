package com.lee.mht.system.config;

import com.lee.mht.system.utils.ApplicationContextHolder;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.RandomUtils;
import org.apache.ibatis.cache.Cache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;

import java.security.MessageDigest;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;


/**
 * MyBatis二级缓存Redis实现
 * 重点处理以下几个问题
 * 1、缓存穿透：存储空值解决，MyBatis框架实现
 * 2、缓存击穿：使用互斥锁，我们自己实现
 * 3、缓存雪崩：缓存有效期设置为一个随机范围，我们自己实现
 * 4、读写性能：redis key不能过长
 * @author FucXing
 * @date 2022/01/05 19:36
 **/
public class MybatisRedisCache implements Cache
{

    private static final Logger logger = LoggerFactory.getLogger(MybatisRedisCache.class);

    /**
     * 统一缓存头
     */
    private static final String CACHE_NAME = "MyBatis:";
    /**
     * 读写锁：解决缓存击穿
     */
    private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock();
    /**
     * 表空间ID：方便后面的缓存清理
     */
    private final String id;
    /**
     * redis服务接口：提供基本的读写和清理
     */
    //private static volatile RedisService redisService;

    //private RedisTemplate redisService; //(RedisTemplate) ApplicationContextHolder.getBean("redisTemplate");

    //这里使用了redis缓存，使用springboot自动注入

    private RedisTemplate<String, Object> redisTemplate;


    /**
     * 信息摘要
     */
    private volatile MessageDigest messageDigest;

    /////////////////////// 解决缓存雪崩，具体范围根据业务需要设置合理值 //////////////////////////
    /**
     * 缓存最小有效期
     */
    private int minExpireMinutes = 60;
    /**
     * 缓存最大有效期
     */
    private int maxExpireMinutes = 120;

    /**
     * MyBatis给每个表空间初始化的时候要用到
     *
     * @param id 其实就是namespace的值
     */
    public MybatisRedisCache(String id) {
        if (id == null) {
            throw new IllegalArgumentException("Cache instances require an ID");
        }
        this.id = id;
    }

    /**
     * 获取ID
     *
     * @return 真实值
     */
    @Override
    public String getId() {
        return id;
    }

    /**
     * 创建缓存
     *
     * @param key   其实就是sql语句
     * @param value sql语句查询结果
     */
    @Override
    public void putObject(Object key, Object value) {
        try {
            String strKey = generateRedisKey(key);
            // 在redis额外维护CacheNamespace创建的key，clear的时候只清理当前CacheNamespace的数据
            getRedisTemplate().opsForHash().put(CACHE_NAME + id, strKey, "1");
            // 有效期 随机，防止雪崩
            int expireMinutes = RandomUtils.nextInt(minExpireMinutes, maxExpireMinutes);
            logger.info("将查询结果存储到cache.key:" + strKey + ",value:" + value);
            getRedisTemplate().opsForValue().set(strKey, value, expireMinutes, TimeUnit.SECONDS);

            logger.debug("Put cache to redis, id={}", strKey);
        } catch (Exception e) {
            logger.error("Redis put failed, key=" + key.toString(), e);
        }
    }

    /**
     * 读取缓存
     *
     * @param key 其实就是sql语句
     * @return 缓存结果
     */
    @Override
    public Object getObject(Object key) {
        try {
            String strKey = generateRedisKey(key);
            logger.debug("Get cache from redis, id={} key={}", id, strKey);
            return getRedisTemplate().opsForValue().get(strKey);
        } catch (Exception e) {
            logger.error("Redis get failed, fail over to db", e);
            return null;
        }
    }

    /**
     * 删除缓存
     *
     * @param key 其实就是sql语句
     * @return 结果
     */
    @Override
    public Object removeObject(Object key) {
        try {
            String strKey = generateRedisKey(key);
            getRedisTemplate().delete(strKey);
            logger.debug("Remove cache from redis, id={}", id);
        } catch (Exception e) {
            logger.error("Redis remove failed", e);
        }
        return null;
    }

    /**
     * 缓存清理
     * 应该是根据表空间进行清理
     */
    @Override
    public void clear() {
        try {
            logger.debug("clear cache, id={}", id);
            String hsKey = CACHE_NAME + id;
            // 获取CacheNamespace所有缓存key
            Map<Object, Object> idMap = getRedisTemplate().opsForHash().entries(hsKey);
            if (!idMap.isEmpty()) {
                Set<Object> keySet = idMap.keySet();
                Set<String> keys = new HashSet<>(keySet.size());
                keySet.forEach(item -> keys.add(item.toString()));
                // 清空CacheNamespace下面所有缓存Key
                getRedisTemplate().delete(keys);
                // 清空CacheNamespace
                getRedisTemplate().delete(hsKey);
            }
        } catch (Exception e) {
            logger.error("clear cache failed", e);
        }
    }

    /**
     * 获取缓存大小，暂时没用上
     *
     * @return 长度
     */
    @Override
    public int getSize() {
        return 0;
    }

    /**
     * 获取读写锁：为了解决缓存击穿
     *  mybites 3.2 不生效
     * @return 锁
     */
    @Override
    public ReadWriteLock getReadWriteLock() {
        return readWriteLock;
    }

    /**
     * 获取Redis服务接口
     * 使用双重检查保证线程安全
     *
     * @return 服务实例
     */
    private RedisTemplate<String,Object> getRedisTemplate() {
        if (redisTemplate == null) {
            synchronized (RedisTemplate.class) {
                if (redisTemplate == null) {
                    redisTemplate = (RedisTemplate<String,Object>) ApplicationContextHolder.getBean("redisTemplate");
                }
            }
        }
        return redisTemplate;
    }

    private String generateRedisKey(Object key) {
        String o = this.id + DigestUtils.sha256Hex(key.toString().getBytes());
        //logger.info(o);
        return o;
    }

    public static byte[] append(byte[]... bas) {
        int iLen = 0;
        byte[][] var2 = bas;
        int var3 = bas.length;

        int var4;
        for (var4 = 0; var4 < var3; ++var4) {
            byte[] ba = var2[var4];
            if (ba != null && ba.length > 0) {
                iLen += ba.length;
            }
        }

        byte[] result = new byte[iLen];
        iLen = 0;
        byte[][] var8 = bas;
        var4 = bas.length;

        for (int var9 = 0; var9 < var4; ++var9) {
            byte[] ba = var8[var9];
            if (ba != null && ba.length > 0) {
                System.arraycopy(ba, 0, result, iLen, ba.length);
                iLen += ba.length;
            }
        }

        return result;
    }

    public int getMinExpireMinutes() {
        return minExpireMinutes;
    }

    public MybatisRedisCache setMinExpireMinutes(int minExpireMinutes) {
        this.minExpireMinutes = minExpireMinutes;
        return this;
    }

    public int getMaxExpireMinutes() {
        return maxExpireMinutes;
    }

    public MybatisRedisCache setMaxExpireMinutes(int maxExpireMinutes) {
        this.maxExpireMinutes = maxExpireMinutes;
        return this;
    }
}