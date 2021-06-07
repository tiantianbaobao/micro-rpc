package com.mini.rpc.rpccore.common;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/***
 * @description
 * @author <h>cuitao@aixuexi.com</h>
 * @date 2021-05-24 0:29
 * @since V1.0.0
 */
public class RpcRequestHolder {
    public static final AtomicLong REQUEST_ID_GENERATOR = new AtomicLong(0);
    public static final Map<Long, MiniRpcFuture<RpcResponse>> REQUEST_MAP = new ConcurrentHashMap<>();

}
