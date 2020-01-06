package com.wwh.ghfs.fs;

import com.wwh.ghfs.client.ClientTransport;
import com.wwh.ghfs.configure.GhfsBaseConfig;

/**
 * Copyright asiainfo.com
 * 创建文件
 * @author wuwh6
 */
public class GFileImp implements GFile {

    private ClientTransport transport;

    public GFileImp(GhfsBaseConfig config){

    }


    @Override
    public void createFile(String fileName) {

    }

    @Override
    public void size() {

    }

    @Override
    public BlockInfo[] getBlock() {
        return new BlockInfo[0];
    }

    @Override
    public boolean isWrite() {
        return false;
    }
}
