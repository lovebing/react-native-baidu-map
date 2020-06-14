/*
 * Copyright (c) 2016-present, lovebing.net.
 *
 * This source code is licensed under the MIT license found in the
 * LICENSE file in the root directory of this source tree.
 */

package org.lovebing.reactnative.baidumap.module;

import android.Manifest;
import android.util.Log;
import androidx.annotation.NonNull;

import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactMethod;

import org.lovebing.reactnative.baidumap.support.AppUtils;

/**
 * @author lovebing
 * @date 2019/10/30
 */
public class BaiduMapManager extends BaseModule {

    public BaiduMapManager(ReactApplicationContext reactContext) {
        super(reactContext);
    }

    @NonNull
    @Override
    public String getName() {
        return getClass().getSimpleName();
    }

    @ReactMethod
    public void initSDK(String key) {
        Log.i("initSDK", key);
    }

    @ReactMethod
    public void hasLocationPermission(Promise promise) {
        promise.resolve(AppUtils.hasPermission(context.getCurrentActivity(), Manifest.permission.ACCESS_FINE_LOCATION));
    }
}
