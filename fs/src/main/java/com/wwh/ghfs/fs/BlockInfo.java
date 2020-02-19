package com.wwh.ghfs.fs;

import com.wwh.ghfs.core.util.BytesUtil;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;

/**
 * Copyright asiainfo.com
 * 文件块大小索引，总共80个字节
 *
 * 8b的id+8b的长度+32的校验码+32字节的地址
 * @author wuwh6
 */
public class BlockInfo {
    /**块Id 8个字节*/
    public long blockId;
    /**块保存地址*/
    public String blockAddress;
    /**块对应的大小*/
    public long blockSize;
    /**校验码*/
    public String crc="";

    public long getBlockId() {
        return blockId;
    }

    public void setBlockId(long blockId) {
        this.blockId = blockId;
    }

    public String getBlockAddress() {
        return blockAddress;
    }

    public void setBlockAddress(String blockAddress) {

        this.blockAddress = blockAddress;
    }

    public long getBlockSize() {
        return blockSize;
    }

    public void setBlockSize(long blockSize) {
        this.blockSize = blockSize;
    }

    public String getCrc() {
        return crc;
    }

    public void setCrc(String crc) {
        this.crc = crc;
    }

    public String getStorageId() {
        return storageId;
    }

    public void setStorageId(String storageId) {
        this.storageId = storageId;
    }
    /**快对应的存储ID*/
    public String storageId;

    public byte[] getbytes(){
       ByteBuffer buffer = ByteBuffer.allocate(80);
       buffer.putLong(this.blockId);
       buffer.putLong(this.blockSize);
       buffer.put(BytesUtil.append(this.crc,32,' '),0,32);
       buffer.put(BytesUtil.append(this.blockAddress,32,' '),0,32);
       return buffer.array();
    }

    public void loadBlack(byte[] bytes){
        this.blockId = BytesUtil.getLong(bytes);
        byte[] lenght = new byte[8];
        System.arraycopy(bytes,8,lenght,0,8);
        this.blockSize = BytesUtil.getLong(lenght);
        byte[] src = new byte[32];
        System.arraycopy(bytes,16,src,0,32);
        this.crc = new String(src);
        byte[] address = new byte[32];
        System.arraycopy(bytes,48,address,0,32);
        this.blockAddress = new String(address);
    }

}
