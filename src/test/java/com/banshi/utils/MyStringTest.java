package com.banshi.utils;


import org.junit.Assert;
import org.junit.Test;

public class MyStringTest {

    @Test
    public void getUUID() {
        String uuid = MyString.getUUID();
        System.out.println("uuid = " + uuid);
        Assert.assertEquals(36, uuid.length());
        Assert.assertTrue(MyString.isUUID(uuid));
        System.out.println("uuid = " + System.currentTimeMillis());
    }

    @Test
    public void isMatch() {

        String regex = "^(?!.*(不合谐)).*$";//用到了前瞻，不含指定字符串
        Assert.assertEquals(false, MyString.isMatch("结尾不合谐", regex));
        Assert.assertEquals(false, MyString.isMatch("中间不合谐呢", regex));
        Assert.assertEquals(false, MyString.isMatch("不合谐开始", regex));
        Assert.assertEquals(true, MyString.isMatch("很合谐哦", regex));

        String regex2 = "^.*(?<!(不合谐))$";//用到了后顾，不能以指定字符串结尾
        Assert.assertEquals(false, MyString.isMatch("结尾不合谐", regex2));
        Assert.assertEquals(true, MyString.isMatch("中间不合谐呢", regex2));
        Assert.assertEquals(true, MyString.isMatch("不合谐开始", regex2));
        Assert.assertEquals(true, MyString.isMatch("很合谐哦", regex2));
    }

    @Test
    public void testName() {
        Assert.assertEquals(false, MyString.isMatch("123456", Constants.REGEX_USER_NAME_FORMAT));
        Assert.assertEquals(false, MyString.isMatch("123aaa", Constants.REGEX_USER_NAME_FORMAT));
        Assert.assertEquals(false, MyString.isMatch("aaa中文", Constants.REGEX_USER_NAME_FORMAT));
        Assert.assertEquals(false, MyString.isMatch("aaa!", Constants.REGEX_USER_NAME_FORMAT));
        Assert.assertEquals(true, MyString.isMatch("aaa111", Constants.REGEX_USER_NAME_FORMAT));
        Assert.assertEquals(true, MyString.isMatch("aaa_111", Constants.REGEX_USER_NAME_FORMAT));
        Assert.assertEquals(true, MyString.isMatch("aaa-111", Constants.REGEX_USER_NAME_FORMAT));
        Assert.assertEquals(true, MyString.isMatch("aaa.111", Constants.REGEX_USER_NAME_FORMAT));
    }

    @Test
    public void testEmail() {
        Assert.assertEquals(false, MyString.isMatch("test.test.com", Constants.REGEX_USER_EMAIL_FORMAT));
        Assert.assertEquals(false, MyString.isMatch("test@testcom", Constants.REGEX_USER_EMAIL_FORMAT));
        Assert.assertEquals(false, MyString.isMatch("test@test@test.com", Constants.REGEX_USER_EMAIL_FORMAT));
        Assert.assertEquals(false, MyString.isMatch("tes+t@test.com", Constants.REGEX_USER_EMAIL_FORMAT));
        Assert.assertEquals(true, MyString.isMatch("test_1@test.com", Constants.REGEX_USER_EMAIL_FORMAT));
        Assert.assertEquals(true, MyString.isMatch("test-1@test-1.com", Constants.REGEX_USER_EMAIL_FORMAT));
        Assert.assertEquals(true, MyString.isMatch("test.1@test-1.com", Constants.REGEX_USER_EMAIL_FORMAT));
        Assert.assertEquals(true, MyString.isMatch("test@test.com", Constants.REGEX_USER_EMAIL_FORMAT));
    }

    @Test
    public void testMobile() {
        Assert.assertEquals(false, MyString.isMatch("12345678", Constants.REGEX_USER_MOBILE_FORMAT));
        Assert.assertEquals(false, MyString.isMatch("aaa12345678", Constants.REGEX_USER_MOBILE_FORMAT));
        Assert.assertEquals(false, MyString.isMatch("12345678aaa", Constants.REGEX_USER_MOBILE_FORMAT));
        Assert.assertEquals(false, MyString.isMatch("02112345678", Constants.REGEX_USER_MOBILE_FORMAT));
        Assert.assertEquals(false, MyString.isMatch("121123456781", Constants.REGEX_USER_MOBILE_FORMAT));
        Assert.assertEquals(true, MyString.isMatch("12345678123", Constants.REGEX_USER_MOBILE_FORMAT));
    }

    @Test
    public void testPwd() {
        Assert.assertEquals(false, MyString.isMatch("12345", Constants.REGEX_USER_PWD_FORMAT));
        Assert.assertEquals(false, MyString.isMatch("~!123456", Constants.REGEX_USER_PWD_FORMAT));
        Assert.assertEquals(true, MyString.isMatch("123456", Constants.REGEX_USER_PWD_FORMAT));
        Assert.assertEquals(true, MyString.isMatch("!123456", Constants.REGEX_USER_PWD_FORMAT));
        Assert.assertEquals(true, MyString.isMatch("!@#$%^&*aaDD123456", Constants.REGEX_USER_PWD_FORMAT));

    }

}
