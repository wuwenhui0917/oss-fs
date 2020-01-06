package com.wwh.ghfs.fs;

/**
 * Copyright asiainfo.com
 * 分布式文件句柄
 * @author wuwh6
 */
public interface GFile {


    /**
     * 创建一个文件
     * @param fileName
     */
    public void createFile(String fileName);

    /**
     * 文件大小
     */
    public void size();

    /**
     * 获取文件块信息
     * @return
     */
    public BlockInfo[] getBlock();
}
