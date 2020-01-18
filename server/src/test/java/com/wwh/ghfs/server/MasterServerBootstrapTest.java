package com.wwh.ghfs.server;


import com.wwh.ghfs.io.NettyBaseConfig;
import org.junit.Test;

/**
 * Copyright asiainfo.com
 *
 * @author wuwh6
 */
public class MasterServerBootstrapTest {
    @Test
    public void server() throws Exception {
        MasterServerBootstrap str= new MasterNodeServerBootStrap(new NettyBaseConfig(),null);
        str.start(9999);
    }

}