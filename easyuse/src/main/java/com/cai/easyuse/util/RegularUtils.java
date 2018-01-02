
package com.cai.easyuse.util;

import java.util.regex.Pattern;

/**
 * 正则表达式工具类
 * <p>
 * 里面包含了一些常见的正则表达式和方法
 * 
 * @author cailingxiao
 * @date 2016年2月19日
 * 
 */
public final class RegularUtils {

    /**
     * 验证手机号码的正则
     */
    public static final String REG_PHONE = "(\\+\\d+)?1[3458]\\d{9}$";
    /**
     * 汉字
     */
    public static final String REG_CN = "^[\\u4e00-\\u9fa5]{0,}$";

    /**
     * 电子邮箱
     */
    public static final String REG_EMAIL = "^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$";

    /**
     * 身份证号码
     */
    public static final String REG_ID = "^([0-9]){7,18}(x|X)?$";

    /**
     * qq号码
     */
    public static final String REG_QQ = "[1-9][0-9]{4,}";

    /**
     * ip地址
     */
    public static final String REG_IP = "\\d+\\.\\d+\\.\\d+\\.\\d+";

    /**
     * 校验手机号
     * 
     * @param mobile 当前手机号
     * @return
     */
    public static boolean checkMobile(String mobile) {
        String regex = REG_PHONE;
        return Pattern.matches(regex, mobile);
    }

    /**
     * 是否包含中文字符
     * 
     * @param cn
     * @return
     */
    public static boolean checkCn(String cn) {
        return Pattern.matches(REG_CN, cn);
    }

    /**
     * email是否规范
     * 
     * @param email
     * @return
     */
    public static boolean checkEmail(String email) {
        return Pattern.matches(REG_EMAIL, email);
    }

    /**
     * 身份证号码格式是否正确
     * 
     * @param id
     * @return
     */
    public static boolean checkId(String id) {
        return Pattern.matches(REG_ID, id);
    }

    /**
     * qq号码的格式是否正确
     * 
     * @param qq
     * @return
     */
    public static boolean checkQq(String qq) {
        return Pattern.matches(REG_QQ, qq);
    }

    /**
     * ip地址的格式是否正确
     * 
     * @param ip
     * @return
     */
    public static boolean checkIp(String ip) {
        return Pattern.matches(REG_IP, ip);
    }
}
