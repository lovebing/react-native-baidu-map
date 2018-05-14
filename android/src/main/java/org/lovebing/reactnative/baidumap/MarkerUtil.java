package org.lovebing.reactnative.baidumap;

import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.facebook.react.bridge.ReadableMap;

/**
 * Created by lovebing on Sept 28, 2016.
 */
public class MarkerUtil {

  public static void updateMaker(Marker maker, ReadableMap option) {
    LatLng position = getLatLngFromOption(option);
    maker.setPosition(position);
    if (option != null && option.hasKey("title")) {
      maker.setTitle(option.getString("title"));
    }
  }

  public static Marker addMarker(MapView mapView, ReadableMap option) {
    BitmapDescriptor bitmap = BitmapDescriptorFactory.fromResource(R.mipmap.icon_gcoding);
    LatLng position = getLatLngFromOption(option);
    String title = option.hasKey("title") ? option.getString("title") : "";
    OverlayOptions overlayOptions = new MarkerOptions()
        .icon(bitmap)
        .position(position)
        .title(title);

    Marker marker = (Marker) mapView.getMap().addOverlay(overlayOptions);
    return marker;
  }


  private static LatLng getLatLngFromOption(ReadableMap option) {
    double latitude = option.getDouble("latitude");
    double longitude = option.getDouble("longitude");
    return new LatLng(latitude, longitude);

  }
}
