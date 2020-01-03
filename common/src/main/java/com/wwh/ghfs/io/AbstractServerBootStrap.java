package com.wwh.ghfs.io;

import com.wwh.ghfs.core.util.NameThreadFactory;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Copyright asiainfo.com
 *
 * @author wuwh6
 */
@ChannelHandler.Sharable
public  abstract  class AbstractServerBootStrap extends AbstractChannelHandle  {


    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractServerBootStrap.class);
    private int connections=2;
    private int works=100;
    private NettyBaseConfig nettyServerConfig;
    protected ChannelHandler[] channelHandlers=null;
    private ServerBootstrap serverbootstrap = null;
    private final AtomicBoolean initialized = new AtomicBoolean(false);
    public AbstractServerBootStrap(NettyBaseConfig config, final ChannelHandler... handlers){
        super(new ThreadPoolExecutor(NettyBaseConfig.DEFAUL_THREAD_CORE_MESSAGE_SIZE,NettyBaseConfig.DEFAUL_THREAD_MAX_MESSAGE_SIZE,
                60, TimeUnit.MINUTES,new ArrayBlockingQueue<Runnable>(500),new NameThreadFactory("message-handle-")));
        this.nettyServerConfig = config;

        this.channelHandlers = handlers;

    }

    public AbstractServerBootStrap(NettyBaseConfig config, ThreadPoolExecutor pools){
        super(pools);
        this.nettyServerConfig = config;


    }

    public void start(int listenPort){

        // 连接处理group
        EventLoopGroup boss = new NioEventLoopGroup(this.connections);
        // 事件处理group
        EventLoopGroup worker = new NioEventLoopGroup(100);
        serverbootstrap = new ServerBootstrap();
        serverbootstrap.group(boss,worker);
        serverbootstrap.channel(NioServerSocketChannel.class);
        serverbootstrap.option(ChannelOption.SO_BACKLOG, 1024 * 1024);
        serverbootstrap.childOption(ChannelOption.SO_KEEPALIVE, true)
                .childOption(ChannelOption.TCP_NODELAY, true)
                .childOption(ChannelOption.SO_SNDBUF, nettyServerConfig.getServerSocketSendBufSize())
                .childOption(ChannelOption.SO_RCVBUF, nettyServerConfig.getServerSocketResvBufSize())
                .childOption(ChannelOption.WRITE_BUFFER_WATER_MARK,
                        new WriteBufferWaterMark(nettyServerConfig.getWriteBufferLowWaterMark(),
                                nettyServerConfig.getWriteBufferHighWaterMark()))
                 .localAddress(new InetSocketAddress(listenPort))
                 .childHandler(new ChannelInitializer<SocketChannel>() {

            @Override
            protected void initChannel(SocketChannel sc) throws Exception {
                //ChannelPipeline p = sc.pipeline();
                sc.pipeline().addLast(new IdleStateHandler(nettyServerConfig.getChannelMaxReadIdleSeconds(), 1, 1))
                        .addLast(new  MessageDecoder())
                        .addLast(new  MessageEncoder());
                if (null != channelHandlers) {
                    addChannelPipelineLast(sc, channelHandlers);
                }
                else {
                    addChannelPipelineLast(sc, AbstractServerBootStrap.this);
                }


            }

        });
        if (nettyServerConfig.isEnableServerPooledByteBufAllocator()) {
            this.serverbootstrap.childOption(ChannelOption.ALLOCATOR, NettyBaseConfig.DIRECT_BYTE_BUF_ALLOCATOR);
        }

        try {
            ChannelFuture future = this.serverbootstrap.bind(listenPort).sync();
            LOGGER.info("Server started ... ");
            initialized.set(true);
            future.channel().closeFuture().sync();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    protected void addChannelPipelineLast(Channel channel, ChannelHandler... handlers) {
        if (null != channel && null != handlers) {
            channel.pipeline().addLast(handlers);
        }
    }




}
