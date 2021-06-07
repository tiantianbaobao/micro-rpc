package com.mini.rpc.rpcconsumer;

import com.mini.rpc.rpcconsumer.annotation.RpcReference;
import com.mini.rpc.rpcfacade.HelloService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration()
@ComponentScan(basePackages = "com.mini.rpc")
class RpcConsumerApplicationTests {

    @Autowired
    private LocalService localService;

    @Test
    void contextLoads() {
        String msg = localService.hello();
        System.err.println("msg -> " + msg);
    }

}
