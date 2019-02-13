/**
 * Copyright (c) 2016-present, lovebing.org.
 *
 * This source code is licensed under the MIT license found in the
 * LICENSE file in the root directory of this source tree.
 */

package org.lovebing.reactnative.baidumap.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.Button;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.model.LatLng;

import java.util.Objects;

/**
 * @author lovebing Created on Dec 09, 2018
 */
public class OverlayInfoWindow extends ViewGroup implements OverlayView {

    private String title;
    private LatLng location;
    private InfoWindow infoWindow;
    private BaiduMap baiduMap;
    private Id prevId;
    private boolean visible = false;

    public OverlayInfoWindow(Context context) {
        super(context);
    }

    public OverlayInfoWindow(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public OverlayInfoWindow(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(21)
    public OverlayInfoWindow(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setLocation(LatLng location) {
        this.location = location;
    }

    public void setVisible(boolean visible) {
        if (visible && !this.visible) {
            show();
        } else if (!visible && this.visible) {
            hide();
        }
        this.visible = visible;
    }

    @Override
    public void addTopMap(BaiduMap baiduMap) {
        this.baiduMap = baiduMap;
        show();
    }

    @Override
    public void remove() {

    }

    public void show() {
        if (baiduMap != null) {
            updateInfoWindow();
            baiduMap.showInfoWindow(infoWindow);
        }
    }

    public void hide() {
        if (baiduMap != null) {
            baiduMap.hideInfoWindow();
        }
    }

    private void updateInfoWindow() {
        BitmapDescriptor bitmap;
        Id currentId = new Id(title, location);
        if (currentId.equals(prevId)) {
            return;
        }
        prevId = currentId;
        Button children = new Button(getContext());
        children.setText(title);
        bitmap = BitmapDescriptorFactory.fromView(children);
        infoWindow = new InfoWindow(bitmap, location, -47, new InfoWindow.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick() {

            }
        });
    }


    public static class Id {
        private String title;
        private LatLng location;

        public Id(String title, LatLng location) {
            this.title = title;
            this.location = location;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Id id = (Id) o;
            return Objects.equals(title, id.title) &&
                    Objects.equals(location, id.location);
        }

        @Override
        public int hashCode() {

            return Objects.hash(title, location);
        }
    }
}
