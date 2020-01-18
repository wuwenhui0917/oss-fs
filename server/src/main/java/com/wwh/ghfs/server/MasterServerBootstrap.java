package com.wwh.ghfs.server;

import com.wwh.ghfs.core.protocol.ProtocolConstans;
import com.wwh.ghfs.core.protocol.RequestPackage;
import com.wwh.ghfs.fs.BlockInfo;
import com.wwh.ghfs.fs.GFile;
import com.wwh.ghfs.io.NettyBaseConfig;
import com.wwh.ghfs.io.AbstractServerBootStrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;

import java.util.List;

/**
 * Copyright asiainfo.com
 *
 * @author wuwh6
 */
public abstract class MasterServerBootstrap extends AbstractServerBootStrap {

    public MasterServerBootstrap(NettyBaseConfig config, ChannelHandler... handlers) {
        super(config, handlers);
    }

    /**
     * 创建文件
     * @return
     */
    abstract  GFile  doCreateFile();
    /**
     * 创建块
     * @return
     */
    abstract BlockInfo doCreateBlock();

    abstract  GFile select(String fileName);

    @Override
    public void dispatch(RequestPackage msg, ChannelHandlerContext context) {
        if(msg instanceof RequestPackage){
            //MSGTYPE_RESQUEST_ONEWAY
            RequestPackage req = (RequestPackage)msg;
            Object obj = msg.getBody();


            //请求报文需要返回
            if(req.getMessageType()==ProtocolConstans.MSGTYPE_RESQUEST){
                this.sendResponse(context.channel(),obj,msg.getId());
            }
            //不需要返回
            else if(req.getMessageType()==ProtocolConstans.MSGTYPE_RESQUEST_ONEWAY){
               logger.info(" no  need return resultInfo");
            }
        }
    }

    @Override
    public void destroyChannel(Channel channel) {
        channel.disconnect();
        channel.close();

    }
}
