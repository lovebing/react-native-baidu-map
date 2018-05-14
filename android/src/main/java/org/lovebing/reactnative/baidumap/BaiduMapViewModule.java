/**
 * Created by Uncle Charlie, 2018/05/14
 */
package org.lovebing.reactnative.baidumap;

import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;

public class BaiduMapViewModule extends ReactContextBaseJavaModule {

  private BaiduMapViewManager mViewManager;

  public BaiduMapViewModule(ReactApplicationContext context, BaiduMapViewManager viewManager) {
    super(context);
    mViewManager = viewManager;
  }

  @Override
  public String getName() {
    return "BaiduMapView";
  }

  @ReactMethod
  public void startLocate(Promise promise) {
    if (mViewManager != null) {
      mViewManager.startLocate(promise);
    }
  }
}
