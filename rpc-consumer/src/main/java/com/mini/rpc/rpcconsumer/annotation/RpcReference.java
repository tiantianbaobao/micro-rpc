package com.mini.rpc.rpcconsumer.annotation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/***
 * @description rpc reference
 * @author <h>cuitao@aixuexi.com</h>
 * @date 2021-05-19 22:56
 * @since V1.0.0
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
//@Component
@Autowired
public @interface RpcReference {

    String version() default "1.0";

    String registryType() default "ZOOKEEPER";

    String registryAddr() default "127.0.0.1:2181";

    long timeout() default 5000;
}
