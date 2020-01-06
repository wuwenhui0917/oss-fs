package com.wwh.ghfs.storage;

/**
 * Copyright asiainfo.com
 *
 * @author wuwh6
 */
public class StorageFactory {

    private static Storage  storage= new FileSysStorage();

    public static Storage getStorage(){
        return storage;
    }
}
