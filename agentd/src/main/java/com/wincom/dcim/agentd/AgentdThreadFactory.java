/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wincom.dcim.agentd;

import java.util.concurrent.ThreadFactory;

/**
 *
 * @author master
 */
public interface AgentdThreadFactory {
     public ThreadFactory getThreadFactory();
}
