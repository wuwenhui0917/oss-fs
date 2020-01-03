package com.wwh.ghfs.io;

import com.wwh.ghfs.core.protocol.HeartbeatMessage;
import com.wwh.ghfs.core.protocol.RequestPackage;
import com.wwh.ghfs.core.util.GzipUtil;
import com.wwh.ghfs.core.util.HessionUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import com.wwh.ghfs.core.protocol.ProtocolConstans;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 最大64M的数据量 协议为2B魔术字，1b版本号，4B长度
 * @author wuwh6
 */
public class MessageDecoder extends LengthFieldBasedFrameDecoder {

    private static final Logger LOGGER = LoggerFactory.getLogger(MessageEncoder.class);
    public MessageDecoder(){
        this(ProtocolConstans.MAX_FRAME_LENGTH);
    }
    public MessageDecoder(int maxFrameLength) {
        super(maxFrameLength, 3, 4, -7, 0);

    }
    @Override
    protected Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
        Object decoded = super.decode(ctx, in);
        if (decoded instanceof ByteBuf) {
            ByteBuf frame = (ByteBuf) decoded;
            try {
                return decodeFrame(frame);
            } catch (Exception e) {
                LOGGER.error("Decode frame error!", e);
                throw e;
            } finally {
                frame.release();
            }
        }
        return decoded;
    }

    public Object decodeFrame(ByteBuf frame) {
        byte b0 = frame.readByte();
        byte b1 = frame.readByte();
        if (ProtocolConstans.MAGIC_CODE_BYTES[0] != b0
                || ProtocolConstans.MAGIC_CODE_BYTES[1] != b1) {
            throw new IllegalArgumentException("Unknown magic code: " + b0 + ", " + b1);
        }

        byte version = frame.readByte();
        if (ProtocolConstans.VERSION != version) {
            throw new IllegalArgumentException("Unknown version : " + version);
        }
        //报文长度
        int fullLength = frame.readInt();
        byte messageType = frame.readByte();
        byte codecType = frame.readByte();
        byte compressorType = frame.readByte();
        int requestId = frame.readInt();
        RequestPackage rpcMessage = new RequestPackage();
        rpcMessage.setCodec(codecType);
        rpcMessage.setId(requestId);
        rpcMessage.setCompressor(compressorType);
        rpcMessage.setMessageType(messageType);
        // 如果是心跳
        if (messageType == ProtocolConstans.MSGTYPE_HEARTBEAT_REQUEST) {
            rpcMessage.setBody(HeartbeatMessage.PING);
        } else if (messageType == ProtocolConstans.MSGTYPE_HEARTBEAT_RESPONSE) {
            rpcMessage.setBody(HeartbeatMessage.PONG);
        } else {
            int bodyLength = fullLength - ProtocolConstans.V1_HEAD_LENGTH;
            if (bodyLength > 0) {
                byte[] bs = new byte[bodyLength];
                frame.readBytes(bs);
                if(compressorType==ProtocolConstans.COMPREE_TYPE_GZIP){
                    bs = GzipUtil.decompress(bs);
                }
                rpcMessage.setBody(HessionUtil.decode(bs));
            }
        }
        return rpcMessage;
    }
}
