/*
 * Copyright (c) 2016-present, lovebing.net.
 *
 * This source code is licensed under the MIT license found in the
 * LICENSE file in the root directory of this source tree.
 */

package org.lovebing.reactnative.baidumap.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.Polyline;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.map.Stroke;
import com.baidu.mapapi.model.LatLng;

import java.util.List;

/**
 * @author lovebing Created on Dec 9, 2018
 */
public class OverlayPolyline extends View implements OverlayView {

    private List<LatLng> points;
    private Polyline polyline;

    private Stroke stroke = new Stroke(1, 0xAAFF0000);

    public OverlayPolyline(Context context) {
        super(context);
    }

    public OverlayPolyline(Context context,  AttributeSet attrs) {
        super(context, attrs);
    }

    public OverlayPolyline(Context context,  AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(21)
    public OverlayPolyline(Context context,  AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public List<LatLng> getPoints() {
        return points;
    }

    public void setPoints(List<LatLng> points) {
        this.points = points;
        if (polyline != null) {
            polyline.setPoints(points);
        }
    }

    public void setStroke(Stroke stroke) {
        this.stroke = stroke;
        if (polyline != null) {
            polyline.setColor(stroke.color);
            polyline.setWidth(stroke.strokeWidth);
        }
    }

    @Override
    public void addTopMap(BaiduMap baiduMap) {
        PolylineOptions options = new PolylineOptions().width(stroke.strokeWidth)
                .color(stroke.color).points(points);
        polyline = (Polyline) baiduMap.addOverlay(options);
    }

    @Override
    public void removeFromMap(BaiduMap baiduMap) {
        if (polyline != null) {
            polyline.remove();
            polyline = null;
        }
    }
}
