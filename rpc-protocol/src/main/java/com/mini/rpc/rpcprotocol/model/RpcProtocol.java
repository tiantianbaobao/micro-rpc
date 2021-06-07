package com.mini.rpc.rpcprotocol.model;

import lombok.Data;

import java.io.Serializable;

/***
 * @description
 * @author <h>cuitao@aixuexi.com</h>
 * @date 2021-05-20 0:34
 * @since V1.0.0
 */
@Data
public class RpcProtocol<T> implements Serializable {
    private MsgHeader msgHeader;

    private T body;

    @Data
    public static class MsgHeader implements Serializable{
        private short magic; // 魔数
        private byte version; // 协议版本号
        private byte serialization; // 序列化算法
        private byte msgType; // 报文类型
        private byte status; // 状态
        private long requestId; // 消息 ID
        private int msgLen; // 数据长度
    }
}
