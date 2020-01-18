package com.wwh.ghfs.lock;

import java.util.concurrent.locks.Lock;

/**
 * Copyright asiainfo.com
 *
 * @author wuwh6
 */
public class LockFatory {

    public static Lock getRedisLock(){
         return null;
    }

    public static Lock getLocalLock(){
        return null;
    }
}
