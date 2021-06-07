package com.mini.rpc.rpcprotocol.contant;

import lombok.Getter;

/***
 * @description 消息类型的枚举
 * @author <h>cuitao@aixuexi.com</h>
 * @date 2021-05-21 11:18
 * @since V1.0.0
 */
public enum MsgTypeEnum {
    REQUEST(1),
    RESPONSE(2),
    HEARTBEAT(3);

    @Getter
    private int type;

    MsgTypeEnum(int type) {
        this.type = type;
    }

    public static MsgTypeEnum findTypeEnumByType(byte type) {
        switch (type){
            case 1:
                return REQUEST;
            case 2:
                return RESPONSE;
            case 3:
                return HEARTBEAT;
            default:
                return null;
        }
    }

}
