package com.minimrpc.rpcregistry.model;

/***
 * @description 描述服务的元数据信息
 * @author <h>cuitao@aixuexi.com</h>
 * @date 2021-05-19 15:35
 * @since V1.0.0
 */
public class ServiceMetadata {
    /***
     * 服务地址
     */
    private String serviceAddr;
    /***
     * 服务端口
     */
    private int servicePort;
    /***
     * 服务名称
     */
    private String serviceName;
    /***
     * 服务版本
     */
    private String serviceVersion;

    public String getServiceAddr() {
        return serviceAddr;
    }

    public void setServiceAddr(String serviceAddr) {
        this.serviceAddr = serviceAddr;
    }

    public int getServicePort() {
        return servicePort;
    }

    public void setServicePort(int servicePort) {
        this.servicePort = servicePort;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getServiceVersion() {
        return serviceVersion;
    }

    public void setServiceVersion(String serviceVersion) {
        this.serviceVersion = serviceVersion;
    }
}
