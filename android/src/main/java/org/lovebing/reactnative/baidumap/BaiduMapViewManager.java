package org.lovebing.reactnative.baidumap;

import static android.content.Context.SENSOR_SERVICE;

import android.content.Context;
import android.graphics.Point;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MapViewLayoutParams;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationConfiguration.LocationMode;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.UiSettings;
import com.baidu.mapapi.model.LatLng;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.uimanager.SimpleViewManager;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.annotations.ReactProp;
import com.facebook.react.uimanager.events.RCTEventEmitter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by lovebing on 12/20/2015.
 */
public class BaiduMapViewManager extends SimpleViewManager<MapView> implements SensorEventListener {

  private static final String REACT_CLASS = "RCTBaiduMapView";
  private boolean isFirstLoc = true;

  private ThemedReactContext mReactContext;

  private int mCurrentDirection = 0;
  private Double lastX = 0.0;
  private float mCurrentAccuracy;
  private double mCurrentLat;
  private double mCurrentLon;

  private Context mAppContext;
  private ReadableArray childrenPoints;
  private HashMap<String, Marker> mMarkerMap = new HashMap<>();
  private HashMap<String, List<Marker>> mMarkersMap = new HashMap<>();
  private TextView mMarkerText;
  private MapView mMapView;
  private BaiduMap mBaiduMap;
  private UiSettings mUiSettings;
  private MyLocationData locData;
  private LocationClient mLocationClient;
  private SensorManager mSensorManager;

  private MyLocationListenner mLocationListenner;

  public String getName() {
    return REACT_CLASS;
  }

  public void initSDK(Context context) {
    mAppContext = context;
    SDKInitializer.initialize(context);
  }

  public MapView createViewInstance(ThemedReactContext context) {
    Log.d(REACT_CLASS, "initilization");

    mReactContext = context;
    mMapView = new MapView(context);

    // My location enabled
    mBaiduMap = mMapView.getMap();
    mUiSettings = mBaiduMap.getUiSettings();

    mBaiduMap.setMyLocationEnabled(true);
    mBaiduMap.setMyLocationConfigeration(new MyLocationConfiguration(
        LocationMode.NORMAL, true, null));

    setListeners();
    return mMapView;
  }

  @ReactProp(name = "draggable", defaultBoolean = true)
  public void setDraggable(MapView mapView, boolean draggable) {
    mUiSettings.setScrollGesturesEnabled(draggable);
  }

  @ReactProp(name = "allGesturesEnabled", defaultBoolean = true)
  public void setAllGesturesEnabled(MapView mapView, boolean enabled) {
    if (mUiSettings != null) {
      mUiSettings.setAllGesturesEnabled(enabled);
    }
  }

  @ReactProp(name = "zoomControlsVisible", defaultBoolean = false)
  public void setZoomControlsVisible(MapView mapView, boolean zoomControlsVisible) {
    mapView.showZoomControls(zoomControlsVisible);
  }

  @ReactProp(name = "trafficEnabled", defaultBoolean = false)
  public void setTrafficEnabled(MapView mapView, boolean trafficEnabled) {
    mapView.getMap().setTrafficEnabled(trafficEnabled);
  }

  @ReactProp(name = "baiduHeatMapEnabled", defaultBoolean = false)
  public void setBaiduHeatMapEnabled(MapView mapView, boolean baiduHeatMapEnabled) {
    mapView.getMap().setBaiduHeatMapEnabled(baiduHeatMapEnabled);
  }

  @ReactProp(name = "mapType")
  public void setMapType(MapView mapView, int mapType) {
    mapView.getMap().setMapType(mapType);
  }

  @ReactProp(name = "zoom", defaultInt = 18)
  public void setZoom(MapView mapView, float zoom) {
    MapStatus mapStatus = new MapStatus.Builder().zoom(zoom).build();
    MapStatusUpdate mapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mapStatus);
    mapView.getMap().setMapStatus(mapStatusUpdate);
  }

  @ReactProp(name = "center")
  public void setCenter(MapView mapView, ReadableMap position) {
    if (position == null || !position.hasKey("latitude") || !position.hasKey("longitude")) {
      return;
    }

    double latitude = position.getDouble("latitude");
    double longitude = position.getDouble("longitude");
    LatLng point = new LatLng(latitude, longitude);

    BaiduMap map = mapView.getMap();
    MapStatus mapStatus = new MapStatus.Builder()
        .target(point)
        .build();
    MapStatusUpdate mapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mapStatus);
    map.animateMapStatus(mapStatusUpdate);
  }

  @ReactProp(name = "marker")
  public void setMarker(MapView mapView, ReadableMap option) {
    if (option != null) {
      String key = "marker_" + mapView.getId();
      Marker marker = mMarkerMap.get(key);
      if (marker != null) {
        MarkerUtil.updateMaker(marker, option);
      } else {
        marker = MarkerUtil.addMarker(mapView, option);
        mMarkerMap.put(key, marker);
      }
    }
  }

  @ReactProp(name = "markers")
  public void setMarkers(MapView mapView, ReadableArray options) {
    Log.d(REACT_CLASS, String.format("markers %s", options.toString()));

    String key = "markers_" + mapView.getId();
    List<Marker> markers = mMarkersMap.get(key);
    if (markers == null) {
      markers = new ArrayList<>();
    }
    for (int i = 0; i < options.size(); i++) {
      ReadableMap option = options.getMap(i);
      if (markers.size() > i + 1 && markers.get(i) != null) {
        MarkerUtil.updateMaker(markers.get(i), option);
      } else {
        markers.add(i, MarkerUtil.addMarker(mapView, option));
      }
    }
    if (options.size() < markers.size()) {
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
  public void setChildrenPoints(MapView mapView, ReadableArray childrenPoints) {
    this.childrenPoints = childrenPoints;
  }

  @Override
  public void onSensorChanged(SensorEvent sensorEvent) {
    double x = sensorEvent.values[SensorManager.DATA_X];
    if (Math.abs(x - lastX) > 1.0) {
      mCurrentDirection = (int) x;
      locData = new MyLocationData.Builder()
//          .accuracy(mCurrentAccuracy)
          .direction(mCurrentDirection)
          .latitude(mCurrentLat)
          .longitude(mCurrentLon)
          .build();
      mBaiduMap.setMyLocationData(locData);
    }
    lastX = x;
  }

  @Override
  public void onAccuracyChanged(Sensor sensor, int accuracy) {

  }

  @ReactMethod
  public void startLocate(Promise promise) {
    try {
      if (mLocationListenner == null) {
        mLocationListenner = new MyLocationListenner();
      }

      mBaiduMap.setMyLocationEnabled(true);
      mBaiduMap
          .setMyLocationConfiguration(new MyLocationConfiguration(LocationMode.NORMAL,
              true,
              null));
      MapStatus.Builder statusBuilder = new MapStatus.Builder();
      statusBuilder.overlook(0);
      mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(statusBuilder.build()));

      if (mSensorManager == null) {
        mSensorManager = (SensorManager) mAppContext.getSystemService(SENSOR_SERVICE);
        mSensorManager
            .registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION),
                SensorManager.SENSOR_DELAY_UI);
      }

      if (mLocationClient == null) {
        mLocationClient = new LocationClient(mReactContext);
        mLocationClient.registerLocationListener(mLocationListenner);
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true); // 打开gps
        option.setCoorType("bd09ll"); // 设置坐标类型
        option.setScanSpan(1000);
        mLocationClient.setLocOption(option);
      }

      mLocationClient.start();

      promise.resolve(null);
    } catch (Exception e) {
      promise.reject("Locate user error", e);
    }
  }

  public void stopLocate() {
    if (mLocationClient != null) {
      mLocationClient.stop();
      mLocationClient.unRegisterLocationListener(mLocationListenner);
      mLocationClient = null;
    }

    if (mSensorManager != null) {
      mSensorManager.unregisterListener(this);
    }
  }

  private void setListeners() {
    if (mMarkerText == null) {
      mMarkerText = new TextView(mMapView.getContext());
      mMarkerText.setBackgroundResource(R.drawable.popup);
      mMarkerText.setPadding(32, 32, 32, 32);
    }
    mBaiduMap.setOnMapStatusChangeListener(new BaiduMap.OnMapStatusChangeListener() {
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
        sendEvent(mMapView, "onMapStatusChangeStart", getEventParams(mapStatus));
      }

      @Override
      public void onMapStatusChangeStart(MapStatus mapStatus, int i) {
        sendEvent(mMapView, "onMapStatusChangeStart", getEventParams(mapStatus));
      }

      @Override
      public void onMapStatusChange(MapStatus mapStatus) {
        sendEvent(mMapView, "onMapStatusChange", getEventParams(mapStatus));
      }

      @Override
      public void onMapStatusChangeFinish(MapStatus mapStatus) {
        if (mMarkerText.getVisibility() != View.GONE) {
          mMarkerText.setVisibility(View.GONE);
        }
        sendEvent(mMapView, "onMapStatusChangeFinish", getEventParams(mapStatus));
      }
    });

    mBaiduMap.setOnMapLoadedCallback(new BaiduMap.OnMapLoadedCallback() {
      @Override
      public void onMapLoaded() {
        sendEvent(mMapView, "onMapLoaded", null);
      }
    });

    mBaiduMap.setOnMapClickListener(new BaiduMap.OnMapClickListener() {
      @Override
      public void onMapClick(LatLng latLng) {
        mBaiduMap.hideInfoWindow();
        WritableMap writableMap = Arguments.createMap();
        writableMap.putDouble("latitude", latLng.latitude);
        writableMap.putDouble("longitude", latLng.longitude);
        sendEvent(mMapView, "onMapClick", writableMap);
      }

      @Override
      public boolean onMapPoiClick(MapPoi mapPoi) {
        WritableMap writableMap = Arguments.createMap();
        writableMap.putString("name", mapPoi.getName());
        writableMap.putString("uid", mapPoi.getUid());
        writableMap.putDouble("latitude", mapPoi.getPosition().latitude);
        writableMap.putDouble("longitude", mapPoi.getPosition().longitude);
        sendEvent(mMapView, "onMapPoiClick", writableMap);
        return true;
      }
    });

    mBaiduMap.setOnMapDoubleClickListener(new BaiduMap.OnMapDoubleClickListener() {
      @Override
      public void onMapDoubleClick(LatLng latLng) {
        WritableMap writableMap = Arguments.createMap();
        writableMap.putDouble("latitude", latLng.latitude);
        writableMap.putDouble("longitude", latLng.longitude);
        sendEvent(mMapView, "onMapDoubleClick", writableMap);
      }
    });

    mBaiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
      @Override
      public boolean onMarkerClick(Marker marker) {
        if (marker.getTitle().length() > 0) {
          mMarkerText.setText(marker.getTitle());
          InfoWindow infoWindow = new InfoWindow(mMarkerText, marker.getPosition(), -80);
          mMarkerText.setVisibility(View.GONE);
          mBaiduMap.showInfoWindow(infoWindow);
        } else {
          mBaiduMap.hideInfoWindow();
        }
        WritableMap writableMap = Arguments.createMap();
        WritableMap position = Arguments.createMap();
        position.putDouble("latitude", marker.getPosition().latitude);
        position.putDouble("longitude", marker.getPosition().longitude);
        writableMap.putMap("position", position);
        writableMap.putString("title", marker.getTitle());
        sendEvent(mMapView, "onMarkerClick", writableMap);
        return true;
      }
    });
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
    mReactContext
        .getJSModule(RCTEventEmitter.class)
        .receiveEvent(mapView.getId(),
            "topChange",
            event);
  }

  public void addView(MapView parent, View child, int index) {
    if (childrenPoints != null) {
      Point point = new Point();
      ReadableArray item = childrenPoints.getArray(index);
      if (item != null) {
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

  /**
   * 定位SDK监听函数
   */
  public class MyLocationListenner implements BDLocationListener {

    @Override
    public void onReceiveLocation(BDLocation location) {
      Log.d(REACT_CLASS, "receive location");
      // map view 销毁后不在处理新接收的位置
      if (location == null || mMapView == null) {
        return;
      }
      mCurrentLat = location.getLatitude();
      mCurrentLon = location.getLongitude();
      mCurrentAccuracy = location.getRadius();
      locData = new MyLocationData.Builder()
//          .accuracy(location.getRadius())
          .direction(mCurrentDirection)
          .latitude(mCurrentLat)
          .longitude(mCurrentLon)
          .build();
      mBaiduMap.setMyLocationData(locData);
      if (isFirstLoc) {
        isFirstLoc = false;
        LatLng ll = new LatLng(location.getLatitude(),
            location.getLongitude());
        MapStatus.Builder builder = new MapStatus.Builder();
        builder.target(ll).zoom(18.0f);
        mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
      }

      mLocationClient.stop();
      isFirstLoc = true;
    }

    public void onReceivePoi(BDLocation poiLocation) {
      Log.d(REACT_CLASS, "onReceivePoi");
    }
  }
}
