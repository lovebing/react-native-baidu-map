/**
 * Copyright (c) 2016-present, lovebing.org.
 *
 * This source code is licensed under the MIT license found in the
 * LICENSE file in the root directory of this source tree.
 */

package org.lovebing.reactnative.baidumap.support;

import android.util.Log;
import com.baidu.mapapi.model.LatLng;
import com.facebook.react.bridge.ReadableMap;
import org.lovebing.reactnative.baidumap.model.LocationData;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * @author lovebing
 * @date 2019/11/02
 */
public class ConvertUtils {

    private static final String TAG = ConvertUtils.class.getName();
    private static final Map<Class, Field[]> FILED_MAP = new HashMap<>();

    public static LatLng convert(LocationData locationData) {
        if (!locationData.isValid()) {
            return null;
        }
        return new LatLng(locationData.getLatitude(), locationData.getLongitude());
    }

    public static <T> T convert(ReadableMap readableMap, Class<T> targetClass) {
        if (!FILED_MAP.containsKey(targetClass)) {
            FILED_MAP.put(targetClass, targetClass.getDeclaredFields());
        }
        try {
            T target = targetClass.newInstance();
            Field[] fields = FILED_MAP.get(targetClass);
            for (Field field : fields) {
                if (readableMap.hasKey(field.getName())) {
                    field.setAccessible(true);
                    field.set(target, getValue(readableMap, field));
                }
            }
            return target;
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    private static Object getValue(ReadableMap readableMa, Field field) {
        if (field.getType().equals(Integer.class)) {
            return readableMa.getInt(field.getName());
        }
        if (field.getType().equals(Double.class)) {
            return readableMa.getDouble(field.getName());
        }
        if (field.getType().equals(String.class)) {
            return readableMa.getString(field.getName());
        }
        if (field.getType().equals(Boolean.class)) {
            return readableMa.getBoolean(field.getName());
        }
        return null;
    }
}
