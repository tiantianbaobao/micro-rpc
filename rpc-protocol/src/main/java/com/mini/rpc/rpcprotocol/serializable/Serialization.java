package com.mini.rpc.rpcprotocol.serializable;

import java.io.IOException;

/***
 * @description
 * @author <h>cuitao@aixuexi.com</h>
 * @date 2021-05-20 0:40
 * @since V1.0.0
 */
public interface Serialization {
    /***
     * 序列化方法
     * @param obj
     * @param <T>
     * @return
     * @throws IOException
     */
    <T> byte[] serialize(T obj) throws IOException;

    /***
     * 抽象的反序列化方法
     * @param data 字节流
     * @param clazz 目标对象的类
     * @param <T> 泛型对象
     * @return T
     * @throws IOException ioException
     */
    <T> T deserialize(byte[] data, Class<T> clazz) throws IOException;
}
