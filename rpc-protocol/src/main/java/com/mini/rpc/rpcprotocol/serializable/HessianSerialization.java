package com.mini.rpc.rpcprotocol.serializable;

import com.caucho.hessian.io.HessianSerializerInput;
import com.caucho.hessian.io.HessianSerializerOutput;
import com.sun.xml.internal.ws.encoding.soap.SerializationException;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/***
 * @description {@link Serialization}de Hessian implement
 * @author <h>cuitao@aixuexi.com</h>
 * @date 2021-05-20 10:14
 * @since V1.0.0
 */
public class HessianSerialization implements Serialization{
    @Override
    public <T> byte[] serialize(T obj) throws IOException {
        if (obj == null) {
            throw new NullPointerException("序列化对象为空");
        }
        byte[] result;
        HessianSerializerOutput hessianSerializerOutput;
        try(ByteArrayOutputStream os = new ByteArrayOutputStream()) {
            hessianSerializerOutput = new HessianSerializerOutput(os);
            hessianSerializerOutput.writeObject(obj);
            hessianSerializerOutput.flush();
            result = os.toByteArray();
        }catch (Exception ex) {
            throw new SerializationException(ex);
        }
        return result;
    }

    @Override
    public <T> T deserialize(byte[] data, Class<T> clazz) throws IOException {
        if (data == null) {
            throw new NullPointerException("反序列化对象为空");
        }
        T result;
        try(ByteArrayInputStream is = new ByteArrayInputStream(data)) {
            HessianSerializerInput input = new HessianSerializerInput(is);
            result = (T) input.readObject(clazz);
        }catch (Exception ex) {
            throw new SerializationException(ex);
        }
        return result;
    }
}
