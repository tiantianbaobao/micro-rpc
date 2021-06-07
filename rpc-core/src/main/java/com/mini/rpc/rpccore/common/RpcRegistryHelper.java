package com.mini.rpc.rpccore.common;

/***
 * @description
 * @author <h>cuitao@aixuexi.com</h>
 * @date 2021-05-23 22:38
 * @since V1.0.0
 */
public class RpcRegistryHelper {
    public static String buildServiceKey(String serviceName, String serviceVersion) {
        return String.join("#", serviceName, serviceVersion);
    }
}
