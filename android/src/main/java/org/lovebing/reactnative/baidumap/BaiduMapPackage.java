package org.lovebing.reactnative.baidumap;

import android.app.Activity;
import android.content.Context;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.facebook.react.ReactPackage;
import com.facebook.react.bridge.JavaScriptModule;
import com.facebook.react.bridge.NativeModule;
import com.facebook.react.bridge.ReactApplicationContext;

import com.facebook.react.uimanager.ViewManager;


/**
 * Created by lovebing on 4/17/16.
 */
public class BaiduMapPackage implements ReactPackage {

    private Context mContext;

    BaiduMapViewManager baiduMapViewManager;

    public BaiduMapPackage(Context context) {
        this.mContext = context;
        baiduMapViewManager = new BaiduMapViewManager();
        baiduMapViewManager.initSDK(context);
    }

    @Override
    public List<NativeModule> createNativeModules(ReactApplicationContext reactContext) {
        return Arrays.<NativeModule>asList(
                new BaiduMapModule(reactContext),
                new GeolocationModule(reactContext)
        );
    }

    @Override
    public List<ViewManager> createViewManagers(
            ReactApplicationContext reactContext) {
        return Arrays.<ViewManager>asList(
                baiduMapViewManager
        );
    }

    @Override
    public List<Class<? extends JavaScriptModule>> createJSModules() {
        return Collections.emptyList();
    }
}
