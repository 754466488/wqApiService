package com.dzw.micro.wq.application.utils;

import java.security.MessageDigest;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class MD5Utils {
    // 十六进制下数字到字符的映射数组
    private final static String[] hexDigits = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "A", "B", "C", "D",
            "E", "F" };

    /** 把inputString加密 */
    public static String md5(String inputStr) {
        return encodeByMD5(inputStr);
    }


    /** 对字符串进行MD5编码 */
    private static String encodeByMD5(String originString) {
        if (originString != null) {
            try {
                // 创建具有指定算法名称的信息摘要
                MessageDigest md5 = MessageDigest.getInstance("MD5");
                // 使用指定的字节数组对摘要进行最后更新，然后完成摘要计算
                byte[] results = md5.digest(originString.getBytes());
                // System.out.println(results.length);
                // 将得到的字节数组变成字符串返回
                String result = byteArrayToHexString(results);
                //  System.out.println("encode "+result);
                return result;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 轮换字节数组为十六进制字符串
     *
     * @param b
     *            字节数组
     * @return 十六进制字符串
     */
    private static String byteArrayToHexString(byte[] b) {
        StringBuffer resultSb = new StringBuffer();
        for (int i = 0; i < b.length; i++) {
            resultSb.append(byteToHexString(b[i]));
        }
        return resultSb.toString();
    }

    // 将一个字节转化成十六进制形式的字符串
    private static String byteToHexString(byte b) {
        int n = b;
        if (n < 0) {
            n = 256 + n;
        }
        int d1 = n / 16;
        int d2 = n % 16;
        return hexDigits[d1] + hexDigits[d2];
    }

    private static final char[] DIGITS =
            new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    private static MessageDigest digest;

    private MD5Utils() {
    }

    public static String encrypt(String plaintext) {
        if(digest == null) {
            Lock lock = new ReentrantLock();
            lock.lock();
            try {
                digest = MessageDigest.getInstance("MD5");
            } catch (Exception e) {
                return null;
            } finally {
                lock.unlock();
            }
        }
        digest.update(plaintext.getBytes());
        byte[] cipher = digest.digest();
        char[] str = new char[32];
        int p = 0;
        for(int i = 0; i < 16; ++i) {
            byte _b = cipher[i];
            str[p++] = DIGITS[_b >>> 4 & 15];
            str[p++] = DIGITS[_b & 15];
        }
        return new String(str);
    }
}
