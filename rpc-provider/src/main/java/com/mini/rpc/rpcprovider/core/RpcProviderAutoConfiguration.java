package com.mini.rpc.rpcprovider.core;

import com.mini.rpc.rpcprovider.config.RpcProperty;
import com.minimrpc.rpcregistry.support.factory.RegistryFactory;
import com.minimrpc.rpcregistry.constants.RegistryType;
import com.minimrpc.rpcregistry.service.RegistryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

/***
 * @description {@link RpcProvider} auto configuration 
 * @author <h>cuitao@aixuexi.com</h>
 * @date 2021-05-19 14:43
 * @since V1.0.0
 */
@Configuration
@EnableConfigurationProperties(RpcProperty.class)
public class RpcProviderAutoConfiguration {

    @Autowired
    private RpcProperty rpcProperty;

    @Bean
//    @Order(Integer.MAX_VALUE)
    public RpcProvider provider() throws Exception {
        RegistryType registryType = RegistryType.valueOf(rpcProperty.getServiceRegistryType());
        RegistryService serviceRegistry = RegistryFactory.getInstance(rpcProperty.getServiceRegistryAddr(), registryType);
//        return RpcProvider.builder().registryService(serviceRegistry).port(rpcProperty.getServicePort()).build();
        return new RpcProvider(rpcProperty.getServicePort(), serviceRegistry);
    }
}
