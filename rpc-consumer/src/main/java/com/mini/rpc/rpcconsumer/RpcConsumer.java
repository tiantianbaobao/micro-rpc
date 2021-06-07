package com.mini.rpc.rpcconsumer;

import com.mini.rpc.rpccore.common.RpcRequest;
import com.mini.rpc.rpcprotocol.codec.decode.MiniRpcDecoder;
import com.mini.rpc.rpcprotocol.codec.encode.MiniRpcEncoder;
import com.mini.rpc.rpcprotocol.handler.RpcResponseHandler;
import com.mini.rpc.rpcprotocol.model.RpcProtocol;
import com.minimrpc.rpcregistry.model.ServiceMetadata;
import com.minimrpc.rpcregistry.service.RegistryService;
import com.mini.rpc.rpccore.common.RpcRegistryHelper;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.extern.slf4j.Slf4j;

/***
 * @description
 * @author <h>cuitao@aixuexi.com</h>
 * @date 2021-05-24 0:38
 * @since V1.0.0
 */
@Slf4j
public class RpcConsumer {

    private final Bootstrap bootstrap;

    private final EventLoopGroup  eventLoopGroup;

    public RpcConsumer() {
        bootstrap = new Bootstrap();
        eventLoopGroup = new NioEventLoopGroup(4);
        bootstrap.group(eventLoopGroup).channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {

                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        socketChannel.pipeline()
                                .addLast(new MiniRpcEncoder())
                                .addLast(new MiniRpcDecoder())
                                .addLast(new RpcResponseHandler());
                    }
                });
    }

    public void sendRequest(RpcProtocol<RpcRequest> protocol, RegistryService registryService) throws Exception {
        RpcRequest protocolBody = protocol.getBody();
        Object[] params = protocolBody.getParams();
        String serviceKey = RpcRegistryHelper.buildServiceKey(protocolBody.getServiceClass(), protocolBody.getServiceVersion());
        int hashCode = params !=null && params.length > 0? params[0].hashCode() : serviceKey.hashCode();
        ServiceMetadata serviceMetadata = registryService.discovery(serviceKey, hashCode);
        if (serviceMetadata != null) {
            ChannelFuture channelFuture = bootstrap.connect(serviceMetadata.getServiceAddr(), serviceMetadata.getServicePort()).sync();
            channelFuture.addListener(arg -> {
               if (channelFuture.isSuccess()) {
                   log.info("connect remote server[host:{}, port:{} success!]", serviceMetadata.getServiceAddr(), serviceMetadata.getServicePort());
               }else {
                   log.error("connect remote server[host:{}, port:{} failed!]", serviceMetadata.getServiceAddr(), serviceMetadata.getServicePort());
                   channelFuture.cause().printStackTrace();
                   eventLoopGroup.shutdownGracefully();
               }
            });
            log.info("protocol -> " + protocol);
            channelFuture.channel().writeAndFlush(protocol);
            log.info("发送请求成功！");
        }else {
            log.error("get serviceMetadata failed！serviceMetadata is null!");
        }
    }
}
