package com.cai.easyuse.hybrid.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface H5CallNa {
    /**
     * 对应的调用的名称,h5调用时输入的方法名
     * 
     * @return
     */
    String invokeName();

    /**
     * 版本号，用来区分不同版本的不同表现
     * 
     * @return
     */
    int versionCode() default 1;
}
