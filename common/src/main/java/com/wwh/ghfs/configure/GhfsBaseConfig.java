package com.wwh.ghfs.configure;

/**
 * Copyright asiainfo.com
 *
 * @author wuwh6
 */
public class GhfsBaseConfig {

    /**默认数据块大小为64M*/
    private int blockSize=64*1024*1024;
    private String dir="./";

    public String getDataDir(){
        return dir;
    }
}
