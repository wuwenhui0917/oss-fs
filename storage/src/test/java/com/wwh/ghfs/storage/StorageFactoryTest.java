package com.wwh.ghfs.storage;


import org.junit.Test;

/**
 * Copyright asiainfo.com
 *
 * @author wuwh6
 */
public class StorageFactoryTest {
    @Test
    public void getStorage() throws Exception {
        Storage store = StorageFactory.getStorage();
        store.write("hello.txt","你好".getBytes());
    }

}