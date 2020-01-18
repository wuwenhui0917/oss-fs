package com.wwh.ghfs.storage;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Copyright asiainfo.com
 *
 * @author wuwh6
 */
public class StorageFactory {

    private static Storage  storage= new FileSysStorage("data");

    private static  Map<String,Storage> storages = new ConcurrentHashMap<String,Storage>();

    static {
        storages.put("file",storage);
    }

    public static Storage getStorage(){
        return storage;
    }
}
