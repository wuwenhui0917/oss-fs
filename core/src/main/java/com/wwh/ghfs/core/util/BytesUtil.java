package com.wwh.ghfs.core.util;

import java.nio.ByteBuffer;

/**
 * Copyright asiainfo.com
 *
 * @author wuwh6
 */
public class BytesUtil {

    /**
     * long转byte
     *
     * @param l
     * @return
     */
    public static byte[] getLongtoBytes(long l) {
        ByteBuffer buff = ByteBuffer.allocate(8);
        buff.putLong(l);
        return buff.array();
    }

    public static byte[] intToByteArray(int a) {
        return new byte[]{
                (byte) ((a >> 24) & 0xFF),
                (byte) ((a >> 16) & 0xFF),
                (byte) ((a >> 8) & 0xFF),
                (byte) (a & 0xFF)
        };
    }
}
