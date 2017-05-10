package com.wincom.dcim.agentd.internal;

import com.wincom.dcim.agentd.primitives.Handler;
import com.wincom.dcim.agentd.primitives.HandlerContext;
import com.wincom.dcim.agentd.primitives.Message;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 *
 * @author master
 */
public class MessageQueue {
    private final ConcurrentLinkedQueue<Message> queue;
    private HandlerContext context;
    private final Handler handler;
    private boolean inprogress;
    
    public MessageQueue(HandlerContext context, Handler handler) {
        this.queue = new ConcurrentLinkedQueue<>();
        this.context = context;
        this.handler = handler;
    }
    
    public boolean add(Message e) {
        if(inprogress) {
            return queue.add(e);
        } else {
            inprogress = true;
            handler.handle(context, e);
        }
        return true;
    }
    
    public void onCompleteMessage() {
        if(queue.isEmpty()) {
            inprogress = false;
        } else {
            handler.handle(context, queue.poll());
        }
    }
}
