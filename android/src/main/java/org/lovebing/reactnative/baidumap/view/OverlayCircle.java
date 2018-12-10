/**
 * Copyright (c) 2016-present, lovebing.org.
 *
 * This source code is licensed under the MIT license found in the
 * LICENSE file in the root directory of this source tree.
 */

package org.lovebing.reactnative.baidumap.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.Circle;
import com.baidu.mapapi.map.CircleOptions;
import com.baidu.mapapi.map.Stroke;
import com.baidu.mapapi.model.LatLng;

/**
 * @author lovebing Created on Dec 9, 2018
 */
public class OverlayCircle extends View implements OverlayView {

    private LatLng center;
    private int radius = 1400;
    private int fillColor = 0x000000FF;
    private Stroke stroke = new Stroke(5, 0xAA000000);
    private Circle circle;

    public OverlayCircle(Context context) {
        super(context);
    }

    public OverlayCircle(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public OverlayCircle(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(21)
    public OverlayCircle(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public LatLng getCenter() {
        return center;
    }

    public void setCenter(LatLng center) {
        this.center = center;
        if (circle != null) {
            circle.setCenter(center);
        }
    }

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
        if (circle != null) {
            circle.setRadius(radius);
        }
    }

    public int getFillColor() {
        return fillColor;
    }

    public void setFillColor(int fillColor) {
        this.fillColor = fillColor;
        if (circle != null) {
            circle.setFillColor(fillColor);
        }
    }

    public Stroke getStroke() {
        return stroke;
    }

    public void setStroke(Stroke stroke) {
        this.stroke = stroke;
        if (circle != null) {
            circle.setStroke(stroke);
        }
    }

    @Override
    public void addTopMap(BaiduMap baiduMap) {
        CircleOptions options = new CircleOptions().fillColor(fillColor)
                .center(center).stroke(stroke)
                .radius(radius);
        circle = (Circle) baiduMap.addOverlay(options);
    }

    @Override
    public void remove() {

    }
}
