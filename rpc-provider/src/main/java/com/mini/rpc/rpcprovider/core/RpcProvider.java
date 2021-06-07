package com.mini.rpc.rpcprovider.core;

import com.mini.rpc.rpcprotocol.codec.decode.MiniRpcDecoder;
import com.mini.rpc.rpcprotocol.codec.encode.MiniRpcEncoder;
import com.mini.rpc.rpcprotocol.handler.RpcRequestHandler;
import com.mini.rpc.rpcprovider.annotation.RpcService;
import com.minimrpc.rpcregistry.model.ServiceMetadata;
import com.minimrpc.rpcregistry.service.RegistryService;
import com.mini.rpc.rpccore.common.RpcRegistryHelper;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.BeanPostProcessor;

import java.net.InetAddress;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/***
 * @description
 * @author <h>cuitao@aixuexi.com</h>
 * @date 2021-05-19 14:40
 * @since V1.0.0
 */
@Slf4j
public class RpcProvider implements InitializingBean, BeanPostProcessor {

    private final int serverPort;

    private String serverAddress;

    private final RegistryService registryService;

    private final Map<String, Object> rpcServiceMap = new ConcurrentHashMap<>();

    public RpcProvider(int serverPort, RegistryService registryService) {
        this.serverPort = serverPort;
        this.registryService = registryService;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        new Thread(() -> {
            try {
                startRpcServer();
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }).start();
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        RpcService rpcService = bean.getClass().getAnnotation(RpcService.class);
        return null;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        RpcService rpcService = bean.getClass().getAnnotation(RpcService.class);
        if (rpcService != null) {
            String serviceName = rpcService.serviceInterface().getName();
            String serviceVersion = rpcService.version();
            try{
                ServiceMetadata serviceMetadata = new ServiceMetadata();
                serviceMetadata.setServiceAddr(this.serverAddress);
                serviceMetadata.setServicePort(this.serverPort);
                serviceMetadata.setServiceName(serviceName);
                serviceMetadata.setServiceVersion(serviceVersion);
                //registry service
                registryService.register(serviceMetadata);
                rpcServiceMap.put(RpcRegistryHelper.buildServiceKey(serviceName, serviceVersion), bean);
            }catch (Exception ex) {
                System.err.println("服务注册失败！失败信息：" + ex.getMessage());
            }
        }
        return bean;
    }

    /***
     * 启动服务
     * @throws Exception
     */
    public void startRpcServer() throws Exception{
        this.serverAddress = InetAddress.getLocalHost().getHostAddress();
        EventLoopGroup boss = new NioEventLoopGroup();
        EventLoopGroup worker = new NioEventLoopGroup();
        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(boss, worker)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline()
                                    .addLast(new MiniRpcEncoder())
                                    .addLast(new MiniRpcDecoder())
                                    .addLast(new RpcRequestHandler(rpcServiceMap));
                        }
                    }).childOption(ChannelOption.SO_KEEPALIVE, true);
            ChannelFuture channelFuture = serverBootstrap.bind(this.serverAddress, this.serverPort).sync();
            System.err.println("recServiceMap -> " + rpcServiceMap);
            System.out.println("server has started! server address is : " + this.serverAddress + ", server port is : " + this.serverPort);
            log.info("00000");
            channelFuture.channel().closeFuture().sync();
            log.info("111111");
        }catch (Exception exception) {
            //
            log.error("处理异常！错误信息：{}", exception.getMessage(), exception);
        }finally {
            boss.shutdownGracefully();
            worker.shutdownGracefully();
        }
    }
}
