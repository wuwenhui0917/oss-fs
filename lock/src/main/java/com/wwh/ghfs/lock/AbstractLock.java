package com.wwh.ghfs.lock;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.LockSupport;

/**
 * Copyright asiainfo.com
 * 基于AQS实现锁机制
 * @author wuwh6
 */
public abstract class AbstractLock implements DistributeLock {

    /**当前锁上的线程队列*/
    protected FairThreadManager works = new FairThreadManager();
    /**
     * 加锁
     * @param leaseTime
     * @param unit
     * @param interruptibly
     * @throws InterruptedException
     */
    protected void lock(long leaseTime, TimeUnit unit, boolean interruptibly) throws InterruptedException {
        String threadId = String.valueOf(Thread.currentThread().getId());
        Thread work = Thread.currentThread();
        works.addWork(work);
        try {
            Long result = tryAcquire(leaseTime, unit, threadId);
            /**自旋中*/
            while (true) {
                //获取锁 直接退出
                if (result == -1) {
                    return;
                }

                if(result>0){
                    LockSupport.parkNanos(result);
                }
//                else if(interruptibly){
//                    LockSupport.park();
//                }
                else {

                    LockSupport.park();
                }
                if(Thread.interrupted()){
                    Thread.interrupted();
                    throw new InterruptedException();
                }

//                //进行自旋等待
//                if (result >= 0) {
//                    ThreadContextManager.getLatch().tryAcquire(result, TimeUnit.MILLISECONDS);
//                } else if (interruptibly) {
//                    ThreadContextManager.getLatch().acquire();
//
//                } else {
//                ThreadContextManager.getLatch().acquireUninterruptibly();
//                }
                result = tryAcquire(leaseTime, unit, threadId);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            works.removeWork(work);
        }

    }



      protected  abstract long tryAcquire(long time, TimeUnit unit, String threadId);


    @Override
    public void lock(long lockTime) throws InterruptedException {
        try {
            lock(lockTime,TimeUnit.MILLISECONDS,true);
        } catch (InterruptedException e) {

        }
    }

    public Thread getActiveThread(){
       return  Thread.currentThread();
    }



    @Override
    public void unlock() {

        this.works.removeWork(getActiveThread());
        Thread nextThread = this.works.poll();
        LockSupport.unpark(nextThread);
    }


}
