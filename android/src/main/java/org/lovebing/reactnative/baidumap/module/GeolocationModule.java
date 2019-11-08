/**
 * Copyright (c) 2016-present, lovebing.org.
 *
 * This source code is licensed under the MIT license found in the
 * LICENSE file in the root directory of this source tree.
 */

package org.lovebing.reactnative.baidumap.module;

import android.Manifest;
import android.util.Log;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeOption;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.baidu.mapapi.utils.CoordinateConverter;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.WritableArray;
import com.facebook.react.bridge.WritableMap;
import org.lovebing.reactnative.baidumap.support.AppUtils;

import java.util.List;

/**
 * Created by lovebing on 2016/10/28.
 */
public class GeolocationModule extends BaseModule
        implements BDLocationListener, OnGetGeoCoderResultListener {

    private LocationClient locationClient;
    private static GeoCoder geoCoder;
    private volatile boolean locating = false;
    private volatile boolean locateOnce = false;

    public GeolocationModule(ReactApplicationContext reactContext) {
        super(reactContext);
        context = reactContext;
    }

    public String getName() {
        return "BaiduGeolocationModule";
    }

    private void initLocationClient(String coorType) {
        AppUtils.checkPermission(context.getCurrentActivity(), Manifest.permission.ACCESS_FINE_LOCATION);
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationMode.Hight_Accuracy);
        option.setCoorType(coorType);
        option.setIsNeedAddress(true);
        option.setIsNeedAltitude(true);
        option.setIsNeedLocationDescribe(true);
        option.setOpenGps(true);
        locationClient = new LocationClient(context.getApplicationContext());
        locationClient.setLocOption(option);
        Log.i("locationClient", "locationClient");
        locationClient.registerLocationListener(this);
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
        geoCoder.setOnGetGeoCodeResultListener(this);
        return geoCoder;
    }

    /**
     *
     * @param sourceLatLng
     * @return
     */
    protected LatLng getBaiduCoorFromGPSCoor(LatLng sourceLatLng) {
        CoordinateConverter converter = new CoordinateConverter();
        converter.from(CoordinateConverter.CoordType.GPS);
        converter.coord(sourceLatLng);
        LatLng desLatLng = converter.convert();
        return desLatLng;

    }

    @ReactMethod
    public void getGCJ02CoorFromWGS84Coor(double lat, double lng,Promise promise) {
        WritableMap params = Arguments.createMap();
        CoordType coord = SDKInitializer.getCoordType();
        boolean isSetCoord= false;


        if (coord != BD09LL){
            SDKInitializer.setCoordType(BD09LL);
            isSet_coord=true;
        }
        LatLng src=new LatLng(lat,lng);
        CoordinateConverter converter  = new CoordinateConverter()
                .from(GPS)
                .coord(src);
        //转换坐标
        LatLng tempdest = converter.convert();


        if (isSet_coord){
            SDKInitializer.setCoordType(coord);
        }



        if (coord != GCJ02){
            SDKInitializer.setCoordType(GCJ02);
            isSetCoord= true;
        }

        CoordinateConverter descconverter  = new CoordinateConverter()
                .from(CoordinateConverter.CoordType.BD09LL)
                .coord(tempdest);
        //转换坐标
        LatLng dest = descconverter.convert();
        params.putDouble("latitude", dest.latitude);
        params.putDouble("longitude", dest.longitude);
        if (isSet_coord){
            SDKInitializer.setCoordType(coord);

        }
        //System.out.println("getGCJ02CoorFromWGS84Coor lat:"+lat+"lng:"+lng+" to "+"lat:"+dest.latitude+" lng:"+dest.longitude);

        promise.resolve(params);
    }

    @ReactMethod
    public void getBD09LLCoorFromWGS84Coor(double lat, double lng,Promise promise) {
        WritableMap params = Arguments.createMap();
        CoordType coord = SDKInitializer.getCoordType();
        boolean isSetCoord= false;

        if (coord!= BD09LL){
            SDKInitializer.setCoordType(BD09LL);

            isSetCoord= true;
        }
        LatLng src = new LatLng(lat,lng);
        CoordinateConverter converter  = new CoordinateConverter()
                .from(GPS)
                .coord(src);
        //转换坐标
        LatLng dest = converter.convert();
        params.putDouble("latitude", dest.latitude);
        params.putDouble("longitude", dest.longitude);
        if (isSet_coord){
            SDKInitializer.setCoordType(coord);

        }
        //System.out.println("getBD09LLCoorFromWGS84Coor lat:"+lat+"lng:"+lng+" to "+"lat:"+dest.latitude+" lng:"+dest.longitude);

        promise.resolve(params);
    }


    @ReactMethod
    public void getGCJ02CoorFromBD09LLCoor(double lat, double lng,Promise promise) {

        WritableMap params = Arguments.createMap();
        CoordType coord = SDKInitializer.getCoordType();
        boolean isSetCoord= false;

        if (coord != GCJ02){
            SDKInitializer.setCoordType(GCJ02);

            isSetCoord=true;
        }
        LatLng src = new LatLng(lat,lng);
        CoordinateConverter converter  = new CoordinateConverter()
                .from(CoordinateConverter.CoordType.BD09LL)
                .coord(src);
        //转换坐标
        LatLng dest = converter.convert();
        params.putDouble("latitude", dest.latitude);
        params.putDouble("longitude", dest.longitude);
        if (isSet_coord){
            SDKInitializer.setCoordType(coord);

        }
        //System.out.println("getGCJ02CoorFromBD09LLCoor lat:"+lat+"lng:"+lng+" to "+"lat:"+dest.latitude+" lng:"+dest.longitude);
        promise.resolve(params);

    }
    @ReactMethod
    public void getBD09LLCoorFromGCJ02Coor(double lat, double lng,Promise promise) {
        WritableMap params = Arguments.createMap();
        CoordType coord = SDKInitializer.getCoordType();
        boolean isSetCoord= false;

        if (coord != BD09LL){
            SDKInitializer.setCoordType(BD09LL);

            isSetCoord= true;
        }
        LatLng src = new LatLng(lat,lng);
        CoordinateConverter converter = new CoordinateConverter()
                .from(COMMON)
                .coord(src);
        //转换坐标
        LatLng dest = converter.convert();
        params.putDouble("latitude", dest.latitude);
        params.putDouble("longitude", dest.longitude);
        if (isSet_coord){
            SDKInitializer.setCoordType(coord);

        }
        //System.out.println("getBD09LLCoorFromGCJ02Coor lat:"+lat+"lng:"+lng+" to "+"lat:"+dest.latitude+" lng:"+dest.longitude);

        promise.resolve(params);
    }

    @ReactMethod
    public void convertGPSCoor(double lat, double lng, Promise promise) {
        Log.i("convertGPSCoor", "convertGPSCoor");
        LatLng latLng = getBaiduCoorFromGPSCoor(new LatLng(lat, lng));
        WritableMap map = Arguments.createMap();
        map.putDouble("latitude", latLng.latitude);
        map.putDouble("longitude", latLng.longitude);
        promise.resolve(map);
    }

    @ReactMethod
    public void getCurrentPosition(String coorType) {
        if (locating) {
            return;
        }
        locateOnce = true;
        locating = true;
        if (locationClient == null) {
            initLocationClient(coorType);
        }
        Log.i("getCurrentPosition", "getCurrentPosition");
        locationClient.start();
    }

    @ReactMethod
    public void startLocating(String coorType) {
        if (locating) {
            return;
        }
        locateOnce = false;
        locating = true;
        initLocationClient(coorType);
        locationClient.start();
    }

    @ReactMethod
    public void stopLocating() {
        locating = false;
        if (locationClient != null) {
            locationClient.stop();
            locationClient = null;
        }
    }

    @ReactMethod
    public void geocode(String city, String addr) {
        getGeoCoder().geocode(new GeoCodeOption()
                .city(city).address(addr));
    }

    @ReactMethod
    public void reverseGeoCode(double lat, double lng) {
        getGeoCoder().reverseGeoCode(new ReverseGeoCodeOption()
                .location(new LatLng(lat, lng)));
    }

    @ReactMethod
    public void reverseGeoCodeGPS(double lat, double lng) {
        getGeoCoder().reverseGeoCode(new ReverseGeoCodeOption()
                .location(getBaiduCoorFromGPSCoor(new LatLng(lat, lng))));
    }

    @Override
    public void onReceiveLocation(BDLocation bdLocation) {
        WritableMap params = Arguments.createMap();
        params.putDouble("latitude", bdLocation.getLatitude());
        params.putDouble("longitude", bdLocation.getLongitude());
        params.putDouble("speed", bdLocation.getSpeed());
        params.putDouble("direction", bdLocation.getDirection());
        params.putDouble("altitude", bdLocation.getAltitude());
        params.putDouble("radius", bdLocation.getRadius());
        params.putString("address", bdLocation.getAddrStr());
        params.putString("countryCode", bdLocation.getCountryCode());
        params.putString("country", bdLocation.getCountry());
        params.putString("province", bdLocation.getProvince());
        params.putString("cityCode", bdLocation.getCityCode());
        params.putString("city", bdLocation.getCity());
        params.putString("district", bdLocation.getDistrict());
        params.putString("street", bdLocation.getStreet());
        params.putString("streetNumber", bdLocation.getStreetNumber());
        params.putString("buildingId", bdLocation.getBuildingID());
        params.putString("buildingName", bdLocation.getBuildingName());
        Log.i("onReceiveLocation", "onGetCurrentLocationPosition");

        if (locateOnce) {
            locating = false;
            sendEvent("onGetCurrentLocationPosition", params);
            locationClient.stop();
            locationClient = null;
        } else {
            sendEvent("onLocationUpdate", params);
        }
    }

    @Override
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
            ReverseGeoCodeResult.AddressComponent addressComponent = result.getAddressDetail();
            params.putString("address", result.getAddress());
            params.putString("province", addressComponent.province);
            params.putString("city", addressComponent.city);
            params.putString("district", addressComponent.district);
            params.putString("street", addressComponent.street);
            params.putString("streetNumber", addressComponent.streetNumber);

            WritableArray list = Arguments.createArray();
            List<PoiInfo> poiList = result.getPoiList();
            for (PoiInfo info: poiList) {
                WritableMap attr = Arguments.createMap();
                attr.putString("name", info.name);
                attr.putString("address", info.address);
                attr.putString("city", info.city);
                attr.putDouble("latitude", info.location.latitude);
                attr.putDouble("longitude", info.location.longitude);
                list.pushMap(attr);
            }
            params.putArray("poiList", list);
        }
        sendEvent("onGetReverseGeoCodeResult", params);
    }
}
