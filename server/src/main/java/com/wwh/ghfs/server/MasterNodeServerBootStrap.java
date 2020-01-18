package com.wwh.ghfs.server;

import com.wwh.ghfs.fs.BlockInfo;
import com.wwh.ghfs.fs.GFile;
import com.wwh.ghfs.io.NettyBaseConfig;
import io.netty.channel.ChannelHandler;

/**
 * Copyright asiainfo.com
 *
 * @author wuwh6
 */
public class MasterNodeServerBootStrap extends MasterServerBootstrap {
    public MasterNodeServerBootStrap(NettyBaseConfig config, ChannelHandler... handlers) {
        super(config, handlers);
    }

    @Override
    GFile doCreateFile() {
        return null;
    }

    @Override
    BlockInfo doCreateBlock() {
        return null;
    }

    @Override
    GFile select(String fileName) {
        return null;
    }
}
