package com.wwh.ghfs.core.protocol;

import java.io.Serializable;

/**
 * Copyright asiainfo.com
 * 报文解析处理类
 * @author wuwh6
 */
public class RequestPackage implements Serializable {

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public byte getMessageType() {
        return messageType;
    }

    public void setMessageType(byte messageType) {
        this.messageType = messageType;
    }

    public byte getCodec() {
        return codec;
    }

    public void setCodec(byte codec) {
        this.codec = codec;
    }

    public byte getCompressor() {
        return compressor;
    }

    public void setCompressor(byte compressor) {
        this.compressor = compressor;
    }

    public Object getBody() {
        return body;
    }

    public void setBody(Object body) {
        this.body = body;
    }

    private int id;
    private byte messageType;
    private byte codec;
    private byte compressor;
    private Object body;
}
