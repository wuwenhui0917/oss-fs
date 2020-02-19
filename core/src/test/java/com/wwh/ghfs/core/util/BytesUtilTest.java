package com.wwh.ghfs.core.util;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Copyright asiainfo.com
 *
 * @author wuwh6
 */
public class BytesUtilTest {
    @Test
    public void getCrc() throws Exception {
        System.out.println(BytesUtil.getCrc("hello".getBytes()));

    }

    @org.junit.Test
    public void getInt() throws Exception {
        byte[] bytes = BytesUtil.intToByteArray(12333333);
        System.out.println(bytes.length);
        System.out.println(BytesUtil.getIntFormArrayBytes(bytes));
        System.out.println(BytesUtil.getIntFormArrayBytes(bytes));
        assertEquals(BytesUtil.getIntFormArrayBytes(bytes),12333333);

    }

}