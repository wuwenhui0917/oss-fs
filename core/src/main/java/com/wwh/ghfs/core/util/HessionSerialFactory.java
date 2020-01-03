package com.wwh.ghfs.core.util;

import com.alibaba.com.caucho.hessian.io.Deserializer;
import com.alibaba.com.caucho.hessian.io.HessianProtocolException;
import com.alibaba.com.caucho.hessian.io.Serializer;
import com.alibaba.com.caucho.hessian.io.SerializerFactory;

/**
 * Copyright asiainfo.com
 *
 * @author wuwh6
 */
public class HessionSerialFactory extends SerializerFactory {

    public static final SerializerFactory INSTANCE = new HessionSerialFactory();

    private HessionSerialFactory() {
        super();
    }

    public static SerializerFactory getInstance() {
        return INSTANCE;
    }


}
