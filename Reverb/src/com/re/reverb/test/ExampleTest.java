package com.re.reverb.test;

import android.test.InstrumentationTestCase;

/**
 * Created by Bill on 2014-09-13.
 */
public class ExampleTest extends InstrumentationTestCase {
    public void test() throws Exception {
        final int expected = 1;
        final int reality = 5;
        assertEquals(expected, reality);
    }
}
