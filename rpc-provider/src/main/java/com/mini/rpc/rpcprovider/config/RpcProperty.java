package com.mini.rpc.rpcprovider.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.core.annotation.OrderUtils;

/***
 * @description Rpc的属性参数设置
 * @author <h>cuitao@aixuexi.com</h>
 * @date 2021-05-19 14:25
 * @since V1.0.0
 */
@ConfigurationProperties(prefix = "rpc")
@Configuration
//@Order(Integer.MIN_VALUE + 1)
public class RpcProperty {
    /***
     * 暴露端口
     */
    private int servicePort;
    /***
     * 注册地址
     */
    private String serviceRegistryAddr;
    /***
     * 注册方式
     */
    private String serviceRegistryType;

    public int getServicePort() {
        return servicePort;
    }

    public void setServicePort(int servicePort) {
        this.servicePort = servicePort;
    }

    public String getServiceRegistryAddr() {
        return serviceRegistryAddr;
    }

    public void setServiceRegistryAddr(String serviceRegistryAddr) {
        this.serviceRegistryAddr = serviceRegistryAddr;
    }

    public String getServiceRegistryType() {
        return serviceRegistryType;
    }

    public void setServiceRegistryType(String serviceRegistryType) {
        this.serviceRegistryType = serviceRegistryType;
    }
}
