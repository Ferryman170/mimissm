package com.my.test;

import com.my.utils.MD5Util;
import org.junit.Test;

public class MD5Test {
    @Test
    public void Test(){
        String mi = MD5Util.getMD5("000000");
        System.out.println(mi);
    }
}
