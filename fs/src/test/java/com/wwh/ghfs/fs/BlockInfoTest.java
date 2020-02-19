package com.wwh.ghfs.fs;


import com.wwh.ghfs.core.util.BytesUtil;
import org.junit.Assert;
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
    @Test
    public void testLoad(){
        BlockInfo block = new BlockInfo();
        block.setBlockId(12111);
        block.setBlockSize(10240);
        block.setBlockAddress("127.0.0.1");
        block.setStorageId("12323");
        byte[] btes = block.getbytes();
        System.out.println(BytesUtil.getHex(btes));
        BlockInfo bl = new BlockInfo();
        bl.loadBlack(btes);
        System.out.println();
        Assert.assertEquals(bl.blockId,block.blockId);

        System.out.println(bl.getBlockSize());
        System.out.println(bl.getBlockAddress());



    }

}