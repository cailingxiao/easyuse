/*
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 */
package com.cai.easyuse.util;

import java.util.Hashtable;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import android.graphics.Bitmap;

/**
 * 生成二维码
 * <p/>
 * Created by cailingxiao on 2016/8/25.
 */
public final class QrUtils {

    private static final int BLACK = 0xff000000;
    private static final int QR_COLOR = BLACK;

    private QrUtils() {

    }

    /**
     * 创建二维码,注意，二维码的外围有一圈空像素，
     *
     * @param str
     * @param widthAndHeight
     *
     * @return
     */
    public static Bitmap createQr(String str, int widthAndHeight) {
        Hashtable<EncodeHintType, Object> hints = new Hashtable<EncodeHintType, Object>();
        hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
        hints.put(EncodeHintType.MARGIN, 1);
        BitMatrix matrix = null;
        try {
            matrix = new MultiFormatWriter().encode(str, BarcodeFormat.QR_CODE, widthAndHeight, widthAndHeight, hints);
        } catch (WriterException e) {
            e.printStackTrace();
        }
        if (null == matrix) {
            return null;
        }
        int width = matrix.getWidth();
        int height = matrix.getHeight();
        int[] pixels = new int[width * height];

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (matrix.get(x, y)) {
                    pixels[y * width + x] = QR_COLOR;
                }
            }
        }
        Bitmap bitmap = null;
        try {
            bitmap = Bitmap.createBitmap(widthAndHeight, widthAndHeight, Bitmap.Config.ARGB_8888);
        } catch (Exception ex) {
            bitmap = Bitmap.createBitmap(widthAndHeight, widthAndHeight, Bitmap.Config.ARGB_4444);
        }
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
        return bitmap;
    }
}
