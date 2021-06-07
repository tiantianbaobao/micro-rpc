package com.mini.rpc.rpcprovider.annotation;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/***
 * @description 对外暴露服务时的服务注解
 * @author <h>cuitao@aixuexi.com</h>
 * @date 2021-05-19 14:37
 * @since V1.0.0
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface RpcService {
    /***
     * 服务接口类
     * @return
     */
    Class<?> serviceInterface();

    /***
     * 版本号
     * @return
     */
    String version() default "1.0";
}
