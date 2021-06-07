package com.mini.rpc.rpcconsumer;

import com.mini.rpc.rpcconsumer.annotation.RpcReference;
import com.mini.rpc.rpcfacade.HelloService;
import org.springframework.stereotype.Service;

/***
 * @description
 * @author <h>cuitao@aixuexi.com</h>
 * @date 2021-05-24 17:27
 * @since V1.0.0
 */
@Service
public class LocalService {

    @RpcReference
    private HelloService helloService;

//    public LocalService(HelloService helloService) {
//        this.helloService = helloService;
//    }

    public String hello(){
        String msg = helloService.hello();
        System.err.println(msg);
        return msg;
    }
}
