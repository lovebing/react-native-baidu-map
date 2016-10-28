package org.lovebing.reactnative.baidumap;

import android.support.annotation.Nullable;
import android.util.Log;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactApplicationContext;

import com.baidu.mapapi.map.MapView;
import com.facebook.react.bridge.ReactMethod;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;

import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCodeOption;
import com.baidu.mapapi.utils.CoordinateConverter;
import com.baidu.mapapi.utils.CoordinateConverter.CoordType;

import com.facebook.react.bridge.WritableMap;
import com.facebook.react.modules.core.DeviceEventManagerModule;


/**
 * Created by lovebing on 1/30/2016.
 */
public class BaiduMapModule extends BaseModule {

    private static final String REACT_CLASS = "BaiduMapModule";


    private Marker marker;

    public BaiduMapModule(ReactApplicationContext reactContext) {
        super(reactContext);
        context = reactContext;
    }

    public String getName() {
        return REACT_CLASS;
    }

    @ReactMethod
    public void setMarker(double latitude, double longitude) {
        if(marker != null) {
            marker.remove();
        }
        LatLng point = new LatLng(latitude, longitude);
        BitmapDescriptor bitmap = BitmapDescriptorFactory.fromResource(R.mipmap.icon_gcoding);
        OverlayOptions option = new MarkerOptions()
                .icon(bitmap)
                .position(point);
        marker = (Marker)getMap().addOverlay(option);
    }

    @ReactMethod
    public void setZoom(int zoom) {
        MapStatus mapStatus = new MapStatus.Builder()
                .zoom(zoom)
                .build();
        MapStatusUpdate mapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mapStatus);
        getMap().setMapStatus(mapStatusUpdate);
    }
    
    @ReactMethod
    public void moveToCenter(double latitude, double longitude, int zoom) {
        LatLng point = new LatLng(latitude, longitude);
        MapStatus mapStatus = new MapStatus.Builder()
                .target(point)
                .zoom(zoom)
                .build();
        MapStatusUpdate mapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mapStatus);
        getMap().setMapStatus(mapStatusUpdate);
    }

    @ReactMethod
    public void setShowZoomControls(boolean show) {
        getMapView().showZoomControls(show);
    }

    @ReactMethod
    public void setMapType(int mapType) {
        getMap().setMapType(mapType);
    }

    /**
     *
     * @return
     */
    protected MapView getMapView() {
        return BaiduMapViewManager.getMapView();
    }

    protected BaiduMap getMap() {
        return BaiduMapViewManager.getMapView().getMap();
    }
}
