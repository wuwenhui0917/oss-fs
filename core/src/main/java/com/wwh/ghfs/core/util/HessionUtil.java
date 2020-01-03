package com.wwh.ghfs.core.util;

import com.alibaba.com.caucho.hessian.io.Hessian2Input;
import com.alibaba.com.caucho.hessian.io.Hessian2Output;
import com.alibaba.com.caucho.hessian.io.Serializer;
import com.alibaba.com.caucho.hessian.io.SerializerFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Copyright asiainfo.com
 *
 * @author wuwh6
 */
public class HessionUtil {


    public static <T> T decode(byte[] bytes) {
        T obj = null;
        try (ByteArrayInputStream is = new ByteArrayInputStream(bytes);) {
            Hessian2Input input = new Hessian2Input(is);
            obj = (T) input.readObject();
            input.close();
        } catch (IOException e) {
            throw new RuntimeException("Hessian decode error:",e);
        }
        return obj;
    }

    public static  <T> byte[] encode(T t) {
        byte[] stream = null;
        SerializerFactory hessian = HessionSerialFactory.getInstance();
        try {
            Serializer serializer = hessian.getSerializer(t.getClass());
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            Hessian2Output output = new Hessian2Output(baos);
            serializer.writeObject(t, output);
            output.close();
            stream = baos.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException("Hessian encode error:",e);
        }
        return stream;
    }


}
