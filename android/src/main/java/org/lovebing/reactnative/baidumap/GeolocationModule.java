package org.lovebing.reactnative.baidumap;

import android.util.Log;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeOption;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.baidu.mapapi.search.route.BikingRouteResult;
import com.baidu.mapapi.search.route.DrivingRoutePlanOption;
import com.baidu.mapapi.search.route.DrivingRouteResult;
import com.baidu.mapapi.search.route.IndoorRouteResult;
import com.baidu.mapapi.search.route.MassTransitRouteResult;
import com.baidu.mapapi.search.route.OnGetRoutePlanResultListener;
import com.baidu.mapapi.search.route.PlanNode;
import com.baidu.mapapi.search.route.RoutePlanSearch;
import com.baidu.mapapi.search.route.TransitRouteResult;
import com.baidu.mapapi.search.route.WalkingRouteResult;
import com.baidu.mapapi.utils.CoordinateConverter;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.WritableMap;

public class GeolocationModule extends BaseModule
        implements BDLocationListener, OnGetGeoCoderResultListener {

    private LocationClient locationClient;
    private static GeoCoder geoCoder;
    RoutePlanSearch mSearch;
    ReactApplicationContext context;

    public GeolocationModule(ReactApplicationContext reactContext) {
        super(reactContext);
        context = reactContext;
    }

    public String getName() {
        return "BaiduGeolocationModule";
    }


    private void initLocationClient() {
        LocationClientOption option = new LocationClientOption();
        option.setCoorType("bd09ll");
        option.setIsNeedAddress(true);
        option.setIsNeedAltitude(true);
        option.setIsNeedLocationDescribe(true);
        option.setOpenGps(true);
        locationClient = new LocationClient(context.getApplicationContext());
        locationClient.setLocOption(option);
        Log.i("locationClient", "locationClient");
        locationClient.registerLocationListener(this);
    }

    protected GeoCoder getGeoCoder() {
        if(geoCoder != null) {
            geoCoder.destroy();
        }
        geoCoder = GeoCoder.newInstance();
        geoCoder.setOnGetGeoCodeResultListener(this);
        return geoCoder;
    }


    protected LatLng getBaiduCoorFromGPSCoor(LatLng sourceLatLng) {
        CoordinateConverter converter = new CoordinateConverter();
        converter.from(CoordinateConverter.CoordType.GPS);
        converter.coord(sourceLatLng);
        LatLng desLatLng = converter.convert();
        return desLatLng;

    }

    @ReactMethod
    public void getCurrentPosition() {
        if(locationClient == null) {
            initLocationClient();
        }
        locationClient.start();
    }


    @ReactMethod
    public void getGuideStart(int type) {
        if(mSearch == null) {
            initGuideType(type);
        }else{
            initGuideType(type);
        }
    }


    private void initGuideType(int type) {
        mSearch = RoutePlanSearch.newInstance();
        guideType(type);
    }



    public void guideType(int type){
        Toast.makeText(context,"hell0"+type,Toast.LENGTH_LONG).show();
        PlanNode stNode = PlanNode.withCityNameAndPlaceName("杭州", "福鼎家园");
        PlanNode enNode = PlanNode.withCityNameAndPlaceName("杭州", "西城广场");
        mSearch.setOnGetRoutePlanResultListener(new OnGetRoutePlanResultListener() {

            @Override
            public void onGetWalkingRouteResult(WalkingRouteResult walkingRouteResult) {

            }

            @Override
            public void onGetTransitRouteResult(TransitRouteResult transitRouteResult) {

            }

            @Override
            public void onGetMassTransitRouteResult(MassTransitRouteResult massTransitRouteResult) {

            }

            @Override
            public void onGetDrivingRouteResult(DrivingRouteResult result) {
                Toast.makeText(context,"hell0",Toast.LENGTH_LONG).show();
            }

            @Override
            public void onGetIndoorRouteResult(IndoorRouteResult indoorRouteResult) {

            }

            @Override
            public void onGetBikingRouteResult(BikingRouteResult bikingRouteResult) {

            }

        });
        mSearch.drivingSearch((new DrivingRoutePlanOption()).from(stNode).to(enNode));

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
        sendEvent("onGetCurrentLocationPosition", params);
        locationClient.stop();
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
        }
        sendEvent("onGetReverseGeoCodeResult", params);
    }








}
