package com.wwh.ghfs.io;

import com.wwh.ghfs.configure.NettyClientConfig;
import com.wwh.ghfs.core.util.NameThreadFactory;
import com.wwh.ghfs.exception.GhfsRunExcepion;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.concurrent.DefaultEventExecutorGroup;
import java.net.InetSocketAddress;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Copyright asiainfo.com
 *
 * @author wuwh6
 */
public  abstract class AbstractClientBootStrap extends AbstractChannelHandle {

    private NettyClientConfig nettyClientConfig;
    private final Bootstrap bootstrap = new Bootstrap();
    private DefaultEventExecutorGroup defaultEventExecutorGroup;
    private NioEventLoopGroup eventLoopGroupWorker;
    private ChannelHandler[] handler;
    public AbstractClientBootStrap(NettyClientConfig config, ChannelHandler... handlers){

        super(new ThreadPoolExecutor(NettyBaseConfig.DEFAUL_THREAD_CORE_MESSAGE_SIZE,NettyBaseConfig.DEFAUL_THREAD_MAX_MESSAGE_SIZE,
                60, TimeUnit.MINUTES,new ArrayBlockingQueue<Runnable>(500),new NameThreadFactory("client-message-handle-")));
        nettyClientConfig = config;
        int selectorThreadSizeThreadSize = this.nettyClientConfig.getClientSelectorThreadSize();
        this.eventLoopGroupWorker = new NioEventLoopGroup(selectorThreadSizeThreadSize,
                new NameThreadFactory("ghfs-client-selector"));
        this.handler = handlers;
    }

    public void start(){
        this.defaultEventExecutorGroup = new DefaultEventExecutorGroup(nettyClientConfig.getClientWorkerThreads(),
                new NameThreadFactory("ghfs-client-"));
        this.bootstrap.group(this.eventLoopGroupWorker).channel(
                nettyClientConfig.getClientChannelClazz()).option(
                ChannelOption.TCP_NODELAY, true).option(ChannelOption.SO_KEEPALIVE, true).option(
                ChannelOption.CONNECT_TIMEOUT_MILLIS, nettyClientConfig.getConnectTimeoutMillis()).option(
                ChannelOption.SO_SNDBUF, nettyClientConfig.getClientSocketSndBufSize()).option(ChannelOption.SO_RCVBUF,
                nettyClientConfig.getClientSocketRcvBufSize());
        //epoll 模式
//        bootstrap.option(EpollChannelOption.EPOLL_MODE, EpollMode.EDGE_TRIGGERED)
//                .option(EpollChannelOption.TCP_QUICKACK, true);
        bootstrap.handler(
                new ChannelInitializer<SocketChannel>() {

                    @Override
                    public void initChannel(SocketChannel ch) {
                        ChannelPipeline pipeline = ch.pipeline();
                        pipeline.addLast(
                                new IdleStateHandler(nettyClientConfig.getChannelMaxReadIdleSeconds(),
                                        2,
                                        2))
                                .addLast(new  MessageDecoder())
                                .addLast(new  MessageEncoder());
                        if (null != handler) {
                            ch.pipeline().addLast(handler);
                        }
                        else {
                            ch.pipeline().addLast(AbstractClientBootStrap.this);
                        }
                    }
                });

    }

    public Channel getNewChannel(InetSocketAddress address) {
        Channel channel;
        ChannelFuture f = this.bootstrap.connect(address);
        try {
            f.await(this.nettyClientConfig.getConnectTimeoutMillis(), TimeUnit.MILLISECONDS);
            if (f.isCancelled()) {
                throw new GhfsRunExcepion("connect cancelled, can not connect to "+address);
            } else if (!f.isSuccess()) {
                throw new GhfsRunExcepion("connect failed, can not connect to "+address);
            } else {
                channel = f.channel();
            }
        } catch (Exception e) {
            throw new GhfsRunExcepion( "can not connect to "+address);
        }
        return channel;
    }

}
