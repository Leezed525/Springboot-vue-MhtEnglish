package com.lee.mht.system.aspect;

import com.lee.mht.system.annotation.MhtLog;
import com.lee.mht.system.common.Constant;
import com.lee.mht.system.common.ResultObj;
import com.lee.mht.system.entity.AdminLog;
import com.lee.mht.system.service.AdminLogService;
import com.lee.mht.system.service.RedisService;
import com.lee.mht.system.utils.IpUtils;
import com.lee.mht.system.utils.JacksonUtils;
import com.lee.mht.system.utils.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author FucXing
 * @date 2022/01/10 11:41
 **/
@Aspect
@Component
@Slf4j
public class MhtLogAspect {

    @Autowired
    private RedisService redisService;

    /**
     * 首先定义一个切点
     */
    @Pointcut("@annotation(com.lee.mht.system.annotation.MhtLog)")
    public void LogPoint() {
    }


    @Around("LogPoint() && @annotation(mhtLog)")//将注解也注入到切面中
    public Object saveLogInfo(ProceedingJoinPoint point, MhtLog mhtLog) {
        Object obj = null;

        Long beginTime = System.currentTimeMillis();//记录起始时间
        String resultString;
        String timeConsuming;
        try {
            //
            obj = point.proceed();
            ResultObj result = (ResultObj) obj;
            if (result.getCode() == Constant.OK) {
                resultString = Constant.LOG_RESULT_SUCCESS;
            } else {
                resultString = Constant.LOG_RESULT_FAIL;
            }
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            resultString = Constant.LOG_RESULT_FAIL;
        } finally {
            Long currentTime = System.currentTimeMillis();
            timeConsuming = String.valueOf((currentTime - beginTime));
        }
        saveLogInfo(point,resultString,timeConsuming,mhtLog);
        //redisService.pushMhtLogList(adminLog);
        return obj;
    }

    //多线程执行保存日志
    @Async("asyncServiceExecutor")
    void saveLogInfo(ProceedingJoinPoint point, String result, String timeConsuming,MhtLog mhtLog) {
        AdminLog adminLog = new AdminLog();
        //放入type
        adminLog.setType(mhtLog.type());
        //放入action
        adminLog.setAction(mhtLog.action());

        //获取操作的request
        ServletRequestAttributes sra = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        //在adminLog中填充ip和data,并获取操作者
        if (sra != null) {
            HttpServletRequest request = sra.getRequest();
            adminLog.setIp(getIpFromRequest(request));
            //从token中获取操作者
            String token = request.getHeader(Constant.HEADER_TOKEN_KEY);
            adminLog.setOperator(JwtUtils.getUserName(token));
        } else {
            //正常情况下都是可以获得到request的，但是为了防止某些无法预料的情况给对sra为null做个处理
            //因为数据库中ip是必填的
            adminLog.setIp("error ip");
            adminLog.setOperator("error user");
        }
        //获得data
        adminLog.setData(getArgsNameAndValue(point));
        //获得类名
        String class_name = point.getTarget().getClass().getName();
        //获得方法名
        String method_name = point.getSignature().getName();
        //在adminlog中添加方法名
        adminLog.setMethod(class_name + "." + method_name);
        //放入结果
        adminLog.setResult(result);
        //放入耗时
        adminLog.setTimeConsuming(timeConsuming);
        //存入创建时间
        adminLog.setCreatetime(new Date());

        redisService.pushMhtLogList(adminLog);
    }

    //从request 中获取ip，经过测试,如果是null，说明是本地测试时产生的
    private String getIpFromRequest(HttpServletRequest request) {
        String ip = IpUtils.getClientIpAddress(request);
        if (ip == null) {
            return Constant.DEV_DEBUG_IP;
        } else {
            return ip;
        }
    }

    //从point中获取参数名和参数值
    private String getArgsNameAndValue(ProceedingJoinPoint point) {
        Object[] args = point.getArgs();
        String[] parameterNames = ((MethodSignature) (point.getSignature())).getParameterNames();
        Map<String, Object> argsMap = new HashMap<>();
        for (int i = 0; i < args.length; i++) {
            argsMap.put(parameterNames[i], args[i]);
        }
        return JacksonUtils.toJson(argsMap);
    }
}