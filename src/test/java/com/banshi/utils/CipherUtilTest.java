package com.banshi.utils;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CipherUtilTest {

    @Test
    public void testBASE64() throws Exception {
        String inputStr = "简单加密";
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
        String inputStr = MyString.getUUID();
        String output1 = CipherUtil.encryptMD5(CipherUtil.MD5, inputStr.getBytes(Constants.CHARSET_UTF_8));
        String output2 = CipherUtil.encryptMD5(CipherUtil.MD5, inputStr.getBytes(Constants.CHARSET_UTF_8));

        System.out.println("output1:\n" + output1);
        System.out.println("output2:\n" + output2);

        assertEquals(output1, output2);
    }

    @Test
    public void testEncryptSHA() throws Exception {
        String inputStr = MyString.getUUID();
        String output1 = CipherUtil.encryptSHA(CipherUtil.SHA, inputStr.getBytes(Constants.CHARSET_UTF_8));
        String output2 = CipherUtil.encryptSHA(CipherUtil.SHA, inputStr.getBytes(Constants.CHARSET_UTF_8));

        System.out.println("output1:\n" + output1);
        System.out.println("output2:\n" + output2);

        assertEquals(output1, output2);
    }

    @Test
    public void testEncryptHMAC() throws Exception {
        String inputStr = MyString.getUUID();
        String output1 = CipherUtil.encryptHMAC(CipherUtil.MAC_SHA, CipherUtil.MAC_SALT.getBytes(Constants.CHARSET_UTF_8), inputStr.getBytes(Constants.CHARSET_UTF_8));
        String output2 = CipherUtil.encryptHMAC(CipherUtil.MAC_SHA, CipherUtil.MAC_SALT.getBytes(Constants.CHARSET_UTF_8), inputStr.getBytes(Constants.CHARSET_UTF_8));

        System.out.println("output1:\n" + output1);
        System.out.println("output2:\n" + output2);

        assertEquals(output1, output2);
    }


    @Test
    public void testPwd() {
        String encrypt_pwd = CipherUtil.generatePassword("123456");
        assertEquals("E10ADC3949BA59ABBE56E057F20F883E", encrypt_pwd);

        Assert.assertTrue(CipherUtil.validatePassword(encrypt_pwd, "123456"));
    }

}
