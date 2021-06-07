package com.mini.rpc.rpccore.common;

import io.netty.util.concurrent.Promise;
import lombok.Data;

/***
 * @description
 * @author <h>cuitao@aixuexi.com</h>
 * @date 2021-05-24 0:29
 * @since V1.0.0
 */
@Data
public class MiniRpcFuture<T> {
    private Promise<T> promise;
    private long timeout;

    public MiniRpcFuture(Promise<T> promise, long timeout) {
        this.promise = promise;
        this.timeout = timeout;
    }
}
