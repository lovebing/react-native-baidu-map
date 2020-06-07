/*
 * Copyright (c) 2016-present, lovebing.net.
 *
 * This source code is licensed under the MIT license found in the
 * LICENSE file in the root directory of this source tree.
 */

package org.lovebing.reactnative.baidumap.uimanager;

import androidx.annotation.NonNull;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.ViewGroupManager;
import org.lovebing.reactnative.baidumap.view.OverlayMarkerIcon;

/**
 * @author lovebing
 * @date 2020-06-07
 */
public class OverlayMarkerIconManager extends ViewGroupManager<OverlayMarkerIcon> {

    @NonNull
    @Override
    public String getName() {
        return "BaiduMapOverlayMarkerIcon";
    }

    @NonNull
    @Override
    protected OverlayMarkerIcon createViewInstance(@NonNull ThemedReactContext reactContext) {
        return new OverlayMarkerIcon(reactContext);
    }
}
