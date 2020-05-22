/*
 * Copyright (c) 2016-present, lovebing.net.
 *
 * This source code is licensed under the MIT license found in the
 * LICENSE file in the root directory of this source tree.
 */

package org.lovebing.reactnative.baidumap.uimanager;

import android.os.Build;
import android.util.Log;
import android.view.View;

import androidx.annotation.RequiresApi;

import com.baidu.mapapi.map.*;
import com.baidu.mapapi.model.LatLng;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.ViewGroupManager;
import com.facebook.react.uimanager.annotations.ReactProp;

import org.lovebing.reactnative.baidumap.listener.MapListener;
import org.lovebing.reactnative.baidumap.model.LocationData;
import org.lovebing.reactnative.baidumap.support.ConvertUtils;
import org.lovebing.reactnative.baidumap.view.OverlayCluster;
import org.lovebing.reactnative.baidumap.view.OverlayMarker;
import org.lovebing.reactnative.baidumap.view.OverlayView;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@RequiresApi(api = Build.VERSION_CODES.N)
public class MapViewManager extends ViewGroupManager<TextureMapView> {

    private MapListener mapListener;
    private static Set<OverlayMarker> overlayMarkers = ConcurrentHashMap.newKeySet();
    private int childrenCount = 0;

    @Override
    public String getName() {
        return "BaiduMapView";
    }

    @Override
    public void addView(TextureMapView parent, View child, int index) {
        Log.i("MapViewManager", "addView:" + index);
        if (child instanceof OverlayView) {
            if (child instanceof OverlayMarker) {
                overlayMarkers.add((OverlayMarker) child);
            }
            if (child instanceof OverlayCluster) {
                ((OverlayCluster) child).setMapListener(mapListener);
            }
            ((OverlayView) child).addTopMap(parent.getMap());
        }
        super.addView(parent, child, index);
    }

    @Override
    public void removeViewAt(TextureMapView parent, int index) {
        View child = parent.getChildAt(index);
        Log.i("MapViewManager", "removeViewAt:" + index + "," + child.getClass().getName());
        if (child instanceof OverlayView) {
            if (child instanceof OverlayMarker) {
                overlayMarkers.add((OverlayMarker) child);
            }
            ((OverlayView) child).removeFromMap(parent.getMap());
        }
        super.removeViewAt(parent, index);
    }

    @Override
    protected TextureMapView createViewInstance(ThemedReactContext themedReactContext) {
        TextureMapView mapView =  new TextureMapView(themedReactContext);
        BaiduMap map = mapView.getMap();
        mapListener = new MapListener(mapView, themedReactContext);
        map.setOnMapStatusChangeListener(mapListener);
        map.setOnMapLoadedCallback(mapListener);
        map.setOnMapClickListener(mapListener);
        map.setOnMapDoubleClickListener(mapListener);
        map.setOnMarkerClickListener(mapListener);
        return mapView;
    }

    @ReactProp(name = "zoomControlsVisible")
    public void setZoomControlsVisible(TextureMapView mapView, boolean zoomControlsVisible) {
        mapView.showZoomControls(zoomControlsVisible);
    }

    @ReactProp(name="trafficEnabled")
    public void setTrafficEnabled(TextureMapView mapView, boolean trafficEnabled) {
        mapView.getMap().setTrafficEnabled(trafficEnabled);
    }

    @ReactProp(name="baiduHeatMapEnabled")
    public void setBaiduHeatMapEnabled(TextureMapView mapView, boolean baiduHeatMapEnabled) {
        mapView.getMap().setBaiduHeatMapEnabled(baiduHeatMapEnabled);
    }

    @ReactProp(name = "mapType")
    public void setMapType(TextureMapView mapView, int mapType) {
        mapView.getMap().setMapType(mapType);
    }

    @ReactProp(name="zoom")
    public void setZoom(TextureMapView mapView, float zoom) {
        MapStatus mapStatus = new MapStatus.Builder().zoom(zoom).build();
        MapStatusUpdate mapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mapStatus);
        mapView.getMap().setMapStatus(mapStatusUpdate);
    }

    @ReactProp(name = "showsUserLocation")
    public void setShowsUserLocation(TextureMapView mapView, boolean showsUserLocation) {
        mapView.getMap().setMyLocationEnabled(showsUserLocation);
    }

    @ReactProp(name = "showMapPoi")
    public void showMapPoi(MapView mapView, boolean showMapPoi) {
        mapView.getMap().showMapPoi(showMapPoi);
    }

    @ReactProp(name = "locationData")
    public void setLocationData(TextureMapView mapView, ReadableMap readableMap) {
        LocationData locationData = ConvertUtils.convert(readableMap, LocationData.class);
        if (locationData == null || !locationData.isValid()) {
            return;
        }
        MyLocationData.Builder builder = new MyLocationData.Builder()
                .latitude(locationData.getLatitude())
                .longitude(locationData.getLongitude());
        if (locationData.getDirection() != null) {
            builder.direction(locationData.getDirection().floatValue());
        }
        if (locationData.getSpeed() != null) {
            builder.speed(locationData.getSpeed().floatValue());
        }
        mapView.getMap().setMyLocationData(builder.build());
    }

    @ReactProp(name="zoomGesturesEnabled")
    public void setGesturesEnabled(TextureMapView mapView, boolean zoomGesturesEnabled) {
        UiSettings setting = mapView.getMap().getUiSettings();
        setting.setZoomGesturesEnabled(zoomGesturesEnabled);
    }

    @ReactProp(name="scrollGesturesEnabled")
    public void setScrollEnabled(TextureMapView mapView, boolean scrollGesturesEnabled) {
        UiSettings setting = mapView.getMap().getUiSettings();
        setting.setScrollGesturesEnabled(scrollGesturesEnabled);
    }

    @ReactProp(name="center")
    public void setCenter(TextureMapView mapView, ReadableMap position) {
        LocationData locationData = ConvertUtils.convert(position, LocationData.class);
        LatLng point = ConvertUtils.convert(locationData);
        if (point == null) {
            return;
        }
        MapStatus mapStatus = new MapStatus.Builder()
                .target(point)
                .build();
        MapStatusUpdate mapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mapStatus);
        mapView.getMap().setMapStatus(mapStatusUpdate);
    }

    @ReactProp(name = "childrenCount")
    public void setChildrenCount(TextureMapView mapView, Integer childrenCount) {
        Log.i("MapViewManager", "childrenCount:" + childrenCount);
        this.childrenCount = childrenCount;
    }


    public static OverlayMarker findOverlayMaker(Marker marker) {
        for (OverlayMarker overlayMarker : overlayMarkers) {
            if (marker.equals(overlayMarker.getMarker())) {
                return overlayMarker;
            }
            if (marker.getPosition() != null && marker.getPosition().latitude == overlayMarker.getPosition().latitude
                    && marker.getPosition().longitude == overlayMarker.getPosition().longitude) {
                return overlayMarker;
            }
        }
        return null;
    }
}
