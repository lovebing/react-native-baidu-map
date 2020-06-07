/*
 * Copyright (c) 2016-present, lovebing.net.
 *
 * This source code is licensed under the MIT license found in the
 * LICENSE file in the root directory of this source tree.
 */

package org.lovebing.reactnative.baidumap.util;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.view.View;

import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;

/**
 * @author lovebing
 * @date 2020-05-25
 */
public class BitmapUtil {

    public static BitmapDescriptor createBitmapDescriptor(View view) {
        return createBitmapDescriptor(view,  0, 0);
    }

    public static BitmapDescriptor createBitmapDescriptor(View view, int width, int height) {
        if (width > 0 && height > 0) {
            view.layout(0, 0, width, height);
        } else if (view.getMeasuredWidth() == 0 || view.getMeasuredHeight() == 0) {
            view.layout(0, 0, view.getMeasuredWidth() > 0 ? view.getMeasuredWidth() : 50, view.getMeasuredHeight() > 0 ? view.getMeasuredHeight() : 100);
        }
        view.buildDrawingCache();
        BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromBitmap(view.getDrawingCache());
        view.destroyDrawingCache();
        return bitmapDescriptor;
    }

    public static Bitmap resizeBitmap(Bitmap bitmap, int width, int height) {
        float scaleWidth = ((float) width) / bitmap.getWidth();
        float scaleHeight = ((float) height) / bitmap.getHeight();
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }
}
