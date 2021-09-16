package com.chrisjia.mall.filter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

@Aspect
@Component
public class WebLogAspect {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Pointcut("execution(public * com.chrisjia.mall.controller.*.*(..))")
    public void webLog(){
    }

    @Before("webLog()")
    public void doBefore(JoinPoint joinPoint){
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes)RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();
        logger.info("URL: " + request.getRequestURL());
        logger.info("IP: " + request.getRemoteAddr());
        logger.info("Class Method: " + joinPoint.getSignature().getDeclaringTypeName() + "**" + joinPoint.getSignature().getName());
        logger.info("Parameters: " + Arrays.toString(joinPoint.getArgs()));
        logger.info("Parameters: queryString:  " + request.getQueryString());
    }

    @AfterReturning(pointcut = "webLog()", returning="res")
    public void doAfterReturning(Object res) throws JsonProcessingException {
        logger.info("res: " + new ObjectMapper().writeValueAsString(res));
        logger.info("res: " + res);
    }
}
