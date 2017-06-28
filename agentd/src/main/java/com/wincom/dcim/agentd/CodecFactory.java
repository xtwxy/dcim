package com.wincom.dcim.agentd;

import java.util.Properties;

/**
 * Factories to creating <code>Codec</code>s.
 * @author master
 */
public interface CodecFactory {
    /**
     * The content of properties:
     * props := {
     *  inbound: {
     *      codecId: 'codecId',
     *      outboundId: 'codecId',
     *  }
     * }
     * @param props
     * @return
     */
    Codec create(Properties props);
}
