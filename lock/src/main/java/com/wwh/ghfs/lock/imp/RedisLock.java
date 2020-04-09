package com.wwh.ghfs.lock.imp;
import com.wwh.ghfs.lock.AbstractLock;
import redis.clients.jedis.JedisCluster;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;

/**
 * Copyright asiainfo.com
 *
 * @author wuwh6
 */
public class RedisLock extends AbstractLock {

    private String name;
    private String id = "lock-";
    /**
     * 创建锁的lua脚本
     */
    private String lockLua = "if (redis.call('exists', KEYS[1]) == 0) then " +
            "                   redis.call('hset', KEYS[1], ARGV[2], 1); " +
            "                   redis.call('pexpire', KEYS[1], ARGV[1]); " +
            "                   return -1; end; " +
            "               if (redis.call('hexists', KEYS[1], ARGV[2]) == 1) then " +
            "                   redis.call('hincrby', KEYS[1], ARGV[2], 1); " +
            "                   redis.call('pexpire', KEYS[1], ARGV[1]); " +
            "                   return -1; end; " +
            "               return redis.call('pttl', KEYS[1]);";

    /**
     * 释放锁
     */
    private String unlockLua = "if (redis.call('hexists', KEYS[1], ARGV[2]) == 0) then " +
            "                    return 0; " +
            "                 end; " +
            "                 local counter = redis.call('hincrby', KEYS[1], ARGV[2], -1);" +
            "                 if (counter > 0) then " +
            "                     redis.call('pexpire', KEYS[1], ARGV[1]);" +
            "                     return 0; " +
            "                 else   redis.call('del', KEYS[1]);  return 1;  " +
            "                 end;   return -1     ";


    private JedisCluster jedis;

    public RedisLock(String name) {
        this.name = name;
        this.id = id + UUID.randomUUID();
    }

    @Override
    public void lock() {
        try {
            this.lock(10000,  TimeUnit.MILLISECONDS, false);
        } catch (InterruptedException e) {
            throw new RuntimeException("lock error");
        }
    }



    @Override
    public void lockInterruptibly() throws InterruptedException {
        if(Thread.interrupted()){
            Thread.interrupted();
            throw new InterruptedException();
        }
        this.lock(-1L, TimeUnit.MILLISECONDS, true);
    }

    @Override
    public boolean tryLock() {
        long result = tryAcquire(-1, null, String.valueOf(Thread.currentThread().getId()));
        return result == -1;
    }

    @Override
    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        long result = tryAcquire(time, unit, String.valueOf(Thread.currentThread().getId()));
        return result == -1;
    }

    /**
     * 执行处理操作
     *
     * @param time
     * @param unit
     * @return
     */
    @Override
    protected long tryAcquire(long time, TimeUnit unit, String threadId) {
        long internalLockLeaseTime = unit.toMillis(time);
        String lockThreadId = this.id + threadId;
        List<String> params = new ArrayList<>();
        params.add(String.valueOf(internalLockLeaseTime));
        params.add(lockThreadId);
        if (jedis != null) {
            return (Long) jedis.eval(lockLua, Collections.singletonList(this.name), params);
        }
        throw new RuntimeException(" 加锁失败：[redis链接为空]");
    }

    @Override
    public void unlock() {
        List<String> params = new ArrayList<>();
        String threadId = String.valueOf(Thread.currentThread().getId());
        params.add("-1");
        String lockThreadId = this.id + threadId;
        params.add(lockThreadId);
        if (jedis != null) {
            Long result =  (Long) jedis.eval(lockLua, Collections.singletonList(this.name), params);
            if(result==-1){
                new RuntimeException("释放锁失败");
            }
            super.unlock();
        }
        new RuntimeException("释放锁失败；[jedis connection is null]");

    }

    @Override
    public Condition newCondition() {
        return null;
    }


    public JedisCluster getJedis() {
        return jedis;
    }

    public void setJedis(JedisCluster jedis) {
        this.jedis = jedis;
    }
}
