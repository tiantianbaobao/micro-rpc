package com.mini.rpc.rpcprotocol.codec.encode;

import com.mini.rpc.rpcprotocol.model.RpcProtocol;
import com.mini.rpc.rpcprotocol.serializable.Serialization;
import com.mini.rpc.rpcprotocol.serializable.SerializationFactory;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import lombok.extern.slf4j.Slf4j;

/***
 * @description custom {@link MessageToByteEncoder}implement
 * @author <h>cuitao@aixuexi.com</h>
 * @date 2021-05-20 11:11
 * @since V1.0.0
 */
@Slf4j
public class MiniRpcEncoder extends MessageToByteEncoder<RpcProtocol<Object>> {
    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, RpcProtocol<Object> msg, ByteBuf byteBuf) throws Exception {
        log.info("开始处理编码过程....");
        RpcProtocol.MsgHeader msgHeader = msg.getMsgHeader();
        byteBuf.writeShort(msgHeader.getMagic());
        byteBuf.writeByte(msgHeader.getVersion());
        byteBuf.writeByte(msgHeader.getSerialization());
        byteBuf.writeByte(msgHeader.getMsgType());
        byteBuf.writeByte(msgHeader.getStatus());
        byteBuf.writeLong(msgHeader.getRequestId());
        byte serializationType = msgHeader.getSerialization();
        Serialization serialization = SerializationFactory.createSerialization(serializationType);
        byte[] bytes = serialization.serialize(msg.getBody());
        byteBuf.writeInt(bytes.length);
        byteBuf.writeBytes(bytes);
        log.info("处理编码过程结束！");
    }
}
