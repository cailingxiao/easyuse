package com.cai.easyuse.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import android.text.TextUtils;

/**
 * 对字符串和文件进行md5的工具
 * 
 * @author cailingxiao
 * @date 2016年2月18日
 * 
 */
public final class Md5Utils {

    private static final int HIGHT_F = 0xf0;
    private static final int LOW_F = 0xf;
    private static final int HALF_BYTE = 4;

    protected static char[] hexDigits = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e',
            'f' };
    protected static MessageDigest sMessageDigest = null;
    private static final Object sLocked = new Object();

    private Md5Utils() {

    }

    /**
     * 初始化工具
     */
    private static void initMessageDigest() {
        if (null == sMessageDigest) {
            synchronized (sLocked) {
                if (null == sMessageDigest) {
                    try {
                        sMessageDigest = MessageDigest.getInstance("MD5");
                    } catch (NoSuchAlgorithmException e) {
                        System.err.println(Md5Utils.class.getName() + "初始化失败，MessageDigest不支持MD5Util.");
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    /**
     * 文件的md5字符串转化
     * 
     * @param file
     * @return
     */
    public static synchronized String md5(File file) {
        if (null == file) {
            return "";
        }
        FileInputStream fis = null;
        FileChannel fileChannel = null;
        try {
            fis = new FileInputStream(file);
            fileChannel = fis.getChannel();
            MappedByteBuffer byteBuffer = fileChannel.map(FileChannel.MapMode.READ_ONLY, 0, file.length());
            sMessageDigest.update(byteBuffer);
            return bufferToHex(sMessageDigest.digest());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e2) {
            e2.printStackTrace();
        } finally {
            CloseUtils.close(fileChannel);
            CloseUtils.close(fis);
        }
        return "";
    }

    /**
     * 文件的16位md5值
     * 
     * @param file
     * @return
     */
    public static synchronized String md5_16(File file) {
        String md5 = md5(file);
        if (!TextUtils.isEmpty(md5)) {
            return md5.substring(8, 24);
        } else {
            return "";
        }
    }

    /**
     * 对字符串做MD5操作。
     * 
     * @param plainText 明文
     * @return hash后的密文,32位
     */
    public static synchronized String md5(String plainText) {
        initMessageDigest();
        try {
            byte[] bytes = plainText.getBytes();
            sMessageDigest.update(bytes);
            return bufferToHex(sMessageDigest.digest());
        } catch (Exception e) {
            LogUtils.e("MD5", e);
        }
        return "";
    }

    /**
     * 对字符串进行MD5操作，16位的返回值
     * 
     * @param plainText 明文
     * @return hash后的密文，16位的
     */
    public static String md5_16(String plainText) {
        return md5(plainText).substring(8, 24);
    }

    /**
     * 将byte数组变成16进制字符串
     * 
     * @param bytes
     * @return
     */
    private static String bufferToHex(byte[] bytes) {
        return bufferToHex(bytes, 0, bytes.length);
    }

    /**
     * 将byte数组变成16进制字符串
     * 
     * @param bytes
     * @param m
     * @param n
     * @return
     */
    private static String bufferToHex(byte[] bytes, int m, int n) {
        StringBuffer stringbuffer = new StringBuffer(2 * n);
        int k = m + n;
        for (int l = m; l < k; l++) {
            appendHexPair(bytes[l], stringbuffer);
        }
        return stringbuffer.toString();
    }

    /**
     * 添加
     * 
     * @param bt
     * @param stringbuffer
     */
    private static void appendHexPair(byte bt, StringBuffer stringbuffer) {
        char c0 = hexDigits[(bt & HIGHT_F) >> HALF_BYTE];
        char c1 = hexDigits[bt & LOW_F];
        stringbuffer.append(c0);
        stringbuffer.append(c1);
    }
}
