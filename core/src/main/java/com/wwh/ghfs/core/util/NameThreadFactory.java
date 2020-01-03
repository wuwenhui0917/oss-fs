package com.wwh.ghfs.core.util;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Copyright asiainfo.com
 *
 * @author wuwh6
 */
public class NameThreadFactory implements ThreadFactory {


    private String name;
    private final static Map<String, AtomicInteger> PREFIX_COUNTER = new ConcurrentHashMap<>();
    public NameThreadFactory(String name){
        this.name = name;
        PREFIX_COUNTER.putIfAbsent(name, new AtomicInteger(0));
    }
    @Override
    public Thread newThread(Runnable r) {
        String tname = name+PREFIX_COUNTER.get(name).incrementAndGet();
        Thread thread = new Thread(r,tname);
        return thread;
    }




}
