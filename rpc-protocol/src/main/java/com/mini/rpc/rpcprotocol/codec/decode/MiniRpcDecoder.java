package com.mini.rpc.rpcprotocol.codec.decode;

import com.mini.rpc.rpccore.common.RpcRequest;
import com.mini.rpc.rpccore.common.RpcResponse;
import com.mini.rpc.rpcprotocol.contant.MsgTypeEnum;
import com.mini.rpc.rpcprotocol.contant.ProtocolConstants;
import com.mini.rpc.rpcprotocol.model.RpcProtocol;
import com.mini.rpc.rpcprotocol.serializable.Serialization;
import com.mini.rpc.rpcprotocol.serializable.SerializationFactory;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/***
 * @description custom {@link ByteToMessageDecoder}
 * @author <h>cuitao@aixuexi.com</h>
 * @date 2021-05-20 11:39
 * @since V1.0.0
 *
 * Procotol:
 * +---------------------------------------------------------------+
 *
 * | 魔数 2byte | 协议版本号 1byte | 序列化算法 1byte | 报文类型 1byte  |
 *
 * +---------------------------------------------------------------+
 *
 * | 状态 1byte |        消息 ID 8byte     |      数据长度 4byte     |
 *
 * +---------------------------------------------------------------+
 *
 * |                   数据内容 （长度不定）                          |
 *
 * +---------------------------------------------------------------+
 */
@Slf4j
public class MiniRpcDecoder extends ByteToMessageDecoder{
    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> out) throws Exception {
        log.info("开始处理解码过程...");
        if (byteBuf.readableBytes() < ProtocolConstants.HEADER_TOTAL_LENGTH){
            log.info("byteBuf.readableBytes() -> " + byteBuf.readableBytes());
            return;
        }
        log.info("1");
        byteBuf.markReaderIndex();
        short magic = byteBuf.readShort();
        if (magic != ProtocolConstants.PROTOCOL_MAGIC) {
            throw new IllegalArgumentException("protocol magic number is illegal! illegal magic number is : " + magic + ".");
        }
        byte protocolVersion = byteBuf.readByte();
        byte serializationType = byteBuf.readByte();
        byte msgType = byteBuf.readByte();
        byte status = byteBuf.readByte();
        long requestId = byteBuf.readLong();
        int dataLength = byteBuf.readInt();
        if (byteBuf.readableBytes() < dataLength) {
            //当前没读完，重置读标识位
            byteBuf.resetReaderIndex();
            log.info("2");
            return;
        }
        byte[] data = new byte[dataLength];
        byteBuf.readBytes(data);
        MsgTypeEnum typeEnumByType = MsgTypeEnum.findTypeEnumByType(msgType);
        if (typeEnumByType == null) {
            throw new IllegalArgumentException("Illegal message type! System can not support for current message type : " + msgType);
        }

        RpcProtocol.MsgHeader msgHeader = new RpcProtocol.MsgHeader();
        msgHeader.setMagic(magic);
        msgHeader.setVersion(protocolVersion);
        msgHeader.setSerialization(serializationType);
        msgHeader.setMsgType(msgType);
        msgHeader.setStatus(status);
        msgHeader.setRequestId(requestId);
        msgHeader.setMsgLen(dataLength);

        Serialization serialization = SerializationFactory.createSerialization(serializationType);
        log.info("开始执行序列化....");
        switch (typeEnumByType){
            case REQUEST:
                RpcRequest request = serialization.deserialize(data, RpcRequest.class);
                log.info("request -> " + request);
                if (request != null) {
                    RpcProtocol<RpcRequest> protocol = new RpcProtocol<>();
                    protocol.setBody(request);
                    protocol.setMsgHeader(msgHeader);
                    out.add(protocol);
                    log.info("处理请求的编码结束！");
                }
                break;
            case RESPONSE:
                RpcResponse response = serialization.deserialize(data, RpcResponse.class);
                log.info("response -> " + response);
                if (response != null) {
                    RpcProtocol<RpcResponse> protocol = new RpcProtocol<>();
                    protocol.setBody(response);
                    protocol.setMsgHeader(msgHeader);
                    out.add(protocol);
                    log.info("处理响应的编码结束！");
                }
                break;
            case HEARTBEAT:
                //TODO
                break;
//            default:
        }

        log.info("处理节码过程结束！");
    }
}
