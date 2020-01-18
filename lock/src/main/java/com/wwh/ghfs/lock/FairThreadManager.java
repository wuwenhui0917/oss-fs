package com.wwh.ghfs.lock;

import sun.reflect.generics.tree.Tree;

import javax.management.Query;
import java.util.Queue;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Copyright asiainfo.com
 * 实现公平的线程管理工具
 * @author wuwh6
 */
public class FairThreadManager {

    private  Queue<Thread> tree = new ConcurrentLinkedQueue();

    public  void addWork(Thread t){
        tree.add(t);
    }
    public  void removeWork(Thread t){
        tree.remove(t);
    }
    public  Thread poll(){
        return tree.poll();
    }


}
