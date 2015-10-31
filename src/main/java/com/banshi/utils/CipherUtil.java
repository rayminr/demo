package com.banshi.utils;


import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.MessageDigest;

public class CipherUtil {

    public static final String SHA = "SHA";
    public static final String MD5 = "MD5";

    /**
     * MAC算法可选以下多种算法
     *
     * <pre>
     * HmacMD5
     * HmacSHA1
     * HmacSHA256
     * HmacSHA384
     * HmacSHA512
     * </pre>
     */
    public static final String MAC_MD5 = "HmacMD5";
    public static final String MAC_SHA = "HmacSHA1";
    public static final String MAC_SHA256 = "HmacSHA256";

    /**
     * BASE64加密
     *
     * @param data
     * @return
     * @throws Exception
     */
    public static String encryptBASE64(byte[] data) throws Exception {
        return (new BASE64Encoder()).encodeBuffer(data);
    }

    /**
     * BASE64解密
     *
     * @param data
     * @return
     * @throws Exception
     */
    public static byte[] decryptBASE64(String data) throws Exception {
        return (new BASE64Decoder()).decodeBuffer(data);
    }

    /**
     * MD5单向加密
     *
     * @param algorithm 算法
     * @param data       加密前key值
     * @return          加密后key值
     * @throws Exception
     */
    public static byte[] encryptMD5(String algorithm, byte[] data) throws Exception {

        MessageDigest md5 = MessageDigest.getInstance(algorithm);
        md5.update(data);

        return md5.digest();

    }

    /**
     * SHA单向加密
     *
     * @param algorithm 算法
     * @param data       加密前key值
     * @return          加密后key值
     * @throws Exception
     */
    public static byte[] encryptSHA(String algorithm, byte[] data) throws Exception {

        MessageDigest sha = MessageDigest.getInstance(algorithm);
        sha.update(data);

        return sha.digest();

    }

    /**
     * HMAC单向加密
     *
     * @param algorithm 算法
     * @param key       加密所需salt值
     * @param data      加密前key值
     * @return          加密后key值
     * @throws Exception
     */
    public static byte[] encryptHMAC(String algorithm, byte[] key, byte[] data) throws Exception {
        Mac mac = Mac.getInstance(algorithm);
        SecretKeySpec secret = new SecretKeySpec(key, mac.getAlgorithm());
        mac.init(secret);

        return mac.doFinal(data);
    }



    //十六进制下数字到字符的映射数组
    private final static String[] hexDigits = {"0", "1", "2", "3", "4",
            "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f"};


    public static String generatePassword(String inputString) {
        return encodeByMD5(inputString);
    }


    public static boolean validatePassword(String password, String inputString) {
        if (password.equals(encodeByMD5(inputString))) {
            return true;
        } else {
            return false;
        }
    }

    private static String encodeByMD5(String originString) {
        if (originString != null) {
            try {
                //创建具有指定算法名称的信息摘要
                MessageDigest md = MessageDigest.getInstance("MD5");
                //使用指定的字节数组对摘要进行最后更新，然后完成摘要计算
                byte[] results = md.digest(originString.getBytes());
                //将得到的字节数组变成字符串返回
                String resultString = byteArrayToHexString(results);
                return resultString.toUpperCase();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return null;
    }


    private static String byteArrayToHexString(byte[] b) {
        StringBuffer resultSb = new StringBuffer();
        for (int i = 0; i < b.length; i++) {
            resultSb.append(byteToHexString(b[i]));
        }
        return resultSb.toString();
    }


    private static String byteToHexString(byte b) {
        int n = b;
        if (n < 0)
            n = 256 + n;
        int d1 = n / 16;
        int d2 = n % 16;
        return hexDigits[d1] + hexDigits[d2];
    }

}
