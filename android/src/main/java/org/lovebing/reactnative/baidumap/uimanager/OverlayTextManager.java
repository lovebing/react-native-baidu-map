/**
 * Copyright (c) 2016-present, lovebing.org.
 *
 * This source code is licensed under the MIT license found in the
 * LICENSE file in the root directory of this source tree.
 */

package org.lovebing.reactnative.baidumap.uimanager;

import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.uimanager.SimpleViewManager;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.annotations.ReactProp;

import org.lovebing.reactnative.baidumap.util.ColorUtil;
import org.lovebing.reactnative.baidumap.util.LatLngUtil;
import org.lovebing.reactnative.baidumap.view.OverlayText;

import java.math.BigInteger;

public class OverlayTextManager extends SimpleViewManager<OverlayText> {

    @Override
    public String getName() {
        return "BaiduMapOverlayText";
    }

    @Override
    protected OverlayText createViewInstance(ThemedReactContext reactContext) {
        return new OverlayText(reactContext);
    }

    @ReactProp(name = "text")
    public void setText(OverlayText overlayText, String text) {
        overlayText.setText(text);
    }

    @ReactProp(name = "fontSize")
    public void setFontSize(OverlayText overlayText, int fontSize) {
        overlayText.setFontSize(fontSize);
    }

    @ReactProp(name = "fontColor")
    public void setFontColor(OverlayText overlayText, String fontColor) {
        overlayText.setFontColor(ColorUtil.fromString(fontColor));
    }

    @ReactProp(name = "bgColor")
    public void setBgColor(OverlayText overlayText, String bgColor) {
        overlayText.setBgColor(new BigInteger(bgColor, 16).intValue());
    }
    @ReactProp(name = "rotate")
    public void setRotate(OverlayText overlayText, float rotate) {
        overlayText.setRotate(rotate);
    }

    @ReactProp(name = "location")
    public void setLocation(OverlayText overlayText, ReadableMap position) {
        overlayText.setPosition(LatLngUtil.fromReadableMap(position));
    }

}
