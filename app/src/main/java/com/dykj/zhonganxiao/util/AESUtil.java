package com.dykj.zhonganxiao.util;

import android.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.SecretKeySpec;

/**
 * @file: AESUtil  AES加密
 * @author: guokang
 * @date: 2019-08-30
 */
public class AESUtil {

    public AESUtil() {
    }

    public static String encrypt(String input, String key) {
        byte[] crypted = null;

        try {
            KeyGenerator kgen = KeyGenerator.getInstance("AES");
            kgen.init(128);
            SecretKeySpec skey = new SecretKeySpec(key.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(1, skey);
            crypted = cipher.doFinal(input.getBytes());
        } catch (Exception var5) {
            System.out.println(var5.toString());
        }

        return new String(Base64.encodeToString(crypted, 0));
    }

    public static String decrypt(String input, String key) {
        byte[] output = null;

        try {
            KeyGenerator kgen = KeyGenerator.getInstance("AES");
            kgen.init(128);
            SecretKeySpec skey = new SecretKeySpec(key.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(2, skey);
            output = cipher.doFinal(Base64.decode(input, 0));
        } catch (Exception var5) {
            System.out.println(var5.toString());
        }

        return new String(output);
    }
}
