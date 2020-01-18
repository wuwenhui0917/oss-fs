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

    /**
     * 读取存储块
     * @return
     */
   byte[] read();

    /**
     * 获取存储ID
     * @return
     */
   String getStorageId();


    /**
     * 锁定
     */
    void lock();

    /**
     * 解锁
     */
    void unlock();

    /**
     * 校验
     * @return
     */
    boolean check();

}
