package com.wwh.ghfs.io;

import com.wwh.ghfs.core.protocol.ProtocolConstans;
import com.wwh.ghfs.core.protocol.RequestPackage;
import com.wwh.ghfs.core.util.GzipUtil;
import com.wwh.ghfs.core.util.HessionUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 编码处理
 * @author wuwh6
 */
public class MessageEncoder extends MessageToByteEncoder {
    private static final Logger LOGGER = LoggerFactory.getLogger(MessageEncoder.class);

    @Override
    protected void encode(ChannelHandlerContext ctx, Object msg, ByteBuf out) throws Exception {
        try {
            if (msg instanceof RequestPackage) {
                int fullLength = ProtocolConstans.V1_HEAD_LENGTH;
                RequestPackage rpcMessage = (RequestPackage) msg;
                byte messageType = rpcMessage.getMessageType();
                out.writeBytes(ProtocolConstans.MAGIC_CODE_BYTES);
                out.writeByte(ProtocolConstans.VERSION);
                out.writerIndex(out.writerIndex() + 4);
                out.writeByte(messageType);
                out.writeByte(rpcMessage.getCodec());
                out.writeByte(rpcMessage.getCompressor());
                out.writeInt(rpcMessage.getId());
                byte[] bodyBytes = null;
                //// heartbeat has no body
                if (messageType != ProtocolConstans.MSGTYPE_HEARTBEAT_REQUEST
                        && messageType != ProtocolConstans.MSGTYPE_HEARTBEAT_RESPONSE) {
                    bodyBytes = HessionUtil.encode(rpcMessage.getBody());
                    if (rpcMessage.getCompressor() == ProtocolConstans.COMPREE_TYPE_GZIP) {
                        bodyBytes = GzipUtil.compress(bodyBytes);
                    }
                }
                //写body
                if (bodyBytes != null) {
                    out.writeBytes(bodyBytes);
                    fullLength += bodyBytes.length;
                }
                int writeIndex = out.writerIndex();
                //回到index
                out.writerIndex(writeIndex - fullLength + 3);
                //写长度
                out.writeInt(fullLength);
                //回到当前
                out.writerIndex(writeIndex);
            } else {
                throw new UnsupportedOperationException("Not support this class:" + msg.getClass());
            }
        } catch (Exception e) {

        }

    }
}
