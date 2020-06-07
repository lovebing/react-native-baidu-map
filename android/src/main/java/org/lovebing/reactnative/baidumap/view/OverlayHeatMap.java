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

import androidx.annotation.Nullable;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.Gradient;
import com.baidu.mapapi.map.HeatMap;
import com.baidu.mapapi.model.LatLng;

import java.util.List;

/**
 * @author lovebing
 * @date 2020-05-22
 */
public class OverlayHeatMap extends View implements OverlayView {

    private List<LatLng> points;
    private HeatMap heatMap;
    private Gradient gradient;

    public OverlayHeatMap(Context context) {
        super(context);
    }

    public OverlayHeatMap(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public OverlayHeatMap(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setPoints(List<LatLng> points) {
        this.points = points;
    }

    public void setGradient(Gradient gradient) {
        this.gradient = gradient;
    }

    @TargetApi(21)
    public OverlayHeatMap(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public void addTopMap(BaiduMap baiduMap) {
        HeatMap.Builder build = new HeatMap.Builder()
                .data(points);
        if (gradient != null) {
            build.gradient(gradient);
        }
        heatMap = build.build();
        baiduMap.addHeatMap(heatMap);
    }

    @Override
    public void removeFromMap(BaiduMap baiduMap) {
        if (heatMap != null) {
            heatMap.removeHeatMap();
            heatMap = null;
        }
    }
}
