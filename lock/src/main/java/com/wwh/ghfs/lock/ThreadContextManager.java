package com.wwh.ghfs.lock;

import java.util.concurrent.Semaphore;

/**
 * Copyright asiainfo.com
 *
 * @author wuwh6
 */
public class ThreadContextManager {

   private  static ThreadLocal<Semaphore> locals = new ThreadLocal<>();
   public static Semaphore getLatch(){
       Semaphore  semp=  locals.get();
       if(semp==null){
           semp = new Semaphore(0);
           locals.set(semp);
       }
       return semp;
   }

   public static void clear(){
       locals.remove();
   }


}
