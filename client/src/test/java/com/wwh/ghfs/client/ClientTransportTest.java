package com.wwh.ghfs.client;

import com.wwh.ghfs.configure.NettyClientConfig;
import io.netty.channel.Channel;

import org.junit.Test;


import java.net.InetSocketAddress;


/**
 * Copyright asiainfo.com
 *
 * @author wuwh6
 */

public class ClientTransportTest {
    ClientTransport transport = new ClientTransport(new NettyClientConfig());
    @Test
    public void dispatch() throws Exception {
        transport.start();
        Channel chanel = transport.getNewChannel(new InetSocketAddress("127.0.0.1",9999));
        System.out.println(transport.sendAsyncRequest(chanel,"hello",20000));
        while (true);


    }



}
