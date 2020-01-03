package com.wwh.ghfs.core.protocol;

import java.io.Serializable;

public class HeartbeatMessage implements Serializable {
    private static final long serialVersionUID = -985316399527884899L;
    private boolean ping = true;
    /**
     * The constant PING.
     */
    public static final HeartbeatMessage PING = new HeartbeatMessage(true);
    /**
     * The constant PONG.
     */
    public static final HeartbeatMessage PONG = new HeartbeatMessage(false);

    private HeartbeatMessage(boolean ping) {
        this.ping = ping;
    }

    @Override
    public String toString() {
        return this.ping ? "services ping" : "services pong";
    }

    public boolean isPing() {
        return ping;
    }

    public void setPing(boolean ping) {
        this.ping = ping;
    }
}