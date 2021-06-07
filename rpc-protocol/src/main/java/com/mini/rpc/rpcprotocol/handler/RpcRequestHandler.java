package com.mini.rpc.rpcprotocol.handler;

import com.mini.rpc.rpccore.common.RpcRegistryHelper;
import com.mini.rpc.rpccore.common.RpcRequest;
import com.mini.rpc.rpccore.common.RpcResponse;
import com.mini.rpc.rpcprotocol.contant.MsgTypeEnum;
import com.mini.rpc.rpcprotocol.model.RpcProtocol;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cglib.reflect.FastClass;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

/***
 * @description {@link io.netty.channel.SimpleChannelInboundHandler} 实现
 * @author <h>cuitao@aixuexi.com</h>
 * @date 2021-05-21 11:35
 * @since V1.0.0
 */
@Slf4j
public class RpcRequestHandler extends SimpleChannelInboundHandler<RpcProtocol<RpcRequest>> {

    private final Map<String, Object> rpcServiceMap;
    public RpcRequestHandler(Map<String, Object> rpcServiceMap) {
    this.rpcServiceMap = rpcServiceMap;
    }
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, RpcProtocol<RpcRequest> protocol) throws Exception {
        RpcRequestProcessor.submitTask(() -> {
            RpcProtocol<RpcResponse> responseRpcProtocol = new RpcProtocol<>();
            RpcResponse rpcResponse = new RpcResponse();
            RpcProtocol.MsgHeader msgHeader = protocol.getMsgHeader();
            msgHeader.setMsgType((byte) MsgTypeEnum.RESPONSE.getType());
            try{
                log.info("开始处理RPC请求，请求参数：{}", protocol.toString());
                Object data = handle(protocol.getBody());
                log.info("反射执行本地方法返回结果:{}", data);
                rpcResponse.setData(data);
                rpcResponse.setMsg("SUCCESS!");
                msgHeader.setStatus((byte) 1);
                responseRpcProtocol.setBody(rpcResponse);
                responseRpcProtocol.setMsgHeader(msgHeader);

            }catch (Throwable ex){
                rpcResponse.setMsg(ex.toString());
                rpcResponse.setData(null);
                msgHeader.setStatus((byte)2);
                responseRpcProtocol.setBody(rpcResponse);
                responseRpcProtocol.setMsgHeader(msgHeader);
                log.error("处理请求[requestId:{}失败！失败原因:{}]", msgHeader.getRequestId(), ex.getMessage());
            }
            channelHandlerContext.writeAndFlush(responseRpcProtocol);
        });
    }

    /***
     * 反射代理执行
     * @param request
     * @return
     * @throws InvocationTargetException
     */
    private Object handle(RpcRequest request) throws InvocationTargetException {
        String serviceKey = RpcRegistryHelper.buildServiceKey(request.getServiceClass(), request.getServiceVersion());
        Object serviceBean = rpcServiceMap.get(serviceKey);
        if (serviceBean == null) {
            throw new RuntimeException(String.format("service not exist! target service is %s, target method is %s",
                    request.getServiceClass(), request.getMethodName()));
        }
        Class<?> beanClass = serviceBean.getClass();
        String methodName = request.getMethodName();
        Object[] params = request.getParams();
        Class<?>[] paramTypes = request.getParamTypes();
        FastClass fastClass = FastClass.create(beanClass);
        int index = fastClass.getIndex(methodName, paramTypes);
        log.info("执行到代理本地方法");
        return fastClass.invoke(index, serviceBean, params);
    }
}
