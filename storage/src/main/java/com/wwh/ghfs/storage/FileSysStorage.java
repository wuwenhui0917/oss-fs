package com.wwh.ghfs.storage;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * Copyright asiainfo.com
 *
 * @author wuwh6
 */
public  abstract  class FileSysStorage implements Storage {

    private String dir;
    private File dataDir;
    public FileSysStorage(String dataDir){
        this.dir = dataDir;
        if(this.dir!=null){
            this.dataDir = new File(this.dir);
            if(!this.dataDir.exists()){
                this.dataDir.mkdirs();
            }
        }
    }

    @Override
    public String write(String dataName, byte[] bytes) {
        try {
            RandomAccessFile rf = new RandomAccessFile(dataName,"rw");
            FileChannel channel = rf.getChannel();
            ByteBuffer buff = ByteBuffer.wrap(bytes);
            try {
                channel.write(buff);
                channel.close();
            } catch (IOException e) {
            }
            finally {
                try {
                    if(channel!=null){
                        channel.close();
                    }
                    if(rf!=null){
                        rf.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (FileNotFoundException e) {
            return null;
        }
        return dataName;

    }
}
