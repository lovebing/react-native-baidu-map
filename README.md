# [react-native-baidu-map] [![npm version](https://img.shields.io/npm/v/react-native-baidu-map.svg?style=flat)](https://www.npmjs.com/package/react-native-baidu-map)

百度地图 React Native 模块，支持 React Native 0.30+

### 安装
    npm install react-native-baidu-map
### 导入

####Android Studio
- File->New->Import Module 导入 react-native-baidu-map/android目录，设置module name 为 :react-native-baidu-map
- build.gradle `compile project(':react-native-baidu-map')`

- MainApplication`new BaiduMapPackage(getApplicationContext())`
- AndroidMainifest.xml `<meta-data
            android:name="com.baidu.lbsapi.API_KEY" android:value="xx"/>`

#### Xcode
- Project navigator->Libraries->Add Files to 选择 react-native-baidu-map/ios/RCTBaiduMap.xcodeproj
- Project navigator->Build Phases->Link Binary With Libraries 加入 libRCTBaiduMap.a
- Project navigator->Build Settings->Search Paths， Framework search paths 添加 react-native-baidu-map/ios/lib，Header search paths 添加 react-native-baidu-map/ios/RCTBaiduMap
- 添加依赖, react-native-baidu-map/ios/lib 下的全部 framwordk， CoreLocation.framework和QuartzCore.framework、OpenGLES.framework、SystemConfiguration.framework、CoreGraphics.framework、Security.framework、libsqlite3.0.tbd（xcode7以前为 libsqlite3.0.dylib）、CoreTelephony.framework 、libstdc++.6.0.9.tbd（xcode7以前为libstdc++.6.0.9.dylib）、CoreTelephony.framework
- 添加 BaiduMapAPI_Map.framework/Resources/mapapi.bundle

- 其它一些注意事项可参考百度地图LBS文档

##### AppDelegate.m 初始化
    #import "RCTBaiduMapViewManager.h"
    - (BOOL)application:(UIApplication *)application didFinishLaunchingWithOptions:(NSDictionary *)launchOptions
    {
        ...
        [RCTBaiduMapViewManager initSDK:@"api key"];
        ...
    }
  
### 使用方法

    import { MapView, MapTypes, MapModule } from 'react-native-baidu-map

#### MapView 属性
    {
        zoomControlsVisible: PropTypes.bool (Android only),
        mapType: PropTypes.number,
        zoom: PropTypes.number,
        onMapStatusChangeStart: PropTypes.func (Android only),
        onMapStatusChange: PropTypes.func,
        onMapStatusChangeFinish: PropTypes.func (Android only),
        onMapLoaded: PropTypes.func,
        onMapClick: PropTypes.func,
        onMapDoubleClick: PropTypes.func,
        onMarkerClick: PropTypes.func
    }
#### MapModule 方法
    setMarker(double lat, double lng)
    setMapType(int mapType)
    moveToCenter(double lat, double lng, float zoom)
    Promise reverseGeoCode(double lat, double lng)
    Promise reverseGeoCodeGPS(double lat, double lng)
    Promise geocode(String city, String addr),
    Promise getCurrentPosition()
      
