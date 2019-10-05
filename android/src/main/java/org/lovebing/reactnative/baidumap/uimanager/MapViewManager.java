/**
 * Copyright (c) 2016-present, lovebing.org.
 *
 * This source code is licensed under the MIT license found in the
 * LICENSE file in the root directory of this source tree.
 */

package org.lovebing.reactnative.baidumap.uimanager;

import android.util.Log;
import android.view.View;

import android.widget.Toast;
import com.baidu.mapapi.clusterutil.clustering.Cluster;
import com.baidu.mapapi.clusterutil.clustering.ClusterManager;
import com.baidu.mapapi.map.*;
import com.baidu.mapapi.model.LatLng;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.ViewGroupManager;
import com.facebook.react.uimanager.annotations.ReactProp;

import org.lovebing.reactnative.baidumap.listener.MapListener;
import org.lovebing.reactnative.baidumap.util.LatLngUtil;
import org.lovebing.reactnative.baidumap.view.OverlayMarker;
import org.lovebing.reactnative.baidumap.view.OverlayView;

import java.util.ArrayList;
import java.util.List;

public class MapViewManager extends ViewGroupManager<MapView> {

    private static Object EMPTY_OBJ = new Object();
    private static int MAX_MARKER_COUNT_FOR_CLUSTER = 5;

    private List<Object> children = new ArrayList<>(10);
    private int childrenCount = 0;
    private boolean clusterEnabled = false;
    private ClusterManager<OverlayMarker> markerClusterManager;

    @Override
    public String getName() {
        return "BaiduMapView";
    }

    @Override
    public void addView(MapView parent, View child, int index) {
        Log.i("MapViewManager", "addView:" + index);
        if (index == 0 && !children.isEmpty()) {
            removeOldChildViews(parent.getMap());
        }
        if (child instanceof OverlayView) {
            if (child instanceof OverlayMarker && clusterEnabled) {

            } else {
                ((OverlayView) child).addTopMap(parent.getMap());
            }
            children.add(child);
        } else {
            children.add(EMPTY_OBJ);
        }
        if (clusterEnabled && children.size() >= childrenCount) {
            updateMarkerCluster();
        }
    }

    @Override
    public void removeViewAt(MapView parent, int index) {
        Log.i("MapViewManager", "removeViewAt:" + index);
        if (index < children.size()) {
            Object child = children.get(index);
            children.remove(index);
            if (clusterEnabled && child instanceof OverlayMarker) {
                if (childrenCount >= children.size()) {
                    updateMarkerCluster();
                }
            } else if (child instanceof OverlayView) {
                ((OverlayView) child).removeFromMap(parent.getMap());
            } else {
                super.removeViewAt(parent, index);
            }
        }
    }

    @Override
    protected MapView createViewInstance(ThemedReactContext themedReactContext) {
        MapView mapView =  new MapView(themedReactContext);
        BaiduMap map = mapView.getMap();
        MapListener listener = new MapListener(mapView, themedReactContext);

        markerClusterManager = new ClusterManager<>(themedReactContext, map);
        markerClusterManager.setOnClusterClickListener(new ClusterManager.OnClusterClickListener<OverlayMarker>() {
            @Override
            public boolean onClusterClick(Cluster<OverlayMarker> cluster) {
                return false;
            }
        });
        markerClusterManager.setOnClusterItemClickListener(new ClusterManager.OnClusterItemClickListener<OverlayMarker>() {
            @Override
            public boolean onClusterItemClick(OverlayMarker item) {
                return false;
            }
        });
        map.setOnMapStatusChangeListener(new BaiduMap.OnMapStatusChangeListener() {
            @Override
            public void onMapStatusChangeStart(MapStatus mapStatus) {
                listener.onMapStatusChangeStart(mapStatus);
                markerClusterManager.onMapStatusChangeStart(mapStatus);
            }

            @Override
            public void onMapStatusChangeStart(MapStatus mapStatus, int i) {
                listener.onMapStatusChangeStart(mapStatus, i);
                markerClusterManager.onMapStatusChangeStart(mapStatus, i);
            }

            @Override
            public void onMapStatusChange(MapStatus mapStatus) {
                listener.onMapStatusChange(mapStatus);
                markerClusterManager.onMapStatusChange(mapStatus);
            }

            @Override
            public void onMapStatusChangeFinish(MapStatus mapStatus) {
                listener.onMapStatusChangeFinish(mapStatus);
                markerClusterManager.onMapStatusChangeFinish(mapStatus);
            }
        });
        map.setOnMapLoadedCallback(listener);
        map.setOnMapClickListener(listener);
        map.setOnMapDoubleClickListener(listener);
        map.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                return listener.onMarkerClick(marker) && markerClusterManager.onMarkerClick(marker);
            }
        });
        return mapView;
    }

    @ReactProp(name = "zoomControlsVisible")
    public void setZoomControlsVisible(MapView mapView, boolean zoomControlsVisible) {
        mapView.showZoomControls(zoomControlsVisible);
    }

    @ReactProp(name="trafficEnabled")
    public void setTrafficEnabled(MapView mapView, boolean trafficEnabled) {
        mapView.getMap().setTrafficEnabled(trafficEnabled);
    }

    @ReactProp(name="baiduHeatMapEnabled")
    public void setBaiduHeatMapEnabled(MapView mapView, boolean baiduHeatMapEnabled) {
        mapView.getMap().setBaiduHeatMapEnabled(baiduHeatMapEnabled);
    }

    @ReactProp(name = "clusterEnabled")
    public void setClusterEnabled(MapView mapView, boolean clusterEnabled) {
        this.clusterEnabled = clusterEnabled;
    }
    @ReactProp(name = "mapType")
    public void setMapType(MapView mapView, int mapType) {
        mapView.getMap().setMapType(mapType);
    }

    @ReactProp(name="zoom")
    public void setZoom(MapView mapView, float zoom) {
        MapStatus mapStatus = new MapStatus.Builder().zoom(zoom).build();
        MapStatusUpdate mapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mapStatus);
        mapView.getMap().setMapStatus(mapStatusUpdate);
    }

    @ReactProp(name="center")
    public void setCenter(MapView mapView, ReadableMap position) {
        if(position != null) {
            LatLng point = LatLngUtil.fromReadableMap(position);
            MapStatus mapStatus = new MapStatus.Builder()
                    .target(point)
                    .build();
            MapStatusUpdate mapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mapStatus);
            mapView.getMap().setMapStatus(mapStatusUpdate);
        }
    }

    @ReactProp(name = "childrenCount")
    public void setChildrenCount(MapView mapView, Integer childrenCount) {
        Log.i("MapViewManager", "childrenCount:" + childrenCount);
        this.childrenCount = childrenCount;
    }

    private void removeOldChildViews(BaiduMap baiduMap) {
        for (Object child : children) {
            if (child instanceof OverlayView) {
                if (clusterEnabled && child instanceof OverlayMarker) {

                } else {
                    ((OverlayView) child).removeFromMap(baiduMap);
                }
            }
        }
        children.clear();
        if (clusterEnabled) {
            updateMarkerCluster();
        }
    }

    private void updateMarkerCluster() {
        markerClusterManager.clearItems();
        List<OverlayMarker> markers = new ArrayList<>();
        for (Object child : children) {
            if (child instanceof OverlayMarker) {
                markers.add((OverlayMarker) child);
            }
        }
        markerClusterManager.addItems(markers);
        markerClusterManager.cluster();
    }
}
