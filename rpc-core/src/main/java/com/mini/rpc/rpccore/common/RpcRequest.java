package com.mini.rpc.rpccore.common;

import lombok.Data;

import java.io.Serializable;

/***
 * @description rpc request model
 * @author <h>cuitao@aixuexi.com</h>
 * @date 2021-05-20 0:36
 * @since V1.0.0
 */
@Data
public class RpcRequest implements Serializable {
    private String serviceClass;
    private String serviceVersion;
    private String methodName;
    private Object[] params;
    private Class<?>[] paramTypes;
}
