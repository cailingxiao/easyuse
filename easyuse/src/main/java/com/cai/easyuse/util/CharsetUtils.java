package com.cai.easyuse.util;

import java.util.ArrayList;
import java.util.List;

/**
 * 字符工具,普通http可能会用到
 * 
 * @author cailingxiao
 * @date 2016年3月2日
 * 
 */
public class CharsetUtils {

    public static final String DEFAULT_ENCODING_CHARSET = "ISO-8859-1";
    public static final List<String> SUPPORT_CHARSET = new ArrayList<String>();

    static {
        SUPPORT_CHARSET.add("ISO-8859-1");

        SUPPORT_CHARSET.add("GB2312");
        SUPPORT_CHARSET.add("GBK");
        SUPPORT_CHARSET.add("GB18030");

        SUPPORT_CHARSET.add("US-ASCII");
        SUPPORT_CHARSET.add("ASCII");

        SUPPORT_CHARSET.add("ISO-2022-KR");

        SUPPORT_CHARSET.add("ISO-8859-2");

        SUPPORT_CHARSET.add("ISO-2022-JP");
        SUPPORT_CHARSET.add("ISO-2022-JP-2");

        SUPPORT_CHARSET.add("UTF-8");
    }

    private CharsetUtils() {
    }

    /**
     * 将指定的字符串str使用指定的字符编码charset进行编码
     * 
     * @param str
     * @param charset
     * @param judgeCharsetLength
     * @return
     */
    public static String toCharset(final String str, final String charset, int judgeCharsetLength) {
        try {
            String oldCharset = getEncoding(str, judgeCharsetLength);
            return new String(str.getBytes(oldCharset), charset);
        } catch (Throwable ex) {
            LogUtils.w(ex);
            return str;
        }
    }

    /**
     * 获得字符串的编码
     * 
     * @param str
     * @param judgeCharsetLength
     * @return
     */
    public static String getEncoding(final String str, int judgeCharsetLength) {
        String encode = CharsetUtils.DEFAULT_ENCODING_CHARSET;
        for (String charset : SUPPORT_CHARSET) {
            if (isCharset(str, charset, judgeCharsetLength)) {
                encode = charset;
                break;
            }
        }
        return encode;
    }

    /**
     * 判断指定字符串是否是指定的编码
     * 
     * @param str
     * @param charset
     * @param judgeCharsetLength
     * @return
     */
    public static boolean isCharset(final String str, final String charset, int judgeCharsetLength) {
        try {
            String temp = str.length() > judgeCharsetLength ? str.substring(0, judgeCharsetLength) : str;
            return temp.equals(new String(temp.getBytes(charset), charset));
        } catch (Throwable e) {
            return false;
        }
    }

}