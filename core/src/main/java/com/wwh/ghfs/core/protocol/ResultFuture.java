package com.wwh.ghfs.core.protocol;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Copyright asiainfo.com
 *
 * @author wuwh6
 */
public class ResultFuture {
    private RequestPackage requestMessage;
    private long timeout;
    private long start = System.currentTimeMillis();
//    private Object resultMessage;
    private transient CompletableFuture<Object> origin = new CompletableFuture<>();


    /**
     * Is timeout boolean.
     *
     * @return the boolean
     */
    public boolean isTimeout() {
        return System.currentTimeMillis() - start > timeout;
    }

    /**
     * Get object.
     *
     * @return the object
     * @throws TimeoutException the timeout exception
     * @throws InterruptedException the interrupted exception
     */
    public Object get(long timeout, TimeUnit unit) throws TimeoutException,
            InterruptedException {

        Object result = null;
        try {
            result = origin.get(timeout, unit);
        } catch (ExecutionException e) {
            throw new RuntimeException ("Should not get results in a multi-threaded environment", e);
        } catch (TimeoutException e) {
            throw new TimeoutException("cost " + (System.currentTimeMillis() - start) + " ms");
        }

        if (result instanceof RuntimeException) {
            throw (RuntimeException)result;
        } else if (result instanceof Throwable) {
            throw new RuntimeException((Throwable)result);
        }

        return result;
    }

    /**
     * Sets result message.
     *
     * @param obj the obj
     */
    public void setResultMessage(Object obj) {
        origin.complete(obj);
    }

    /**
     * Gets request message.
     *
     * @return the request message
     */
    public RequestPackage getRequestMessage() {
        return requestMessage;
    }

    /**
     * Sets request message.
     *
     * @param requestMessage the request message
     */
    public void setRequestMessage(RequestPackage requestMessage) {
        this.requestMessage = requestMessage;
    }

    /**
     * Gets timeout.
     *
     * @return the timeout
     */
    public long getTimeout() {
        return timeout;
    }

    /**
     * Sets timeout.
     *
     * @param timeout the timeout
     */
    public void setTimeout(long timeout) {
        this.timeout = timeout;
    }

}
