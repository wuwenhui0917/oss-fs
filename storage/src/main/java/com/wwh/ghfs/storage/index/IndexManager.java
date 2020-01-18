package com.wwh.ghfs.storage.index;

import java.io.File;
import java.io.FilenameFilter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Copyright asiainfo.com
 *
 * @author wuwh6
 */
public class IndexManager {

    private String idxDir="./idx";

    public  IndexManager(String indexDir){
        this.idxDir = indexDir;
    }

    public void init(){
        File indexFile = new File(this.idxDir);
        File[] files =  indexFile.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                if(name.endsWith(INDEXFILE)) {
                    return true;
                }
                return false;
            }
        });


    }
    public static final String INDEXFILE=".index";

    private Map<String,String> indexs = new ConcurrentHashMap<String,String>(10000);

    public void loadIndex(String indexDir){

    }
}
