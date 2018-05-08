package org.lovebing.reactnative.baidumap;

import android.support.annotation.Nullable;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.modules.core.DeviceEventManagerModule;

/**
 * Created by lovebing on 2016/10/28.
 */
abstract public class BaseModule extends ReactContextBaseJavaModule {

  protected ReactApplicationContext context;

  public BaseModule(ReactApplicationContext reactContext) {
    super(reactContext);
    context = reactContext;
  }

  /**
   *
   * @param eventName
   * @param params
   */
  protected void sendEvent(String eventName, @Nullable WritableMap params) {
    context
        .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
        .emit(eventName, params);
  }
}
