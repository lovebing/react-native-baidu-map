package org.lovebing.reactnative.baidumap;

import android.support.annotation.Nullable;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
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
public class BaiduMapModule extends ReactContextBaseJavaModule {
    private static final String REACT_CLASS = "BaiduMapModule";

    private static GeoCoder geoCoder;

    private ReactApplicationContext context;

    public BaiduMapModule(ReactApplicationContext reactContext) {
        super(reactContext);
        context = reactContext;
    }

    public String getName() {
        return REACT_CLASS;
    }

    @ReactMethod
    public void setMarker(double latitude, double longitude) {
        LatLng point = new LatLng(latitude, longitude);
        BitmapDescriptor bitmap = BitmapDescriptorFactory.fromResource(R.mipmap.icon_gcoding);
        OverlayOptions option = new MarkerOptions()
                .icon(bitmap)
                .position(point);
        getMap().addOverlay(option);
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

    @ReactMethod
    public void geocode(String city, String addr) {
        getGeoCoder().geocode(new GeoCodeOption()
                .city(city).address(addr));
    }

    @ReactMethod
    public void reverseGeoCode(double lat, double lng) {
        getGeoCoder().reverseGeoCode(new ReverseGeoCodeOption()
                .location(getBaiduCoorFromGPSCoor(new LatLng(lat, lng))));
    }


    /**
     *
     * @return
     */
    protected GeoCoder getGeoCoder() {
        if(geoCoder != null) {
            geoCoder.destroy();
        }
        geoCoder = GeoCoder.newInstance();
        geoCoder.setOnGetGeoCodeResultListener(new OnGetGeoCoderResultListener(){
            public void onGetGeoCodeResult(GeoCodeResult result) {
                WritableMap params = Arguments.createMap();
                if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
                    params.putInt("errcode", -1);
                }
                else {
                    params.putDouble("latitude",  result.getLocation().latitude);
                    params.putDouble("longitude",  result.getLocation().longitude);
                }
                sendEvent("onGetGeoCodeResult", params);
            }

            @Override
            public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
                WritableMap params = Arguments.createMap();
                if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
                    params.putInt("errcode", -1);
                }
                else {
                    params.putString("address",  result.getAddress());
                }
                sendEvent("onGetReverseGeoCodeResult", params);
            }
        });
        return geoCoder;
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

    /**
     *
     * @param sourceLatLng
     * @return
     */
    protected LatLng getBaiduCoorFromGPSCoor(LatLng sourceLatLng) {
        CoordinateConverter converter = new CoordinateConverter();
        converter.from(CoordType.GPS);
        converter.coord(sourceLatLng);
        LatLng desLatLng = converter.convert();
        return desLatLng;

    }

    /**
     *
     * @param eventName
     * @param params
     */
    protected void sendEvent(String eventName,@Nullable WritableMap params) {
        context
                .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
                .emit(eventName, params);
    }
}
