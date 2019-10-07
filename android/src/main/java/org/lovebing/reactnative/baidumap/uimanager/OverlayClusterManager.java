package org.lovebing.reactnative.baidumap.uimanager;

import android.util.Log;
import android.view.View;
import androidx.annotation.NonNull;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.ViewGroupManager;
import org.lovebing.reactnative.baidumap.view.OverlayCluster;
import org.lovebing.reactnative.baidumap.view.OverlayMarker;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lovebing Created on 10月 07, 2019
 */
public class OverlayClusterManager extends ViewGroupManager<OverlayCluster> {

    private static Object EMPTY_OBJ = new Object();
    private List<Object> children = new ArrayList<>(10);

    @NonNull
    @Override
    public String getName() {
        return "BaiduMapOverlayCluster";
    }

    @Override
    public void addView(OverlayCluster parent, View child, int index) {
        Log.i("OverlayClusterManager", "addView: " + index);
        if (index == 0 && !children.isEmpty()) {
            removeOldChildViews(parent);
        }
        if (child instanceof OverlayMarker) {
            children.add(child);
            parent.addMarker((OverlayMarker) child);
        } else {
            children.add(EMPTY_OBJ);
        }
        super.addView(parent, child, index);
    }

    @Override
    public void removeViewAt(OverlayCluster parent, int index) {
        Log.i("OverlayClusterManager", "removeViewAt: " + index);
        Object child = children.get(index);
        children.remove(index);
        if (child instanceof OverlayMarker) {
            parent.removeMarker((OverlayMarker) child);
        }
        super.removeViewAt(parent, index);
    }

    @NonNull
    @Override
    protected OverlayCluster createViewInstance(@NonNull ThemedReactContext reactContext) {
        return new OverlayCluster(reactContext);
    }

    private void removeOldChildViews(OverlayCluster cluster) {
        children.clear();
        cluster.clearMarkers();
    }
}
