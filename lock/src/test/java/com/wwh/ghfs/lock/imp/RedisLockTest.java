package com.wwh.ghfs.lock.imp;


import org.junit.Before;
import org.junit.Test;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;

import java.util.Random;
import java.util.concurrent.CountDownLatch;

/**
 * Copyright asiainfo.com
 *
 * @author wuwh6
 */
public class RedisLockTest {

    JedisCluster cluster =null;
    RedisLock lock=null;


    @Before
    public void before(){
        HostAndPort port1 = new HostAndPort("192.168.23.148",10201);
        cluster = new JedisCluster(port1);

        lock = new RedisLock("testLock");
        lock.setJedis(cluster);

    }

    @Test
    public void testLock(){
        try {
            lock.lock(12000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
//        CountDownLatch latch = new CountDownLatch(1);
//        try {
//            latch.await();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }


    }
    @Test
    public void test2Lock(){
        Random random = new Random();
        CountDownLatch latch = new CountDownLatch(2);
        int all = 0;

        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                for(int i=0;i<100;i++){

                    try {
                        lock.lock(100000);
                        System.out.println(Thread.currentThread().getName()+"="+i);
                        Thread.sleep(random.nextInt(1000));

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    lock.unlock();

                }
                latch.countDown();

            }
        });
        Thread t2 = new Thread(new Runnable() {
            @Override
            public void run() {
                for(int i=0;i<100;i++){

                    try {
                        lock.lock(100000);
                        System.out.println(Thread.currentThread().getName()+"="+i);
                        Thread.sleep(random.nextInt(1000));

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    lock.unlock();


                }
                latch.countDown();

            }
        });
        t1.start();
        t2.start();

        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

}