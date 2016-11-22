package org.lovebing.reactnative.baidumap;

import com.facebook.react.bridge.ReactApplicationContext;

/**
 * Created by lovebing on 1/30/2016.
 */
public class BaiduMapModule extends BaseModule {

    private static final String REACT_CLASS = "BaiduMapModule";
    public BaiduMapModule(ReactApplicationContext reactContext) {
        super(reactContext);
        context = reactContext;
    }

    public String getName() {
        return REACT_CLASS;
    }
}
