package com.minimrpc.rpcregistry.loadblance;

import com.minimrpc.rpcregistry.model.ServiceMetadata;
import org.apache.curator.x.discovery.ServiceInstance;

import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

/***
 * @description {@link ServiceLoadBalancer} zookeeper consistent hash implement
 * @author <h>cuitao@aixuexi.com</h>
 * @date 2021-05-23 23:32
 * @since V1.0.0
 */
public class ZookeeperConsistentHashLoadBalancer implements ServiceLoadBalancer<ServiceInstance<ServiceMetadata>>{

    private static final int VIRTUAL_NODE_SIZE = 10;
    private static final String VIRTUAL_NODE_SPLIT = "#";

    @Override
    public ServiceInstance<ServiceMetadata> select(List<ServiceInstance<ServiceMetadata>> serverList, int hashCode) {
        TreeMap<Integer, ServiceInstance<ServiceMetadata>> ring = makeConsistentHashRing(serverList);
        return allocate(ring, hashCode);
    }

    private ServiceInstance<ServiceMetadata> allocate(TreeMap<Integer, ServiceInstance<ServiceMetadata>> ring, int hashCode) {
        Map.Entry<Integer, ServiceInstance<ServiceMetadata>> entry = ring.ceilingEntry(hashCode);
        if (entry == null) {
            entry = ring.firstEntry()   ;
        }
        return entry.getValue();
    }

    private TreeMap<Integer, ServiceInstance<ServiceMetadata>> makeConsistentHashRing(List<ServiceInstance<ServiceMetadata>> serverList) {
        TreeMap<Integer, ServiceInstance<ServiceMetadata>> ring = new TreeMap<>();
        for (ServiceInstance<ServiceMetadata> serviceInstance : serverList) {
            for (int i = 0; i< VIRTUAL_NODE_SIZE; i++) {
                ring.put((buildServiceLoadBalanceKey(serviceInstance) + VIRTUAL_NODE_SPLIT + i).hashCode(), serviceInstance);
            }
        }
        return ring;
    }

    private String buildServiceLoadBalanceKey(ServiceInstance<ServiceMetadata> serviceInstance) {
        return String.join(":", serviceInstance.getAddress(), String.valueOf(serviceInstance.getPort()));
    }
}
