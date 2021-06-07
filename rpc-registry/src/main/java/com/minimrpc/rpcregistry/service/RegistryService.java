package com.minimrpc.rpcregistry.service;

import com.minimrpc.rpcregistry.model.ServiceMetadata;

import java.io.IOException;

/***
 * @description {@link ServiceMetadata} registry service
 * @author <h>cuitao@aixuexi.com</h>
 * @date 2021-05-23 22:12
 * @since V1.0.0
 */
public interface RegistryService {
    /***
     * 注册
     * @param serviceMetadata
     * @throws Exception
     */
    void register(ServiceMetadata serviceMetadata) throws Exception;

    /***
     * un-注册
     * @param serviceMetadata
     * @throws Exception
     */
    void unregister(ServiceMetadata serviceMetadata) throws Exception;

    /***
     * 服务发现
     * @param serviceName
     * @param invokeHashCode
     * @return
     * @throws Exception
     */
    ServiceMetadata discovery(String serviceName, int invokeHashCode) throws Exception;

    /***
     * 服务销毁
     * @param serviceMetadata
     * @throws IOException
     */
    void destroy(ServiceMetadata serviceMetadata) throws IOException;
}
