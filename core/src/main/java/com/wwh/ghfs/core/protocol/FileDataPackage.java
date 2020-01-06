package com.wwh.ghfs.core.protocol;

/**
 * Copyright asiainfo.com
 *
 * @author wuwh6
 */
public class FileDataPackage implements DataPackage {
    @Override
    public int getPackType() {

        return ProtocolConstans.DATA_PACKAGE_FILEDATA;
    }

    @Override
    public byte[] getData() {
        return new byte[0];
    }

    @Override
    public boolean check() {
        return false;
    }


}
