package com.wincom.dcim.agentd.messages;

import com.wincom.dcim.agentd.ChannelOutboundHandler;
import com.wincom.dcim.agentd.HandlerContext;

/**
 * Created by master on 6/23/17.
 */
public class PasswordAuth extends ChannelOutbound {

    private final String username;
    private final String password;

    public PasswordAuth(HandlerContext c, String user, String pass) {
        super(c);
        this.username = user;
        this.password = pass;
    }

    @Override
    public void applyChannelOutbound(HandlerContext ctx, ChannelOutboundHandler handler) {
        handler.handleAuth(ctx, this);
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public String toString() {
        return String.format("%s to %s:%d", getClass().getSimpleName(), username, password);
    }
}
