/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wincom.dcim.agentd;

import io.netty.channel.EventLoopGroup;
import java.util.concurrent.ThreadFactory;

/**
 *
 * @author master
 */
public interface AgentdThreadFactory {
     public ThreadFactory getThreadFactory();
     public EventLoopGroup getEventLoopGroup();
}
