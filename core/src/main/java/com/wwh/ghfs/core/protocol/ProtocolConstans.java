package com.wwh.ghfs.core.protocol;

/**
 * Copyright asiainfo.com
 *
 * @author wuwh6
 */
public class ProtocolConstans {

    /**
     * Magic code
     */
    public static final byte[] MAGIC_CODE_BYTES = {(byte) 0xda, (byte) 0xda};

    /**
     * Protocol version
     */
    public static final byte VERSION = 1;


    /**
     * Max frame length
     */
    public static final int MAX_FRAME_LENGTH = 64 * 1024 * 1024+1024;

    /***
     * is Gzip compress
     */
    public static final byte COMPREE_TYPE_GZIP = 1;

    public static final byte CODE_TYPE_HESSION = 1;


    /**
     * is no compress
     */
    public static final byte COMPREE_TYPE_NO = 0;

    /**
     * v1 version head_lenght 14
     */
    public static final int V1_HEAD_LENGTH =14;


    /**
     * Message type: Request
     */
    public static final byte MSGTYPE_RESQUEST = 0;
    /**
     * Message type: Response
     */
    public static final byte MSGTYPE_RESPONSE = 1;
    /**
     * Message type: Request which no need response
     */
    public static final byte MSGTYPE_RESQUEST_ONEWAY = 2;
    /**
     * Message type: Heartbeat Request
     */
    public static final byte MSGTYPE_HEARTBEAT_REQUEST = 3;
    /**
     * Message type: Heartbeat Response
     */
    public static final byte MSGTYPE_HEARTBEAT_RESPONSE = 4;
    /**
     * 文件数据类型
     */
    public static final int DATA_PACKAGE_FILEDATA=1;



}
