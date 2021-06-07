package com.mini.rpc.rpcconsumer.inject;

import com.alibaba.fastjson.JSONObject;
import com.mini.rpc.rpcconsumer.RpcConsumer;
import com.mini.rpc.rpccore.common.*;
import com.mini.rpc.rpcprotocol.contant.MsgTypeEnum;
import com.mini.rpc.rpcprotocol.contant.ProtocolConstants;
import com.mini.rpc.rpcprotocol.model.*;
import com.mini.rpc.rpcprotocol.serializable.SerializationTypeEnum;
import com.minimrpc.rpcregistry.service.RegistryService;
import io.netty.channel.DefaultEventLoop;
import io.netty.util.concurrent.DefaultPromise;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import io.netty.util.concurrent.Promise;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

/***
 * @description
 * @author <h>cuitao@aixuexi.com</h>
 * @date 2021-05-24 0:16
 * @since V1.0.0
 */
@Slf4j
public class RpcInvokerProxy implements InvocationHandler {

    private final String serviceVersion;
    private final long timeout;
    private final RegistryService registryService;
    public RpcInvokerProxy(String serviceVersion, long timeout, RegistryService registryService) {
        this.serviceVersion = serviceVersion;
        this.timeout = timeout;
        this.registryService = registryService;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        RpcProtocol<RpcRequest> requestRpcProtocol = new RpcProtocol<>();
        RpcProtocol.MsgHeader msgHeader = new RpcProtocol.MsgHeader();
        msgHeader.setMagic(ProtocolConstants.PROTOCOL_MAGIC);
        msgHeader.setVersion(ProtocolConstants.VERSION);
        long requestId = RpcRequestHolder.REQUEST_ID_GENERATOR.incrementAndGet();
        msgHeader.setRequestId(requestId);
        msgHeader.setSerialization((byte)SerializationTypeEnum.HESSIAN.getType());
        msgHeader.setMsgType((byte) MsgTypeEnum.REQUEST.getType());
        msgHeader.setStatus((byte) 0x1);


        RpcRequest request = new RpcRequest();
        request.setServiceVersion(this.serviceVersion);
        request.setServiceClass(method.getDeclaringClass().getName());
        request.setMethodName(method.getName());
        request.setParamTypes(method.getParameterTypes());
        request.setParams(args);
        requestRpcProtocol.setMsgHeader(msgHeader);
        requestRpcProtocol.setBody(request);

        RpcConsumer rpcConsumer = new RpcConsumer();
        MiniRpcFuture<RpcResponse> future = new MiniRpcFuture<>(new DefaultPromise<>(new DefaultEventLoop()), timeout);
        RpcRequestHolder.REQUEST_MAP.put(requestId, future);
        //发起RPC远程调用
        rpcConsumer.sendRequest(requestRpcProtocol, this.registryService);
        Promise<RpcResponse> promise = future.getPromise().addListener(new GenericFutureListener<Future<? super RpcResponse>>() {
            @Override
            public void operationComplete(Future<? super RpcResponse> future) throws Exception {
                if (future.isSuccess()) {
                    log.info("获取返回值成功");
                } else {
                    log.error("获取返回值失败！");
                }
            }
        });
        log.info("promise -> " + JSONObject.toJSONString(promise));
        return promise.get(timeout, TimeUnit.MILLISECONDS).getData();
    }
}
