package com.mini.rpc.rpcprovider.impl;

import com.mini.rpc.rpcfacade.HelloService;
import com.mini.rpc.rpcprovider.annotation.RpcService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/***
 * @description {@link HelloService} impl
 * @author <h>cuitao@aixuexi.com</h>
 * @date 2021-05-20 0:11
 * @since V1.0.0
 */
@RpcService(serviceInterface = HelloService.class, version = "1.0")
@Slf4j
public class HelloFacadeImpl implements HelloService {
    @Override
    public String hello() {
        log.info("执行hello方法！");
        return "hello world!";
    }
}
