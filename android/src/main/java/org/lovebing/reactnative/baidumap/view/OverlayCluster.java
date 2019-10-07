package org.lovebing.reactnative.baidumap.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ViewGroup;
import com.baidu.mapapi.clusterutil.clustering.ClusterManager;
import com.baidu.mapapi.map.BaiduMap;
import org.lovebing.reactnative.baidumap.listener.MapListener;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lovebing Created on 10月 07, 2019
 */
public class OverlayCluster extends ViewGroup implements OverlayView {

    private MapListener mapListener;
    private BaiduMap baiduMap;
    private List<OverlayMarker> markers = new ArrayList<>(10);
    private ClusterManager<OverlayMarker> markerClusterManager;

    public OverlayCluster(Context context) {
        super(context);
    }

    public OverlayCluster(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public OverlayCluster(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(21)
    public OverlayCluster(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

    }

    @Override
    public void addTopMap(BaiduMap baiduMap) {
        this.baiduMap = baiduMap;
        if (markerClusterManager == null) {
            markerClusterManager = new ClusterManager<>(getContext(), baiduMap);
            mapListener.addMapStatusChangeListener(markerClusterManager);
            Log.i("OverlayCluster", "addMapStatusChangeListener");
        }
        refresh();
    }

    @Override
    public void removeFromMap(BaiduMap baiduMap) {
        mapListener.removeMapStatusChangeListener(markerClusterManager);
        Log.i("OverlayCluster", "removeMapStatusChangeListener");
        markerClusterManager = null;
    }

    public void addMarker(OverlayMarker marker) {
        if (markerClusterManager == null) {
            markers.add(marker);
        } else {
            markerClusterManager.addItem(marker);
            markerClusterManager.cluster();
        }
    }

    public void removeMarker(OverlayMarker marker) {
        Log.i("OverlayCluster", "removeMarker, "
                + marker.getPosition().latitude + "," + marker.getPosition().longitude);
        markers.remove(marker);
        Log.i("OverlayCluster", "markers size: " + markers.size());
        refresh();
    }

    public void clearMarkers() {
        Log.i("OverlayCluster", "clearMarkers: " + markers.size());
        markers.clear();
        if (markerClusterManager != null) {
            markerClusterManager.clearItems();
        }
    }


    public void setMapListener(MapListener mapListener) {
        this.mapListener = mapListener;
    }

    private void refresh() {
        if (markerClusterManager != null) {
            markerClusterManager.clearItems();
            markerClusterManager.addItems(markers);
            markerClusterManager.cluster();
        }
    }
}
