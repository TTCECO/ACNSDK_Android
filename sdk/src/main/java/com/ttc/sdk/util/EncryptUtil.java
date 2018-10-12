package com.ttc.sdk.util;


import android.util.Base64;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class EncryptUtil {

    private static final char[] HEX_CHAR_ARRAY = "0123456789abcdef".toCharArray();
    private static final char[] MD5_CHARS = new char[32];

    public static String base64(byte[] input) {
        return Base64.encodeToString(input, Base64.NO_WRAP);
    }

    public static byte[] decodeBase64(String input) {
        return Base64.decode(input, Base64.NO_WRAP);
    }

    public static String md5(String s) {

        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            byte[] digest = md5.digest(s.getBytes("UTF-8"));
            String key = md5BytesToHex(digest);
            return key;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return "";
    }

    public static String aesDecrypt(String content, String secretKey) {

        if (content == null) {
            return "";
        }
        return aesDecrypt(content.getBytes(), secretKey);
    }

    public static String aesDecrypt(byte[] content, String secretKey) {

        if (content == null) {
           return "";
        }

        try {
            // 创建AES秘钥
            SecretKeySpec key = new SecretKeySpec(secretKey.getBytes(), "AES");
            // 创建密码器
            Cipher cipher = null;
            cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");

            // 初始化解密器
            cipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(new byte[cipher.getBlockSize()]));
            // 解密
            byte[] bytes = cipher.doFinal(content);
            bytes = Arrays.copyOfRange(bytes, 16, bytes.length);
            return new String(bytes);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        }

        return "";
    }

    private static String md5BytesToHex(byte[] bytes) {
        synchronized (MD5_CHARS) {
            return bytesToHex(bytes, MD5_CHARS);
        }
    }

    private static String bytesToHex(byte[] bytes, char[] hexChars) {
        int v;
        for (int j = 0; j < bytes.length; j++) {
            v = bytes[j] & 0xFF;
            hexChars[j * 2] = HEX_CHAR_ARRAY[v >>> 4];
            hexChars[j * 2 + 1] = HEX_CHAR_ARRAY[v & 0x0F];
        }
        return new String(hexChars);
    }
}
