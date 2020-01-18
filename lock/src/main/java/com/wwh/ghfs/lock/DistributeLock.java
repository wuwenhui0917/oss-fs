package com.wwh.ghfs.lock;

import java.util.concurrent.locks.Lock;

/**
 * Copyright asiainfo.com
 *
 * @author wuwh6
 */
public interface DistributeLock extends Lock {

    /**
     * 锁定多长时间
     * @param lockTime
     */
     void lock(long lockTime) throws InterruptedException;
}
