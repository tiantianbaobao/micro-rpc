package com.mini.rpc.rpcconsumer.inject;

import com.mini.rpc.rpcconsumer.annotation.RpcReference;
import com.minimrpc.rpcregistry.support.factory.RegistryFactory;
import com.minimrpc.rpcregistry.constants.RegistryType;
import com.minimrpc.rpcregistry.service.RegistryService;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.lang.reflect.Proxy;

/***
 * @description {@link RpcReference} bean
 * @author <h>cuitao@aixuexi.com</h>
 * @date 2021-05-19 23:26
 * @since V1.0.0
 */
//@Component
public class RpcReferenceBean implements FactoryBean<Object> {

    private Class<?> interfaceClass;

    private String serviceName;

    private String serviceVersion;

    private String registryAddr;

    private String registryType;

    private long timeout;

    private Object object;

    @PostConstruct
    private void init() throws Exception {
        RegistryService registryService = RegistryFactory.getInstance(registryAddr, RegistryType.getTypeByName(registryType));
        this.object = Proxy.newProxyInstance(interfaceClass.getClassLoader(),
                new Class[]{interfaceClass},
                new RpcInvokerProxy(serviceVersion, timeout, registryService));

    }

    @Override
    public Object getObject() throws Exception {
        return object;
    }

    @Override
    public Class<?> getObjectType() {
        return interfaceClass;
    }

    public void setInterfaceClass(Class<?> interfaceClass) {
        this.interfaceClass = interfaceClass;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public void setServiceVersion(String serviceVersion) {
        this.serviceVersion = serviceVersion;
    }

    public void setRegistryAddr(String registryAddr) {
        this.registryAddr = registryAddr;
    }

    public void setRegistryType(String registryType) {
        this.registryType = registryType;
    }

    public void setTimeout(long timeout) {
        this.timeout = timeout;
    }
}
