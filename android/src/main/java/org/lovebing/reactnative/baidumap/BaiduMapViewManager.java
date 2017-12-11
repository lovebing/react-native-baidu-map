package org.lovebing.reactnative.baidumap;

import android.content.Context;
import android.graphics.Point;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapViewLayoutParams;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.TextureMapView;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.route.BikingRoutePlanOption;
import com.baidu.mapapi.search.route.BikingRouteResult;
import com.baidu.mapapi.search.route.DrivingRoutePlanOption;
import com.baidu.mapapi.search.route.DrivingRouteResult;
import com.baidu.mapapi.search.route.IndoorRouteResult;
import com.baidu.mapapi.search.route.MassTransitRoutePlanOption;
import com.baidu.mapapi.search.route.MassTransitRouteResult;
import com.baidu.mapapi.search.route.OnGetRoutePlanResultListener;
import com.baidu.mapapi.search.route.PlanNode;
import com.baidu.mapapi.search.route.RoutePlanSearch;
import com.baidu.mapapi.search.route.TransitRouteResult;
import com.baidu.mapapi.search.route.WalkingRoutePlanOption;
import com.baidu.mapapi.search.route.WalkingRouteResult;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.ViewGroupManager;
import com.facebook.react.uimanager.annotations.ReactProp;
import com.facebook.react.uimanager.events.RCTEventEmitter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import overlayutil.DrivingRouteOverlay;



public class BaiduMapViewManager extends ViewGroupManager<TextureMapView> {

    private static final String REACT_CLASS = "RCTBaiduMapView";

    private ThemedReactContext mReactContext;

    private ReadableArray childrenPoints;
    private HashMap<String, Marker> mMarkerMap = new HashMap<>();
    private HashMap<String, List<Marker>> mMarkersMap = new HashMap<>();
    private TextView mMarkerText;
    Context context;


    @Override
    public String getName() {
        return REACT_CLASS;
    }



    public void initSDK(Context context) {
        SDKInitializer.initialize(context);
    }


    @Override
    protected TextureMapView createViewInstance(ThemedReactContext reactContext) {
        SDKInitializer.initialize(reactContext.getApplicationContext());
        mReactContext = reactContext;
        TextureMapView mapView =  new TextureMapView(reactContext);
        setListeners(mapView);
        this.context=reactContext;
        return mapView;
    }

    @Override
    public void addView(TextureMapView parent, View child, int index) {
        if(childrenPoints != null) {
            Point point = new Point();
            ReadableArray item = childrenPoints.getArray(index);
            if(item != null) {
                point.set(item.getInt(0), item.getInt(1));
                MapViewLayoutParams mapViewLayoutParams = new MapViewLayoutParams
                        .Builder()
                        .layoutMode(MapViewLayoutParams.ELayoutMode.absoluteMode)
                        .point(point)
                        .build();
                parent.addView(child, mapViewLayoutParams);
            }
        }

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
    @ReactProp(name="center")
    public void setCenter(TextureMapView mapView, ReadableMap position) {
        if(position != null) {
            double latitude = position.getDouble("latitude");
            double longitude = position.getDouble("longitude");
            LatLng point = new LatLng(latitude, longitude);
            MapStatus mapStatus = new MapStatus.Builder()
                    .target(point)
                    .build();
            MapStatusUpdate mapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mapStatus);
            mapView.getMap().setMapStatus(mapStatusUpdate);
        }
    }

    @ReactProp(name="marker")
    public void setMarker(TextureMapView mapView, ReadableMap option) {
        if(option != null) {
            String key = "marker_" + mapView.getId();
            Marker marker = mMarkerMap.get(key);
            if(marker != null) {
                MarkerUtil.updateMaker(marker, option);
            }
            else {
                marker = MarkerUtil.addMarker(mapView, option);
                mMarkerMap.put(key, marker);
            }
        }
    }

    @ReactProp(name="markers")
    public void setMarkers(TextureMapView mapView, ReadableArray options) {
        String key = "markers_" + mapView.getId();
        List<Marker> markers = mMarkersMap.get(key);
        if(markers == null) {
            markers = new ArrayList<>();
        }
        for (int i = 0; i < options.size(); i++) {
            ReadableMap option = options.getMap(i);
            if(markers.size() > i + 1 && markers.get(i) != null) {
                MarkerUtil.updateMaker(markers.get(i), option);
            }
            else {
                markers.add(i, MarkerUtil.addMarker(mapView, option));
            }
        }
        if(options.size() < markers.size()) {
            int start = markers.size() - 1;
            int end = options.size();
            for (int i = start; i >= end; i--) {
                markers.get(i).remove();
                markers.remove(i);
            }
        }
        mMarkersMap.put(key, markers);
    }

    @ReactProp(name = "childrenPoints")
    public void setChildrenPoints(TextureMapView mapView, ReadableArray childrenPoints) {
        this.childrenPoints = childrenPoints;
    }


    private void setListeners(final TextureMapView mapView) {
        BaiduMap map = mapView.getMap();

        if(mMarkerText == null) {
            mMarkerText = new TextView(mapView.getContext());
            mMarkerText.setBackgroundResource(R.drawable.popup);
            mMarkerText.setPadding(32, 32, 32, 32);
        }

        map.setOnMapStatusChangeListener(new BaiduMap.OnMapStatusChangeListener() {
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
                if(mMarkerText.getVisibility() != View.GONE) {
                    mMarkerText.setVisibility(View.GONE);
                }
                sendEvent(mapView, "onMapStatusChangeFinish", getEventParams(mapStatus));
            }
        });

        map.setOnMapLoadedCallback(new BaiduMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {
                sendEvent(mapView, "onMapLoaded", null);
            }
        });

        map.setOnMapClickListener(new BaiduMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                mapView.getMap().hideInfoWindow();
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
        });
        map.setOnMapDoubleClickListener(new BaiduMap.OnMapDoubleClickListener() {
            @Override
            public void onMapDoubleClick(LatLng latLng) {
                WritableMap writableMap = Arguments.createMap();
                writableMap.putDouble("latitude", latLng.latitude);
                writableMap.putDouble("longitude", latLng.longitude);
                sendEvent(mapView, "onMapDoubleClick", writableMap);
            }
        });

        map.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                if(marker.getTitle()!=null && marker.getTitle().length() > 0) {
                    mMarkerText.setText(marker.getTitle());
                    InfoWindow infoWindow = new InfoWindow(mMarkerText, marker.getPosition(), -80);
                    mMarkerText.setVisibility(View.GONE);
                    mapView.getMap().showInfoWindow(infoWindow);
                }
                else {
                    mapView.getMap().hideInfoWindow();
                }
                WritableMap writableMap = Arguments.createMap();
                WritableMap position = Arguments.createMap();
                position.putDouble("latitude", marker.getPosition().latitude);
                position.putDouble("longitude", marker.getPosition().longitude);
                writableMap.putMap("position", position);
                writableMap.putString("title", marker.getTitle());
                sendEvent(mapView, "onMarkerClick", writableMap);
                return true;
            }
        });
    }
    @ReactProp(name = "routeType")
    public void guideStart(TextureMapView mapView,int type){
        getGuideStart(mapView,type);
    }

    private void sendEvent(TextureMapView mapView, String eventName, @Nullable WritableMap params) {
        WritableMap event = Arguments.createMap();
        event.putMap("params", params);
        event.putString("type", eventName);
        mReactContext
                .getJSModule(RCTEventEmitter.class)
                .receiveEvent(mapView.getId(),
                        "topChange",
                        event);
    }

    RoutePlanSearch mSearch;

    public void getGuideStart(TextureMapView mapView,int type) {
        if(mSearch == null && type != 10) {
            initGuideType(mapView,type);
        }else{
            guideType(mapView,type);
        }
    }


    private void initGuideType(final TextureMapView mapView, int type) {
        mSearch = RoutePlanSearch.newInstance();
        mSearch.setOnGetRoutePlanResultListener(new OnGetRoutePlanResultListener() {

            @Override
            public void onGetWalkingRouteResult(WalkingRouteResult walkingRouteResult) {

            }

            @Override
            public void onGetTransitRouteResult(TransitRouteResult transitRouteResult) {

            }

            @Override
            public void onGetMassTransitRouteResult(MassTransitRouteResult massTransitRouteResult) {
                BaiduMap baiduMap = mapView.getMap();
                baiduMap.clear();
            }

            @Override
            public void onGetDrivingRouteResult(DrivingRouteResult result) {
                BaiduMap baiduMap = mapView.getMap();
                Toast.makeText(context,"hello",Toast.LENGTH_LONG).show();
                if(result == null || result.error != SearchResult.ERRORNO.NO_ERROR){
                    Toast.makeText(context, "抱歉，未找到结果", Toast.LENGTH_SHORT).show();
                }
                if (result.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
                    //起终点或途经点地址有岐义，通过以下接口获取建议查询信息
                    //result.getSuggestAddrInfo()
                    return;
                }
                if (result.error == SearchResult.ERRORNO.NO_ERROR) {
                    Toast.makeText(context, "成功"+result.getRouteLines().size(), Toast.LENGTH_SHORT).show();
                    baiduMap.clear();
                    // mroute = result.getRouteLines().get(0);
                    DrivingRouteOverlay overlay = new DrivingRouteOverlay(baiduMap);
//                    mrouteOverlay = overlay;
                    baiduMap.setOnMarkerClickListener(overlay);
                    overlay.setData(result.getRouteLines().get(0));
                    overlay.addToMap();
                    overlay.zoomToSpan();
                }
            }

            @Override
            public void onGetIndoorRouteResult(IndoorRouteResult indoorRouteResult) {

            }

            @Override
            public void onGetBikingRouteResult(BikingRouteResult bikingRouteResult) {

            }

        });
        guideType(mapView,type);
    }

    private static final int WALK = 4;
    private static final int BIKE = 3;
    private static final int CAR = 1;
    private static final int BUS = 2;


    public void guideType(final TextureMapView mapView, int type){
        Toast.makeText(context,"hell0"+type,Toast.LENGTH_LONG).show();
        PlanNode stNode = PlanNode.withCityNameAndPlaceName("杭州", "福鼎家园");
        PlanNode enNode = PlanNode.withCityNameAndPlaceName("杭州", "西城广场");
        switch (type){
            case WALK:
                mSearch.walkingSearch((new WalkingRoutePlanOption()).from(stNode).to(enNode));
                break;
            case BIKE:
                mSearch.bikingSearch((new BikingRoutePlanOption()).from(stNode).to(enNode));
                break;
            case CAR:
                mSearch.drivingSearch((new DrivingRoutePlanOption()).from(stNode).to(enNode));
                break;
            case BUS:
                mSearch.masstransitSearch((new MassTransitRoutePlanOption()).from(stNode).to(enNode));
                break;
            default:
                break;
        }


    }
}
