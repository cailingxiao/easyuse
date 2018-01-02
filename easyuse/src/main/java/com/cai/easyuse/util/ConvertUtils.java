package com.cai.easyuse.util;

import java.math.BigDecimal;
import java.sql.Timestamp;

import android.text.TextUtils;

/**
 * 基本类型转换工具类。
 */
public class ConvertUtils {

    private ConvertUtils() {

    }

    /**
     * 将 string 类型转化为 timestamp 类型。
     *
     * @param src 被转化的string。
     *
     * @return timestamp 转化结果。
     */
    public static Timestamp str2Time(String src) {
        if (src == null) {
            return null;
        }

        try {
            return Timestamp.valueOf(src);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    /**
     * 将 string 类型转化为 Float 类型。
     *
     * @param src 被转化的string。
     *
     * @return float 转化结果。
     */
    public static Float str2Float(String src) {
        if (src == null) {
            return 0f;
        }

        try {
            return Float.valueOf(src);
        } catch (NumberFormatException e) {
            return 0f;
        }
    }

    /**
     * 将 string 类型转化为 float 类型。
     *
     * @param src 被转化的string。
     * @param def 默认值。
     *
     * @return float 转化结果。
     */
    public static Float str2Float(String src, float def) {
        if (src == null) {
            return def;
        }

        try {
            return Float.valueOf(src);
        } catch (NumberFormatException e) {
            return def;
        }
    }

    /**
     * 将 string 类型转化为 int 类型。
     *
     * @param src 被转化的string。
     *
     * @return int 转化结果。
     */
    public static Integer str2Int(String src) {
        if (src == null) {
            return 0;
        }

        try {
            return Integer.valueOf(src);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    /**
     * 将 string 类型转化为 int 类型。
     *
     * @param src 被转化的string。
     * @param def 默认值。
     *
     * @return integer 转化结果。
     */
    public static Integer str2Int(String src, int def) {
        if (src == null) {
            return def;
        }

        try {
            return Integer.valueOf(src);
        } catch (NumberFormatException e) {
            return def;
        }
    }

    /**
     * 将 string 类型转化为 Long 类型。
     *
     * @param src 被转化的string。
     *
     * @return Long 转化结果。
     */
    public static Long str2Long(String src) {
        if (src == null) {
            return 0L;
        }

        try {
            return Long.valueOf(src);
        } catch (NumberFormatException e) {
            return 0L;
        }
    }

    /**
     * 将 string 类型转化为 Long 类型。
     *
     * @param src 被转化的string。
     * @param def 默认值String。
     *
     * @return Long 转化结果。
     */
    public static Long str2Long(String src, long def) {
        if (src == null) {
            return def;
        }

        try {
            return Long.valueOf(src);
        } catch (NumberFormatException e) {
            return def;
        }
    }

    /**
     * 将 string 类型转化为 Long 类型。
     *
     * @param src 被转化的string。
     *
     * @return Long 转化结果。
     */
    public static Double str2Double(String src) {
        if (src == null) {
            return 0.0;
        }

        try {
            return Double.valueOf(src);
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }

    /**
     * 将 string 类型转化为 Long 类型。
     *
     * @param src 被转化的string。
     * @param def 默认值String。
     *
     * @return Long 转化结果。
     */
    public static Double str2Double(String src, Double def) {
        if (src == null) {
            return def;
        }

        try {
            return Double.valueOf(src);
        } catch (NumberFormatException e) {
            return def;
        }
    }

    /**
     * 将 string 类型转化为 Long 类型。
     *
     * @param src 被转化的string。
     *
     * @return Long 转化结果。
     */
    public static Character str2Character(String src) {
        int startPosition = 0;
        if (src == null) {
            return null;
        }
        try {
            return Character.valueOf(src.charAt(startPosition));
        } catch (NumberFormatException e) {
            return null;
        }
    }

    /**
     * 将 string 类型转化为 Long 类型。
     *
     * @param src 被转化的string。
     * @param def 默认值String。
     *
     * @return Long 转化结果。
     */
    public static Character str2Character(String src, Character def) {
        int startPosition = 0;
        if (src == null) {
            return def;
        }
        try {
            return Character.valueOf(src.charAt(startPosition));
        } catch (NumberFormatException e) {
            return def;
        }
    }

    /**
     * 将 string 类型转化为 Long 类型。
     *
     * @param src 被转化的string。
     *
     * @return Long 转化结果。
     */
    public static Byte str2Byte(String src) {
        if (src == null) {
            return null;
        }
        try {
            return Byte.valueOf(src);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    /**
     * 将 string 类型转化为 Long 类型。
     *
     * @param src 被转化的string。
     * @param def 默认值String。
     *
     * @return Long 转化结果。
     */
    public static Byte str2Byte(String src, Byte def) {
        if (src == null) {
            return def;
        }
        try {
            return Byte.valueOf(src);
        } catch (NumberFormatException e) {
            return def;
        }
    }

    /**
     * 将 string 类型转化为 Long 类型。
     *
     * @param src 被转化的string。
     *
     * @return Long 转化结果。
     */
    public static boolean str2Boolean(String src) {
        if (src == null) {
            return false;
        }
        try {
            return Boolean.valueOf(src);
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * 将 string 类型转化为 Long 类型。
     *
     * @param src 被转化的string。
     * @param def 默认值String。
     *
     * @return Long 转化结果。
     */
    public static boolean str2Boolean(String src, boolean def) {
        if (src == null) {
            return def;
        }
        try {
            return Boolean.valueOf(src);
        } catch (NumberFormatException e) {
            return def;
        }
    }

    /**
     * 将 string 类型转化为 Long 类型。
     *
     * @param src 被转化的string。
     *
     * @return Long 转化结果。
     */
    public static Short str2Short(String src) {
        if (src == null) {
            return 0;
        }
        try {
            return Short.valueOf(src);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    /**
     * 将 string 类型转化为 Long 类型。
     *
     * @param src 被转化的string。
     * @param def 默认值String。
     *
     * @return Long 转化结果。
     */
    public static Short str2Short(String src, Short def) {
        if (src == null) {
            return def;
        }
        try {
            return Short.valueOf(src);
        } catch (NumberFormatException e) {
            return def;
        }
    }

    /**
     * 将 string 类型转化为 BigDecimal 类型。
     *
     * @param src 被转化的string。
     *
     * @return BigDecimal 转化结果。
     */
    public static BigDecimal str2BigDecimal(String src) {
        BigDecimal result = BigDecimal.ZERO;
        try {
            result = new BigDecimal(src);
        } catch (NumberFormatException e) {
            result = BigDecimal.ZERO;
        }
        return result;
    }

    /**
     * utf-8编码转换成unicode编码
     *
     * @param origin
     *
     * @return
     */
    public static String utf8ToUnicode(String origin) {
        if (TextUtils.isEmpty(origin)) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < origin.length(); i++) {
            sb.append(String.format("\\u%04X", Character.codePointAt(origin, i)));
        }
        String unicode = sb.toString();
        return unicode;
    }

    /**
     * 将可能可以转换成int的进行转换
     *
     * @param objInt
     *
     * @return
     */
    public static int toInt(Object objInt) {
        if (null == objInt) {
            return 0;
        } else {
            if (objInt instanceof String) {
                return str2Int((String) objInt, 0);
            } else if (objInt instanceof Number){
                return ((Number) objInt).intValue();
            }
            return 0;
        }
    }

}
