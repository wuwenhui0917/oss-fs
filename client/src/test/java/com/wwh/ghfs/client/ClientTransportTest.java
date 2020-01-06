package com.wwh.ghfs.client;

import com.wwh.ghfs.configure.NettyClientConfig;
import io.netty.channel.Channel;

import org.junit.Test;


import java.net.InetSocketAddress;
import java.util.Scanner;


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
        System.out.println("......................"+transport.sendAsyncRequest(chanel,"hello",500));
        int i=0;
        while (true){
            String line="发送第"+i++;
            System.out.println("发送："+line);
            System.out.println("返回："+transport.sendAsyncRequest(chanel,line,1000));


        }


    }



}
