package com.banshi.utils;

import org.junit.Assert;
import org.junit.Test;

import java.math.BigInteger;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class CipherUtilTest {

    private final String inputStr = "简单加密";

    @Test
    public void testBASE64() throws Exception {
        System.out.println("原文:\n" + inputStr);

        String encryptStr = CipherUtil.encryptBASE64(inputStr.getBytes(Constants.CHARSET_UTF_8));

        System.out.println("BASE64加密后:\n" + encryptStr);

        byte[] output = CipherUtil.decryptBASE64(encryptStr);

        String outputStr = new String(output);

        System.out.println("BASE64解密后:\n" + outputStr);

        // 验证BASE64加密解密一致性
        assertEquals(inputStr, outputStr);
    }

    @Test
    public void testEncryptMD5() throws Exception {
        byte[] output1 = CipherUtil.encryptMD5(CipherUtil.MD5, inputStr.getBytes(Constants.CHARSET_UTF_8));
        byte[] output2 = CipherUtil.encryptMD5(CipherUtil.MD5, inputStr.getBytes(Constants.CHARSET_UTF_8));

        String hashCode1 = (new BigInteger(output1)).toString(16);
        String hashCode2 = (new BigInteger(output2)).toString(16);
        System.out.println("hashCode1:\n" + hashCode1);
        System.out.println("hashCode2:\n" + hashCode2);

        assertArrayEquals(output1,output2);
        assertEquals(hashCode1, hashCode2);
    }

    @Test
    public void testEncryptSHA() throws Exception {
        byte[] output1 = CipherUtil.encryptSHA(CipherUtil.SHA, inputStr.getBytes(Constants.CHARSET_UTF_8));
        byte[] output2 = CipherUtil.encryptSHA(CipherUtil.SHA, inputStr.getBytes(Constants.CHARSET_UTF_8));

        String hashCode1 = (new BigInteger(output1)).toString(16);
        String hashCode2 = (new BigInteger(output2)).toString(16);
        System.out.println("hashCode1:\n" + hashCode1);
        System.out.println("hashCode2:\n" + hashCode2);

        assertArrayEquals(output1,output2);
        assertEquals(hashCode1, hashCode2);
    }

    @Test
    public void testEncryptHMAC() throws Exception {
        String salt = "sdecfr#-123";
        byte[] output1 = CipherUtil.encryptHMAC(CipherUtil.MAC_SHA, salt.getBytes(Constants.CHARSET_UTF_8), inputStr.getBytes(Constants.CHARSET_UTF_8));
        byte[] output2 = CipherUtil.encryptHMAC(CipherUtil.MAC_SHA, salt.getBytes(Constants.CHARSET_UTF_8), inputStr.getBytes(Constants.CHARSET_UTF_8));

        String hashCode1 = (new BigInteger(output1)).toString(16);
        String hashCode2 = (new BigInteger(output2)).toString(16);
        System.out.println("hashCode1:\n" + hashCode1);
        System.out.println("hashCode2:\n" + hashCode2);

        assertArrayEquals(output1,output2);
        assertEquals(hashCode1, hashCode2);
    }


    @Test
    public void testPwd(){
        String encrypt_pwd = CipherUtil.generatePassword("123456");
        assertEquals("E10ADC3949BA59ABBE56E057F20F883E", encrypt_pwd);

        Assert.assertTrue(CipherUtil.validatePassword(encrypt_pwd,"123456"));
    }

}
