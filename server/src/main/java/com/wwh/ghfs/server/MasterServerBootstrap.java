package com.wwh.ghfs.server;

import com.wwh.ghfs.core.protocol.ProtocolConstans;
import com.wwh.ghfs.core.protocol.RequestPackage;
import com.wwh.ghfs.io.NettyBaseConfig;
import com.wwh.ghfs.io.AbstractServerBootStrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;

/**
 * Copyright asiainfo.com
 *
 * @author wuwh6
 */
public class MasterServerBootstrap extends AbstractServerBootStrap {

    public MasterServerBootstrap(NettyBaseConfig config, ChannelHandler... handlers) {
        super(config, handlers);
    }

    @Override
    public void dispatch(RequestPackage msg, ChannelHandlerContext context) {
        Object obj = msg.getBody();
        if(obj instanceof RequestPackage){
            RequestPackage req = (RequestPackage)obj;
            req.setMessageType(ProtocolConstans.MSGTYPE_RESPONSE);
            this.sendRequest(context.channel(),req);
        }
    }

    @Override
    public void destroyChannel(Channel channel) {
        channel.disconnect();
        channel.close();

    }
}
