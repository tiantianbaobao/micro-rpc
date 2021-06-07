package com.mini.rpc.rpcprotocol.serializable;

import com.alibaba.fastjson.JSONObject;

import java.io.IOException;

/***
 * @description {@link Serialization} JSON implemet
 * @author <h>cuitao@aixuexi.com</h>
 * @date 2021-05-20 10:32
 * @since V1.0.0
 */
public class JsonSerialization implements Serialization{
    @Override
    public <T> byte[] serialize(T obj) throws IOException {
        if (obj == null) {
            throw new NullPointerException("序列化对象为空");
        }
        byte[] result =null;
        try{
            result = JSONObject.toJSONBytes(obj);
        }catch (Exception ex) {
//            throw new SerializationException(ex);
        }
        return result;
    }

    @Override
    public <T> T deserialize(byte[] data, Class<T> clazz) throws IOException {
        if (data == null) {
            throw new NullPointerException("反序列化对象为空");
        }
        T obj = null;
        try{
            obj = JSONObject.parseObject(data, clazz);;
        }catch (Exception ex) {
//            throw new SerializationException(ex);
        }
        return obj;
    }
}
