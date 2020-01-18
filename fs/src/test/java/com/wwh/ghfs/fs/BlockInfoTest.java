package com.wwh.ghfs.fs;


import org.junit.Test;

import java.nio.ByteBuffer;

/**
 * Copyright asiainfo.com
 *
 * @author wuwh6
 */
public class BlockInfoTest {

    @Test
    public void testLong(){
        ByteBuffer buffer = ByteBuffer.allocate(8);
        buffer.putLong(0,1000000000000000000L);
        System.out.println(buffer.array().length);
        long s=9l;
        byte[] bytes = new byte[8];
        bytes[0]=(byte)(0xFF&s);

    }

}