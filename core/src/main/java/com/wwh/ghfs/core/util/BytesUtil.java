package com.wwh.ghfs.core.util;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.util.zip.CRC32;

/**
 * Copyright asiainfo.com
 *
 * @author wuwh6
 */
public class BytesUtil {

    public final static String CHARTSET="utf-8";

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

    public static byte[] append(String bytes,int size,char defaultString){

        try {
            byte[] bys = bytes.getBytes(CHARTSET);
            if(bys.length>=size){
                return bys;
            }
            int diff = size-bys.length;
            StringBuilder builder = new StringBuilder(bytes);
            for(int i=0;i<diff;i++){
                builder.append(defaultString);
            }
            return builder.toString().getBytes(CHARTSET);

        } catch (UnsupportedEncodingException e) {

        }
        return null;


    }

    public static long getLong(byte[] pamrabytes) {
        byte[] longbytes = new byte[8];
        System.arraycopy(pamrabytes, 0, longbytes, 0, 8);
        ByteBuffer buffer = ByteBuffer.wrap(longbytes);
        return buffer.getLong();
    }

    public static int getIntFormArrayBytes(byte[] pamrabytes) {
        ByteBuffer buffer = ByteBuffer.wrap(pamrabytes);
        return buffer.getInt();
    }

    public static String getHex(byte[] bytes) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(bytes[i] & 0xFF);
            if (hex.length() < 2) {
                sb.append(0);
            }
            sb.append(hex).append(" ");
        }
        return sb.toString().toUpperCase();

    }

    /**
     * crc校验码
     * @param data
     * @return
     */
    public static long getCrc(byte[] data){
        CRC32 crc32 = new CRC32();
        crc32.update(data);
        return crc32.getValue();

    }

//    public static int getInt(byte[] bytes){
//        int i=0;
//        i+=bytes[0];
//        i+=bytes[1]<<8;
//        i+=bytes[2]<<16;
//        i+=bytes[3]<<24;
////        int number = 0;
////        for(int i = 0; i < 4 ; i++){
////            number += bytes[i] << i*8;
////        }
////        return number;
//
//        return i;
//    }

}
