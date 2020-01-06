package com.wwh.ghfs.core.protocol;

import java.io.Serializable;

/**
 * Copyright asiainfo.com
 * 报文基本类型
 * @author wuwh6
 */
public interface DataPackage extends Serializable {

    /**获取报文类型*/
    int  getPackType();

    /**
     * 获取真实数据
     * @return
     */
    byte[] getData();

    /**
     * 进行校验
     * @return
     */
    boolean check();





}
