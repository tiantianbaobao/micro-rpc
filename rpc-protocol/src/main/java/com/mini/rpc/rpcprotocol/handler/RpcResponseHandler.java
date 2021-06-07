package com.mini.rpc.rpcprotocol.handler;

import com.mini.rpc.rpccore.common.MiniRpcFuture;
import com.mini.rpc.rpccore.common.RpcRequestHolder;
import com.mini.rpc.rpccore.common.RpcResponse;
import com.mini.rpc.rpcprotocol.model.RpcProtocol;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

/***
 * @description {@link SimpleChannelInboundHandler}impl
 * @author <h>cuitao@aixuexi.com</h>
 * @date 2021-05-22 0:28
 * @since V1.0.0
 */
@Slf4j
public class RpcResponseHandler extends SimpleChannelInboundHandler<RpcProtocol<RpcResponse>> {
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, RpcProtocol<RpcResponse> protocol) throws Exception {
        RpcProtocol.MsgHeader msgHeader = protocol.getMsgHeader();
        if (msgHeader == null) {
           log.error("响应消息头为空！");
           return;
        }
        long requestId = msgHeader.getRequestId();
        log.info("请求消息ID：{}", requestId);
        MiniRpcFuture<RpcResponse> future = RpcRequestHolder.REQUEST_MAP.remove(requestId);
        future.getPromise().setSuccess(protocol.getBody());
        log.info("处理请求成功！设置返回结果为:{}", protocol.getBody());
        channelHandlerContext.channel().writeAndFlush(future);
    }
}

