package com.cai.easyuse.scan;

/**
 * Created by cailingxiao on 2017/5/12.
 */

public final class ScanConst {
    private ScanConst() {

    }

    public static final String MODULE_TAG = "ScanTag";
    public static final int AUTO_FOCUS = 1;
    public static final int DECODE = 2;
    public static final int DECODE_FAILED = 3;
    public static final int DECODE_SUCCEEDED = 4;
    public static final int ENCODE_FAILED = 5;
    public static final int ENCODE_SUCCEEDED = 6;
    public static final int LAUNCH_PRODUCT_QUERY = 7;
    public static final int QUIT = 8;
    public static final int RESTART_PREVIEW = 9;
    public static final int RETURN_SCAN_RESULT = 10;

    public static final String PARAMS_TOP_TIP = "p_top_tip";
    public static final String PARAMS_BOTTOM_TIP = "p_bottom_tip";

    public static final String DATA_RESULT = "result";
    public static final String DATA_BITMAP = "bitmap";

}
