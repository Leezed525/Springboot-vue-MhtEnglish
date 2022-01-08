package com.lee.mht.system.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * @author FucXing
 * @date 2022/01/08 14:32
 **/
@Aspect
@Component
@Slf4j
public class CostTimeAspect {
    /**
     * 首先定义一个切点
     */
    @Pointcut("@annotation(com.lee.mht.system.annotation.CostTime)")
    public void countTime() {
    }

    @Around("countTime()")
    public Object doAround(ProceedingJoinPoint joinPoint) {
        Object obj = null;
        try {
            long beginTime = System.currentTimeMillis();
            obj = joinPoint.proceed();
            //获取方法名称
            String methodName = joinPoint.getSignature().getName();
            //获取类名称
            String className = joinPoint.getSignature().getDeclaringTypeName();
            log.info("类:[{}]，方法:[{}]耗时时间为:[{}]", className, methodName, (System.currentTimeMillis() - beginTime) + "ms");
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        return obj;
    }
}