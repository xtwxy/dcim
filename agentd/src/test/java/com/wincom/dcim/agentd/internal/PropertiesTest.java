package com.wincom.dcim.agentd.internal;

import java.util.Properties;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import org.junit.Test;

/**
 *
 * @author master
 */
public class PropertiesTest {

    @Test
    public void testHashCodeAndEquals1() {
        Properties expected = new Properties();
        Properties actual = new Properties();
        expected.put("key1", "value1");
        expected.put("key2", "value2");
        actual.put("key2", "value2");
        actual.put("key1", "value1");
        assertEquals(expected, actual);
        assertEquals(expected.hashCode(), actual.hashCode());
    }

    @Test
    public void testHashCodeAndEquals2() {
        Properties expected = new Properties();
        Properties actual = new Properties();
        expected.put("key1", "");
        expected.put("key2", "value2");
        actual.put("key2", "");
        actual.put("key1", "value1");

        assertThat(expected, not(equalTo(actual)));
        assertThat(expected.hashCode(), not(equalTo(actual.hashCode())));
    }
}
