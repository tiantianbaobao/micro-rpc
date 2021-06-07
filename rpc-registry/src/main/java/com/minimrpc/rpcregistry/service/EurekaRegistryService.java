package com.minimrpc.rpcregistry.service;

import com.minimrpc.rpcregistry.model.ServiceMetadata;

import java.io.IOException;

/***
 * @description {@link RegistryService} Eureka implement
 * @author <h>cuitao@aixuexi.com</h>
 * @date 2021-05-23 23:03
 * @since V1.0.0
 */
public class EurekaRegistryService implements RegistryService{

    public EurekaRegistryService(String serviceRegistryAddr) {

    }

    @Override
    public void register(ServiceMetadata serviceMetadata) throws Exception {

    }

    @Override
    public void unregister(ServiceMetadata serviceMetadata) throws Exception {

    }

    @Override
    public ServiceMetadata discovery(String serviceName, int invokeHashCode) throws Exception {
        return null;
    }

    @Override
    public void destroy(ServiceMetadata serviceMetadata) throws IOException {

    }
}
