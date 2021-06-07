package com.mini.rpc.rpcprotocol.serializable;

/***
 * @description {@link Serialization}factory
 * @author <h>cuitao@aixuexi.com</h>
 * @date 2021-05-20 10:47
 * @since V1.0.0
 */
public class SerializationFactory {
    public static Serialization createSerialization(byte serializationType) {
        SerializationTypeEnum typeEnum = SerializationTypeEnum.findTypeEnumByType(serializationType);
        switch (typeEnum) {
            case JSON:
                return new JsonSerialization();
            case HESSIAN:
                return new HessianSerialization();
            default:
                throw new IllegalArgumentException();
        }
    }
}
