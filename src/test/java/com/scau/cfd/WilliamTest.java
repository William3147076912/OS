package com.scau.cfd;

import org.junit.Test;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

/**
 * 这个类是：
 * @author: William
 * @date: 2024-10-30T18:58:12CST 18:58
 * @description:
 */
public class WilliamTest {
    @Test
    public void test() throws UnsupportedEncodingException {
        for (byte aByte : "asd".getBytes(StandardCharsets.UTF_8)) {
            System.out.println(aByte);
        }
        System.out.println(Arrays.toString("asd".getBytes(StandardCharsets.UTF_8)));
    }
}
