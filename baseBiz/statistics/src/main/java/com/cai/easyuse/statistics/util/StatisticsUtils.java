/*
 * Copyright (C) 2017 cailingxiao, Inc. All Rights Reserved.
 */
package com.cai.easyuse.statistics.util;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * 容器工具类，提供常见容器生成和转换的方法
 * <p>
 * Created by cailingxiao on 2017/1/10.
 */
public class StatisticsUtils {
    private StatisticsUtils() {

    }

    public static Object[] map2Array(Map map) {
        if (null != map && map.size() > 0) {
            Object[] ret = new Object[map.size() * 2];
            Iterator iterator = map.keySet().iterator();
            int i = 0;
            while (iterator.hasNext()) {
                Object key = iterator.next();
                ret[i++] = key;
                ret[i++] = map.get(key);
            }
            return ret;
        }
        return null;
    }

    public static Map array2Map(Object... array) {
        if (null != array && array.length > 0) {
            final int len = array.length / 2;
            if (len > 0) {
                Map map = new HashMap(len + 1);
                for (int i = 0; i < len; i++) {
                    map.put(array[i++], array[i]);
                }
                return map;
            }

        }
        return null;
    }

}
