package org.lovebing.reactnative.baidumap.uimanager;

import android.view.View;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.ViewGroupManager;
import com.facebook.react.uimanager.annotations.ReactProp;
import org.lovebing.reactnative.baidumap.util.LatLngUtil;
import org.lovebing.reactnative.baidumap.view.OverlayInfoWindow;

/**
 * @author lovebing Created on Dec 09, 2018
 */
public class OverlayOverlayInfoWindowManager extends ViewGroupManager<OverlayInfoWindow> {

    private OverlayInfoWindow overlayInfoWindow;
    private volatile boolean _visible = false;
    @Override
    public String getName() {
        return "BaiduMapOverlayInfoWindow";
    }

    @Override
    protected OverlayInfoWindow createViewInstance(ThemedReactContext reactContext) {
        synchronized (this) {
            if (overlayInfoWindow == null) {
                overlayInfoWindow = new OverlayInfoWindow(reactContext);
            }
        }
        return overlayInfoWindow;
    }

    @Override
    public void addView(OverlayInfoWindow parent, View child, int index) {
        //parent.setChildren(child);
    }

    @ReactProp(name = "title")
    public void setTitle(OverlayInfoWindow overlayInfoWindow, String title) {
        if (overlayInfoWindow == null) {
            return;
        }
        overlayInfoWindow.setTitle(title);
    }

    @ReactProp(name = "location")
    public void setLocation(OverlayInfoWindow overlayInfoWindow, ReadableMap location) {
        if (overlayInfoWindow == null) {
            return;
        }
        overlayInfoWindow.setLocation(LatLngUtil.fromReadableMap(location));
    }

    @ReactProp(name = "visible")
    public void setVisible(OverlayInfoWindow overlayInfoWindow, boolean visible) {
        if (overlayInfoWindow == null) {
            return;
        }
        overlayInfoWindow.setVisible(visible);
    }
}
