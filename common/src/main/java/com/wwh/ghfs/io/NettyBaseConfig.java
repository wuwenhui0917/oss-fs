package com.wwh.ghfs.io;

import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.Channel;
import io.netty.channel.ServerChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.NettyRuntime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NettyBaseConfig {
    private static final Logger LOGGER = LoggerFactory.getLogger(NettyBaseConfig.class);
    private int serverSocketSendBufSize = 153600;
    private int serverSocketResvBufSize = 153600;
    private int writeBufferHighWaterMark = 67108864;
    private int writeBufferLowWaterMark = 1048576;
    /**
     是否使用直接内存区（本地内存）
     */
    private boolean enableServerPooledByteBufAllocator =false;

    protected static int WORKER_THREAD_SIZE=100;

    /**
     * The constant SERVER_CHANNEL_CLAZZ.
     */
    protected static final Class<? extends ServerChannel> SERVER_CHANNEL_CLAZZ = NioServerSocketChannel.class;
    /**
     * The constant CLIENT_CHANNEL_CLAZZ.
     */
    protected static final Class<? extends Channel> CLIENT_CHANNEL_CLAZZ=NioSocketChannel.class;


    private static final int DEFAULT_WRITE_IDLE_SECONDS = 5;

    private static final int READIDLE_BASE_WRITEIDLE = 3;

    protected static final int DEFAUL_THREAD_CORE_MESSAGE_SIZE = Runtime.getRuntime().availableProcessors();
    protected static final int DEFAUL_THREAD_MAX_MESSAGE_SIZE = 1000;



    static {
     
    }

    public int getWriteBufferLowWaterMark() {
        return writeBufferLowWaterMark;
    }

    public int getWriteBufferHighWaterMark() {
        return writeBufferHighWaterMark;
    }

    public int getChannelMaxReadIdleSeconds() {
        return 15;
    }

    public boolean isEnableServerPooledByteBufAllocator() {
        return enableServerPooledByteBufAllocator;
    }


    /**
     * The enum Work thread mode.
     */
    enum WorkThreadMode {

        /**
         * Auto work thread mode.
         */
        Auto(NettyRuntime.availableProcessors() * 2 + 1),
        /**
         * Pin work thread mode.
         */
        Pin(NettyRuntime.availableProcessors()),
        /**
         * Busy pin work thread mode.
         */
        BusyPin(NettyRuntime.availableProcessors() + 1),
        /**
         * Default work thread mode.
         */
        Default(NettyRuntime.availableProcessors() * 2);

        /**
         * Gets value.
         *
         * @return the value
         */
        public int getValue() {
            return value;
        }

        private int value;

        WorkThreadMode(int value) {
            this.value = value;
        }

        /**
         * Gets mode by name.
         *
         * @param name the name
         * @return the mode by name
         */
        public static WorkThreadMode getModeByName(String name) {
            if (Auto.name().equalsIgnoreCase(name)) {
                return Auto;
            } else if (Pin.name().equalsIgnoreCase(name)) {
                return Pin;
            } else if (BusyPin.name().equalsIgnoreCase(name)) {
                return BusyPin;
            } else if (Default.name().equalsIgnoreCase(name)) {
                return Default;
            } else {
                return null;
            }
        }

    }

    int getServerSocketSendBufSize(){
        return this.serverSocketSendBufSize;
    }

    int getServerSocketResvBufSize(){
        return this.serverSocketResvBufSize;
    }

    public static final PooledByteBufAllocator DIRECT_BYTE_BUF_ALLOCATOR =
            new PooledByteBufAllocator(
                    true,
                    WORKER_THREAD_SIZE,
                    WORKER_THREAD_SIZE,
                    2048 * 64,
                    10,
                    512,
                    256,
                    64,
                    true,
                    0
            );
}
