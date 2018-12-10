/**
 * Copyright (c) 2016-present, lovebing.org.
 *
 * This source code is licensed under the MIT license found in the
 * LICENSE file in the root directory of this source tree.
 */

package org.lovebing.reactnative.baidumap.listener;

import android.support.annotation.Nullable;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.model.LatLng;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.uimanager.events.RCTEventEmitter;

public class MapListener implements BaiduMap.OnMapStatusChangeListener,
        BaiduMap.OnMapLoadedCallback,
        BaiduMap.OnMapClickListener,
        BaiduMap.OnMapDoubleClickListener,
        BaiduMap.OnMarkerClickListener {

    private ReactContext reactContext;
    private MapView mapView;

    public MapListener(MapView mapView, ReactContext reactContext) {
        this.mapView = mapView;
        this.reactContext = reactContext;
    }

    @Override
    public void onMapClick(LatLng latLng) {
        WritableMap writableMap = Arguments.createMap();
        writableMap.putDouble("latitude", latLng.latitude);
        writableMap.putDouble("longitude", latLng.longitude);
        sendEvent(mapView, "onMapClick", writableMap);
    }

    @Override
    public boolean onMapPoiClick(MapPoi mapPoi) {
        WritableMap writableMap = Arguments.createMap();
        writableMap.putString("name", mapPoi.getName());
        writableMap.putString("uid", mapPoi.getUid());
        writableMap.putDouble("latitude", mapPoi.getPosition().latitude);
        writableMap.putDouble("longitude", mapPoi.getPosition().longitude);
        sendEvent(mapView, "onMapPoiClick", writableMap);
        return true;
    }

    @Override
    public void onMapDoubleClick(LatLng latLng) {
        WritableMap writableMap = Arguments.createMap();
        writableMap.putDouble("latitude", latLng.latitude);
        writableMap.putDouble("longitude", latLng.longitude);
        sendEvent(mapView, "onMapDoubleClick", writableMap);
    }

    @Override
    public void onMapLoaded() {
        sendEvent(mapView, "onMapLoaded", null);
    }

    @Override
    public void onMapStatusChangeStart(MapStatus mapStatus) {
        sendEvent(mapView, "onMapStatusChangeStart", getEventParams(mapStatus));
    }

    @Override
    public void onMapStatusChangeStart(MapStatus mapStatus, int i) {

    }

    @Override
    public void onMapStatusChange(MapStatus mapStatus) {
        sendEvent(mapView, "onMapStatusChange", getEventParams(mapStatus));
    }

    @Override
    public void onMapStatusChangeFinish(MapStatus mapStatus) {
        sendEvent(mapView, "onMapStatusChangeFinish", getEventParams(mapStatus));
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        WritableMap writableMap = Arguments.createMap();
        WritableMap position = Arguments.createMap();
        position.putDouble("latitude", marker.getPosition().latitude);
        position.putDouble("longitude", marker.getPosition().longitude);
        writableMap.putMap("position", position);
        writableMap.putString("title", marker.getTitle());
        sendEvent(mapView, "onMarkerClick", writableMap);
        return true;
    }

    private WritableMap getEventParams(MapStatus mapStatus) {
        WritableMap writableMap = Arguments.createMap();
        WritableMap target = Arguments.createMap();
        target.putDouble("latitude", mapStatus.target.latitude);
        target.putDouble("longitude", mapStatus.target.longitude);
        writableMap.putMap("target", target);
        writableMap.putDouble("zoom", mapStatus.zoom);
        writableMap.putDouble("overlook", mapStatus.overlook);
        return writableMap;
    }

    /**
     *
     * @param eventName
     * @param params
     */
    private void sendEvent(MapView mapView, String eventName, @Nullable WritableMap params) {
        WritableMap event = Arguments.createMap();
        event.putMap("params", params);
        event.putString("type", eventName);
        reactContext
                .getJSModule(RCTEventEmitter.class)
                .receiveEvent(mapView.getId(),
                        "topChange",
                        event);
    }
}
