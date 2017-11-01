package com.cai.easyuse.base.mark;

/**
 * 空接口，作为不被混淆的标记
 * <p>
 * 将下面这行代码加入到混淆文件中
 * <p></p>
 * -keep class com.cycle.easyuse.mark.UnConfusion
 * -keep class * implements com.cycle.easyuse.mark.UnConfusion
 * -keepclassmembers class * implements com.cycle.easyuse.mark.UnConfusion {
 * public <fields>;
 * public <methods>;
 * }
 * <p>
 *
 * @author cailingxiao
 */
public interface UnConfusion {

}
