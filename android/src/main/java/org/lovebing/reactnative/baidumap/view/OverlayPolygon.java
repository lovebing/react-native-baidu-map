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
import com.baidu.mapapi.map.Polygon;
import com.baidu.mapapi.map.PolygonOptions;
import com.baidu.mapapi.map.Stroke;
import com.baidu.mapapi.model.LatLng;

import java.util.List;

/**
 * @author lovebing Created on Dec 9, 2018
 */
public class OverlayPolygon extends View implements OverlayView {

    private Polygon polygon;
    private List<LatLng> points;
    private int fillColor = 0xAAFFFF00;
    private Stroke stroke = new Stroke(5, 0xAA00FF00);

    public OverlayPolygon(Context context) {
        super(context);
    }

    public OverlayPolygon(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public OverlayPolygon(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(21)
    public OverlayPolygon(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }


    public List<LatLng> getPoints() {
        return points;
    }

    public void setPoints(List<LatLng> points) {
        this.points = points;
        if (polygon != null) {
            polygon.setPoints(points);
        }
    }

    public int getFillColor() {
        return fillColor;
    }

    public void setFillColor(int fillColor) {
        this.fillColor = fillColor;
        if (polygon != null) {
            polygon.setFillColor(fillColor);
        }
    }

    public Stroke getStroke() {
        return stroke;
    }

    public void setStroke(Stroke stroke) {
        this.stroke = stroke;
        if (polygon != null) {
            polygon.setStroke(stroke);
        }
    }

    @Override
    public void addTopMap(BaiduMap baiduMap) {
        PolygonOptions options = new PolygonOptions()
                .points(points)
                .stroke(stroke)
                .fillColor(fillColor);
        polygon = (Polygon) baiduMap.addOverlay(options);
    }

    @Override
    public void remove() {

    }
}
