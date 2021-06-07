package com.minimrpc.rpcregistry.support.factory;

import com.minimrpc.rpcregistry.constants.RegistryType;
import com.minimrpc.rpcregistry.service.EurekaRegistryService;
import com.minimrpc.rpcregistry.service.RegistryService;
import com.minimrpc.rpcregistry.service.ZookeeperRegistryService;

/***
 * @description
 * @author <h>cuitao@aixuexi.com</h>
 * @date 2021-05-19 15:14
 * @since V1.0.0
 */
public class RegistryFactory {

    private static volatile RegistryService registryService;
    /***
     *
     * @param serviceRegistryAddr 服务注册地址
     * @param registryType 注册类型
     * @return RegistryService
     */
    public static RegistryService getInstance(String serviceRegistryAddr, RegistryType registryType) throws Exception {
        if (null == registryService) {
            synchronized (RegistryFactory.class) {
                if (null == registryService) {
                    switch (registryType) {
                        case ZOOKEEPER:
                            registryService = new ZookeeperRegistryService(serviceRegistryAddr);
                            break;
                        case EUREKA:
                            registryService = new EurekaRegistryService(serviceRegistryAddr);
                            break;
                        default:
                            registryService = null;
                            System.err.println("registryService is null!");
                    }
                }
            }
        }
        return registryService;
    }

}
