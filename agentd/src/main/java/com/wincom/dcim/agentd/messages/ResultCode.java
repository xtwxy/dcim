package com.wincom.dcim.agentd.messages;

/**
 *
 * @author master
 */
public enum ResultCode {
    SUCCESS(0),
    FAILURE(1);
    
    ResultCode(int code) {
        this.code = code;
    }
    
    private int code;
}
