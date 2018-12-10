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
import com.baidu.mapapi.map.Arc;
import com.baidu.mapapi.map.ArcOptions;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.model.LatLng;

/**
 * @author lovebing Created on Dec 09, 2018
 */
public class OverlayArc extends View implements OverlayView {

    private Arc arc;
    private Points points;
    private int width = 4;
    private int color = 0xAA00FF00;

    public OverlayArc(Context context) {
        super(context);
    }

    public OverlayArc(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public OverlayArc(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(21)
    public OverlayArc(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public void setWidth(int width) {
        this.width = width;
        if (arc != null) {
            arc.setWidth(width);
        }
    }

    public Points getPoints() {
        return points;
    }

    public void setPoints(Points points) {
        this.points = points;
        if (arc != null) {
            arc.setPoints(points.getP1(), points.getP2(), points.getP3());
        }
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
        if (arc != null) {
            arc.setColor(color);
        }
    }

    @Override
    public void addTopMap(BaiduMap baiduMap) {
        ArcOptions ooArc = new ArcOptions()
                .color(color)
                .width(width)
                .points(points.getP1(), points.getP2(), points.getP3());
        arc = (Arc) baiduMap.addOverlay(ooArc);
    }

    @Override
    public void remove() {

    }

    public static final class Points {

        private LatLng p1;
        private LatLng p2;
        private LatLng p3;

        public Points() {
        }

        public Points(LatLng p1, LatLng p2, LatLng p3) {
            this.p1 = p1;
            this.p2 = p2;
            this.p3 = p3;
        }

        public LatLng getP1() {
            return p1;
        }

        public void setP1(LatLng p1) {
            this.p1 = p1;
        }

        public LatLng getP2() {
            return p2;
        }

        public void setP2(LatLng p2) {
            this.p2 = p2;
        }

        public LatLng getP3() {
            return p3;
        }

        public void setP3(LatLng p3) {
            this.p3 = p3;
        }
    }
}
