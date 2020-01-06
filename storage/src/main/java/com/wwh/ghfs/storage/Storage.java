package com.wwh.ghfs.storage;

/**
 * Copyright asiainfo.com
 *
 * @author wuwh6
 */
public interface Storage {

    /**
     * 写入存储中
     * @param bytes
     */
   String write(String dataName,byte[] bytes);

}
