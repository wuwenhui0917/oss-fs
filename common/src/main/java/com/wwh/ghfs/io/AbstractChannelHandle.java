package com.wwh.ghfs.io;

import com.wwh.ghfs.core.protocol.HeartbeatMessage;
import com.wwh.ghfs.core.protocol.ProtocolConstans;
import com.wwh.ghfs.core.protocol.RequestPackage;
import com.wwh.ghfs.core.protocol.ResultFuture;
import com.wwh.ghfs.core.util.PositiveAtomicCounter;
import io.netty.channel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.*;

/**
 * 通讯处理基类
 * @author wuwh6
 */
public abstract  class AbstractChannelHandle  extends ChannelDuplexHandler {

    protected Logger logger = LoggerFactory.getLogger(this.getClass());
    /**
     * The Message executor.
     */
    protected final ThreadPoolExecutor messageExecutor;

    protected final PositiveAtomicCounter idGenerator = new PositiveAtomicCounter();

    /**
     * The 返回消息缓存.
     */
    protected final ConcurrentHashMap<Integer, ResultFuture> mq = new ConcurrentHashMap<>();

    public AbstractChannelHandle(ThreadPoolExecutor executor){
        this.messageExecutor = executor;
    }

    public abstract void dispatch(RequestPackage msg,ChannelHandlerContext context);


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if(msg==null) return;
        if(msg instanceof RequestPackage){
            RequestPackage reqmsg =(RequestPackage) msg;
            if(reqmsg.getMessageType()==ProtocolConstans.MSGTYPE_RESQUEST){
                try{

//                    dispatch(reqmsg,ctx);
                    messageExecutor.execute(new Runnable() {
                        @Override
                        public void run() {
                            try{
                                dispatch(reqmsg,ctx);
                            }catch (Throwable th){
                                logger.error(th.getMessage());
                            }
                        }
                    });
                }catch (RejectedExecutionException e){
                    logger.error("thread pool is full, current max pool size is"+messageExecutor.getActiveCount());
                }

            }
            //发送数据后返回的报文时
            else if(reqmsg.getMessageType()==ProtocolConstans.MSGTYPE_RESPONSE){
                ResultFuture fultter = mq.remove(reqmsg.getId());
                fultter.setResultMessage(reqmsg.getBody());
                //后续处理
                messageExecutor.execute(new Runnable() {
                    @Override
                    public void run() {
                        try{
                            dispatch(reqmsg,ctx);
                        }catch (Throwable th){
                            logger.error(th.getMessage());
                        }
                    }
                });
            }

        }
    }

    @Override
    public void close(ChannelHandlerContext ctx, ChannelPromise promise) throws Exception {
        logger.info("{} is closed",ctx);
        super.close(ctx, promise);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        logger.error(
                ctx.channel() + " connect exception. " + cause.getMessage(),
                cause);
        try{
            destroyChannel(ctx.channel());
        }catch (Exception e){
            logger.error("failed to close channel {}: {}", ctx.channel(), e.getMessage(), e);

        }
    }

    protected Object sendAsyncRequestWithoutResponse(Channel channel, Object msg) throws
            TimeoutException {
        return sendAsyncRequest( channel, msg, 0);
    }
    public int getNextMessageId() {
        return idGenerator.incrementAndGet();
    }

    /**
     * 发送报文
     * @param channel
     * @param msg
     */
    protected void sendRequest(Channel channel, Object msg) {
        RequestPackage rpcMessage = new RequestPackage();
        rpcMessage.setMessageType(msg instanceof HeartbeatMessage ?
                ProtocolConstans.MSGTYPE_HEARTBEAT_REQUEST
                : ProtocolConstans.MSGTYPE_RESQUEST);
        rpcMessage.setCodec(ProtocolConstans.CODE_TYPE_HESSION);
        rpcMessage.setCompressor(ProtocolConstans.COMPREE_TYPE_GZIP);
        rpcMessage.setBody(msg);
        rpcMessage.setId(getNextMessageId());
        channel.writeAndFlush(rpcMessage);
    }

    /**
     * 同步发送返回值
     * @param channel
     * @param msg
     * @param timeout
     * @return
     * @throws TimeoutException
     */
    public Object sendAsyncRequest(Channel channel, Object msg, long timeout)
            throws TimeoutException {
        if (channel == null) {
            logger.warn("sendAsyncRequestWithResponse nothing, caused by null channel.");
            return false;
        }
        final RequestPackage rpcMessage = new RequestPackage();
        rpcMessage.setId(getNextMessageId());
        rpcMessage.setMessageType(ProtocolConstans.MSGTYPE_RESQUEST);
        rpcMessage.setCodec(ProtocolConstans.CODE_TYPE_HESSION);
        rpcMessage.setCompressor(ProtocolConstans.COMPREE_TYPE_GZIP);
        rpcMessage.setBody(msg);
        ResultFuture refuture = new ResultFuture();
        refuture.setRequestMessage(rpcMessage);
        refuture.setTimeout(timeout);
        //放入队列中
        mq.put(rpcMessage.getId(),refuture);
        CountDownLatch latch = new CountDownLatch(1);
        if(channel.isWritable()){
            ChannelFuture future = channel.writeAndFlush(rpcMessage);
            future.addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture future) throws Exception {
                    try{
                        if (!future.isSuccess()) {
                            logger.error(">>>>>>>>>>>>>>>>");
                            refuture.setResultMessage(future.cause());
                            destroyChannel(future.channel());
                        }

                    }finally {
                        latch.countDown();
                    }
                }
            });

        }
        try {
            latch.await(timeout,TimeUnit.SECONDS);
            return refuture.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }



    public abstract void destroyChannel( Channel channel);
}
