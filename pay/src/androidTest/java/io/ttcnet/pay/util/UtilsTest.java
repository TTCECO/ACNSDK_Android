package io.ttcnet.pay.util;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.util.Map;

/**
 * Created by lwq on 2018/12/6.
 */
@RunWith(RobolectricTestRunner.class)
public class UtilsTest {

    @Test
    public void getField(){
        Person person = new Person();
        Map<String, Object> fileds = Util.INSTANCE.getRequestFileds(person);
        Assert.assertEquals(5, 6);
    }


    class Person {
        String name = "ab";
        int age = 99;
    }
}
