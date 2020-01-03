package com.wwh.ghfs.client;

import com.wwh.ghfs.configure.NettyClientConfig;
import com.wwh.ghfs.core.protocol.RequestPackage;
import com.wwh.ghfs.io.AbstractClientBootStrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;

import java.net.InetSocketAddress;

/**
 * Copyright asiainfo.com
 *
 * @author wuwh6
 */
public class ClientTransport extends AbstractClientBootStrap {

    private Channel channel;
    public ClientTransport(NettyClientConfig config) {
        super(config, null);
    }

    @Override
    public void dispatch(RequestPackage msg, ChannelHandlerContext context) {

    }

    public void connect(String ip,int port){
        InetSocketAddress address = new InetSocketAddress(ip,port);
        this.channel = this.getNewChannel(address);
    }


    @Override
    public void destroyChannel(Channel channel) {
           if(channel!=null){
               channel.close();

           }
    }
}
