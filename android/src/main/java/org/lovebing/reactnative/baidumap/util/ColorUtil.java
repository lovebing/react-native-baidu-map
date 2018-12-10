/**
 * Copyright (c) 2016-present, lovebing.org.
 *
 * This source code is licensed under the MIT license found in the
 * LICENSE file in the root directory of this source tree.
 */

package org.lovebing.reactnative.baidumap.util;

import java.math.BigInteger;

/**
 * @author lovebing Created on Dec 09, 2018
 */
public class ColorUtil {

    public static int fromString(String color) {
        if (color.startsWith("#")) {
            color = color.substring(1);
        }
        return new BigInteger(color, 16).intValue();
    }
}
