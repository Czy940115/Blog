package com.czy.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * ClassName: MySystemlog
 * Package: com.czy.annotation
 * Description:
 *
 * @Author Chen Ziyun
 * @Version 1.0
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface MySystemlog {
    //为controller提供接口的描述信息，用于'日志记录'功能
    String businessName();
}
