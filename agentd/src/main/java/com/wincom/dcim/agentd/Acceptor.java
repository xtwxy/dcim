/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wincom.dcim.agentd;

import io.netty.channel.Channel;

/**
 *
 * @author master
 */
public interface Acceptor {
    public void onAccept(Channel ch);
}
