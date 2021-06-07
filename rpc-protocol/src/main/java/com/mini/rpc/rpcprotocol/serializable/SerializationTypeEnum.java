package com.mini.rpc.rpcprotocol.serializable;

/***
 * @description
 * @author <h>cuitao@aixuexi.com</h>
 * @date 2021-05-20 11:04
 * @since V1.0.0
 */
public enum SerializationTypeEnum {
    /***
     * 序列化方式
     */
    HESSIAN(1),
    JSON(2);

    private int type;

    SerializationTypeEnum(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    /***
     *
     * @param type
     * @return
     */
    public static SerializationTypeEnum findTypeEnumByType(byte type) {
        if (type == 1) {
            return HESSIAN;
        }else if (type == 2){
            return JSON;
        }
        return null;
    }
}
