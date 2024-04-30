package com.czy.aspect;

import com.alibaba.fastjson.JSON;
import com.czy.annotation.MySystemlog;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * ClassName: myLogAspect
 * Package: com.czy.aspect
 * Description:
 *
 * @Author Chen Ziyun
 * @Version 1.0
 */
@Component
@Slf4j
@Aspect// 告诉Spring容器，myLogAspect是切面类
public class MyLogAspect {
    //确定哪个切点，以后哪个类想成为切点，就在哪个类添加上面那行的注解。例如下面这个xxpt()就是切点
    @Pointcut("@annotation(com.czy.annotation.MySystemlog)")
    public void pt(){

    }

    //定义通知的方法(这里用的是环绕通知)，通知的方法也就是增强的具体代码。@Around注解表示该通知的方法会用在哪个切点
    @Around("pt()")
    public Object printLog(ProceedingJoinPoint joinPoint) throws Throwable {
        Object ret;
        //proceed方法表示调用目标方法，ret就是目标方法执行完之后的返回值
        try {
            beforeJoin(joinPoint);
            ret = joinPoint.proceed();
            afterJoin(ret);
        }finally {
            // 结束后换行
            log.info("=======================end=======================" + System.lineSeparator());
        }
        return ret;
    }

    private void afterJoin(Object ret) {
        // 打印出参
        log.info("返回参数   : {}", JSON.toJSONString(ret));
    }

    private void beforeJoin(ProceedingJoinPoint joinPoint) {
        //ServletRequestAttributes是RequestAttributes是spring接口的实现类
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();

        //获取被增强方法的注解对象，例如获取UserController类的updateUserInfo方法上一行的@mySystemlog注解
        //getSystemlog是我们下面写的方法
        MySystemlog mySystemlog = getSystemLog(joinPoint);

        log.info("======================Start======================");
        // 打印请求 URL
        log.info("请求URL   : {}", request.getRequestURL());
        // 打印描述信息
        log.info("接口描述   : {}", mySystemlog.businessName());
        // 打印 Http method
        log.info("请求方式   : {}", request.getMethod());
        // 打印调用 controller 的全路径以及执行方法
        log.info("请求类名   : {}.{}", joinPoint.getSignature().getDeclaringTypeName(),((MethodSignature) joinPoint.getSignature()).getName());
        // 打印请求的 IP
        log.info("访问IP    : {}",request.getRemoteHost());
        // 打印请求入参
        log.info("传入参数   : {}", JSON.toJSONString(joinPoint.getArgs()));
    }

    private MySystemlog getSystemLog(ProceedingJoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        MySystemlog annotation = signature.getMethod().getAnnotation(MySystemlog.class);
        return annotation;
    }


}
