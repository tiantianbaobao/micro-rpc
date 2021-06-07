package com.minimrpc.rpcregistry.service;

import com.mini.rpc.rpccore.common.RpcRegistryHelper;
import com.minimrpc.rpcregistry.loadblance.ZookeeperConsistentHashLoadBalancer;
import com.minimrpc.rpcregistry.model.ServiceMetadata;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.x.discovery.ServiceDiscovery;
import org.apache.curator.x.discovery.ServiceDiscoveryBuilder;
import org.apache.curator.x.discovery.ServiceInstance;
import org.apache.curator.x.discovery.details.JsonInstanceSerializer;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

/***
 * @description {@link RegistryService}default implement
 * @author <h>cuitao@aixuexi.com</h>
 * @date 2021-05-23 22:17
 * @since V1.0.0
 */
public class ZookeeperRegistryService implements RegistryService{

    private static final int BASE_SLEEP_TIME_MS = 1000;
    private static final int MAX_RETRY_TIMES = 3;
    private static final String BASE_ROOT_PATH = "mini-rpc";
    private final ServiceDiscovery<ServiceMetadata> serviceDiscovery;

    public ZookeeperRegistryService(String registryAddr) throws Exception{
        CuratorFramework client = CuratorFrameworkFactory.newClient(registryAddr, new ExponentialBackoffRetry(BASE_SLEEP_TIME_MS, MAX_RETRY_TIMES));
        client.start();
        JsonInstanceSerializer<ServiceMetadata> serializer = new JsonInstanceSerializer<>(ServiceMetadata.class);
        this.serviceDiscovery = ServiceDiscoveryBuilder
                .builder(ServiceMetadata.class)
                .client(client)
                .serializer(serializer)
                .basePath(BASE_ROOT_PATH)
                .build();
        this.serviceDiscovery.start();

    }

    @Override
    public void register(ServiceMetadata serviceMetadata) throws Exception {
        ServiceInstance<ServiceMetadata> instance = ServiceInstance
                .<ServiceMetadata>builder()
                .name(RpcRegistryHelper.buildServiceKey(serviceMetadata.getServiceName(), serviceMetadata.getServiceVersion()))
                .address(serviceMetadata.getServiceAddr())
                .port(serviceMetadata.getServicePort())
                .payload(serviceMetadata)
                .build();
        serviceDiscovery.registerService(instance);
    }

    @Override
    public void unregister(ServiceMetadata serviceMetadata) throws Exception {
        ServiceInstance<ServiceMetadata> instance = ServiceInstance
                .<ServiceMetadata>builder()
                .name(RpcRegistryHelper.buildServiceKey(serviceMetadata.getServiceName(), serviceMetadata.getServiceVersion()))
                .address(serviceMetadata.getServiceAddr())
                .port(serviceMetadata.getServicePort())
                .payload(serviceMetadata)
                .build();
        serviceDiscovery.unregisterService(instance);
    }

    @Override
    public ServiceMetadata discovery(String serviceName, int invokeHashCode) throws Exception {
        Collection<ServiceInstance<ServiceMetadata>> serviceInstances = serviceDiscovery.queryForInstances(serviceName);
        ServiceInstance<ServiceMetadata> serviceInstance = new ZookeeperConsistentHashLoadBalancer().select((List<ServiceInstance<ServiceMetadata>>) serviceInstances, invokeHashCode);
        if (serviceInstance != null) {
            return serviceInstance.getPayload();
        }
        System.err.println("服务发现为空！");
        return null;
    }

    @Override
    public void destroy(ServiceMetadata serviceMetadata) throws IOException {
        serviceDiscovery.close();
    }
}
