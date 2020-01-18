package com.wwh.ghfs.fs.index;

/**
 * Copyright asiainfo.com
 * 索引文件
 * @author wuwh6
 */
public interface IndexFile {

    /**
     * 加载索引文件
     * @param fileName
     */
    public void loadIndex(String fileName);

    /**
     * 写入索引信息
     * @param info
     */
    public void write(IndexInfo info);


}
