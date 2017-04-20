package com.wincom.dcim.agentd;

import java.util.Properties;

/**
 * Factories to creating <code>Codec</code>s.
 * @author master
 */
public interface CodecFactory {
    public Codec create(Properties props);
}
