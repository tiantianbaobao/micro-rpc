package com.mini.rpc.rpccore.common;

import lombok.Data;

import java.io.Serializable;

/***
 * @description rpc response common model
 * @author <h>cuitao@aixuexi.com</h>
 * @date 2021-05-20 0:38
 * @since V1.0.0
 */
@Data
public class RpcResponse implements Serializable {
    private Object data;
    private String msg;
}
