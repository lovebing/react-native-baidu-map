/**
 * Copyright (c) 2016-present, lovebing.org.
 *
 * This source code is licensed under the MIT license found in the
 * LICENSE file in the root directory of this source tree.
 */

package org.lovebing.reactnative.baidumap.util;

import com.baidu.mapapi.map.Stroke;
import com.facebook.react.bridge.ReadableMap;

/**
 * @author lovebing Created on Dec 09, 2018
 */
public class StrokeUtil {

    public static Stroke fromReadableMap(ReadableMap stroke) {
        return new Stroke(stroke.getInt("width"),
                ColorUtil.fromString(stroke.getString("color")));
    }
}
