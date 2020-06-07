/*
 * Copyright (c) 2016-present, lovebing.net.
 *
 * This source code is licensed under the MIT license found in the
 * LICENSE file in the root directory of this source tree.
 */

package org.lovebing.reactnative.baidumap.listener;

import android.util.Log;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.TextureMapView;
import com.baidu.mapapi.model.LatLng;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.uimanager.events.RCTEventEmitter;

import org.lovebing.reactnative.baidumap.uimanager.MapViewManager;
import org.lovebing.reactnative.baidumap.view.OverlayMarker;

import java.util.ArrayList;
import java.util.List;

public class MapListener implements BaiduMap.OnMapStatusChangeListener,
        BaiduMap.OnMapLoadedCallback,
        BaiduMap.OnMapClickListener,
        BaiduMap.OnMapDoubleClickListener,
        BaiduMap.OnMarkerClickListener {

    private List<BaiduMap.OnMapStatusChangeListener> mapStatusChangeListeners = new ArrayList<>();

    private ReactContext reactContext;
    private TextureMapView mapView;

    public MapListener(TextureMapView mapView, ReactContext reactContext) {
        this.mapView = mapView;
        this.reactContext = reactContext;
    }

    @Override
    public void onMapClick(LatLng latLng) {
        WritableMap writableMap = Arguments.createMap();
        writableMap.putDouble("latitude", latLng.latitude);
        writableMap.putDouble("longitude", latLng.longitude);
        mapView.getMap().hideInfoWindow();
        sendEvent(mapView, "onMapClick", writableMap);
    }

    @Override
    public void onMapPoiClick(MapPoi mapPoi) {
        WritableMap writableMap = Arguments.createMap();
        writableMap.putString("name", mapPoi.getName());
        writableMap.putString("uid", mapPoi.getUid());
        writableMap.putDouble("latitude", mapPoi.getPosition().latitude);
        writableMap.putDouble("longitude", mapPoi.getPosition().longitude);
        mapView.getMap().hideInfoWindow();
        sendEvent(mapView, "onMapPoiClick", writableMap);
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
        for (BaiduMap.OnMapStatusChangeListener mapStatusChangeListener : mapStatusChangeListeners) {
            mapStatusChangeListener.onMapStatusChange(mapStatus);
        }
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
        OverlayMarker overlayMarker = MapViewManager.findOverlayMaker(marker);
        mapView.getMap().hideInfoWindow();
        if (overlayMarker != null) {
            InfoWindow infoWindow = overlayMarker.getInfoWindow(marker.getPosition());
            if (infoWindow != null) {
                mapView.getMap().showInfoWindow(infoWindow);
            }
            reactContext
                    .getJSModule(RCTEventEmitter.class)
                    .receiveEvent(overlayMarker.getId(),
                            "topClick", writableMap.copy());
        }
        sendEvent(mapView, "onMarkerClick", writableMap);
        return true;
    }

    public void addMapStatusChangeListener(BaiduMap.OnMapStatusChangeListener mapStatusChangeListener) {
        mapStatusChangeListeners.add(mapStatusChangeListener);
    }

    public void removeMapStatusChangeListener(BaiduMap.OnMapStatusChangeListener mapStatusChangeListener) {
        mapStatusChangeListeners.remove(mapStatusChangeListener);
    }

    private WritableMap getEventParams(MapStatus mapStatus) {
        WritableMap writableMap = Arguments.createMap();
        WritableMap target = Arguments.createMap();
        target.putDouble("latitude", mapStatus.target.latitude);
        target.putDouble("longitude", mapStatus.target.longitude);
        writableMap.putMap("target", target);
        writableMap.putDouble("latitudeDelta", mapStatus.bound.northeast.latitude - mapStatus.bound.southwest.latitude);
        writableMap.putDouble("longitudeDelta", mapStatus.bound.northeast.longitude - mapStatus.bound.southwest.longitude);
        writableMap.putDouble("zoom", mapStatus.zoom);
        writableMap.putDouble("overlook", mapStatus.overlook);
        return writableMap;
    }

    /**
     *
     * @param eventName
     * @param params
     */
    private void sendEvent(TextureMapView mapView, String eventName, WritableMap params) {
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
