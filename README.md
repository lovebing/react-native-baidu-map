# react-native-baidu-map [![npm version](https://img.shields.io/npm/v/react-native-baidu-map.svg?style=flat)](https://www.npmjs.com/package/react-native-baidu-map)

Baidu Map SDK modules and view for React Native(Android & IOS), support react native 0.57+

百度地图 React Native 模块，支持 react native 0.57+，已更新到最新的百度地图SDK版本。

![Android](https://raw.githubusercontent.com/lovebing/react-native-baidu-map/master/images/android.jpg)
![IOS](https://raw.githubusercontent.com/lovebing/react-native-baidu-map/master/images/ios.jpg)
### 环境要求
1.JS
- node: 8.0 及以上

2.Android
- Android SDK: api 28 及上以上
- gradle: 4.6
- Android Studio: 3.1.3 及以上

3.IOS
- XCode: 8.0 及以上

### JS模块安装
    npm install react-native-baidu-map --save
### 原生模块导入

#### Android Studio

1.导入

方法一：
`react-native link react-native-baidu-map`

方法二：使用 mavenCentral，编辑 app/build.gradle，添加依赖
```groovy
repositories {
    mavenCentral()
}
dependencies {
    implementation 'org.lovebing.reactnative:baidu-map:1.0.1'
}
```
方法三：使用本地 aar (打包后生成)
- 在 app/libs 目录下添加 baidu-map-release.aar
- 编辑 app/build.gradle，添加依赖
```groovy
android {
    repositories {
        flatDir {
            dirs 'libs'
        }
    }
}
dependencies {
    implementation fileTree(dir: "libs", include: ["*.jar", "*.aar"])
}
```
2.编辑 MainApplication.java，添加 BaiduMapPackage

```java
public class MainApplication extends Application implements ReactApplication {
    @Override
    protected List<ReactPackage> getPackages() {
      return Arrays.<ReactPackage>asList(
          new MainReactPackage(), new BaiduMapPackage()
      );
    }
}
```

#### IOS/Xcode （重构中）
- Project navigator->Libraries->Add Files to 选择 react-native-baidu-map/ios/RCTBaiduMap.xcodeproj
- Project navigator->Build Phases->Link Binary With Libraries 加入 libRCTBaiduMap.a
- Project navigator->Build Settings->Search Paths， Framework search paths 添加 react-native-baidu-map/ios/lib，Header search paths 添加 react-native-baidu-map/ios/RCTBaiduMap
- 添加依赖, react-native-baidu-map/ios/lib 下的全部 framwordk， CoreLocation.framework和QuartzCore.framework、OpenGLES.framework、SystemConfiguration.framework、CoreGraphics.framework、Security.framework、libsqlite3.0.tbd（xcode7以前为 libsqlite3.0.dylib）、CoreTelephony.framework 、libstdc++.6.0.9.tbd（xcode7以前为libstdc++.6.0.9.dylib）
- 添加 BaiduMapAPI_Map.framework/Resources/mapapi.bundle

- 其它一些注意事项可参考百度地图LBS文档

##### AppDelegate.m init 初始化
    #import "RCTBaiduMapViewManager.h"
    - (BOOL)application:(UIApplication *)application didFinishLaunchingWithOptions:(NSDictionary *)launchOptions
    {
        ...
        [RCTBaiduMapViewManager initSDK:@"api key"];
        ...
    }

### Usage 使用方法

    import { MapView, MapTypes, Geolocation, Overelay } from 'react-native-baidu-map

#### MapView Props 属性
| Prop                    | Type  | Default  | Description
| ----------------------- |:-----:| :-------:| -------
| zoomControlsVisible     | bool  | true     | Android only
| trafficEnabled          | bool  | false    |
| baiduHeatMapEnabled     | bool  | false    |
| mapType                 | number| 1        |
| zoom                    | number| 10       |
| center                  | object| null     | {latitude: 0, longitude: 0}
| onMapStatusChangeStart  | func  | undefined| Android only
| onMapStatusChange       | func  | undefined|
| onMapStatusChangeFinish | func  | undefined| Android only
| onMapLoaded             | func  | undefined|
| onMapClick              | func  | undefined|
| onMapDoubleClick        | func  | undefined|
| onMarkerClick           | func  | undefined|
| onMapPoiClick           | func  | undefined|

#### Overelay 覆盖物
    const { Marker, Arc, Circle, Polyline, Polygon, Text, InfoWindow } = Overelay;

##### Marker Props 属性
| Prop                    | Type  | Default  | Description
| ----------------------- |:-----:| :-------:| -------
| title                   | string| null     |
| location                | object| {latitude: 0, longitude: 0}    |
| perspective             | bool  | null     |
| flat                    | bool  | null     |
| rotate                  | float | 0        |
| icon                    | any   | null     | icon图片，同 <Image> 的 source 属性
| alpha                   | float | 1        |

##### Arc Props 属性
| Prop                    | Type  | Default  | Description
| ----------------------- |:-----:| :-------:| -------
| color                   | string| AA00FF00 |
| width                   | int   | 4        |
| poins                   | array | [{latitude: 0, longitude: 0}, {latitude: 0, longitude: 0}, {latitude: 0, longitude: 0}] | 数值长度必须为 3

##### Circle Props 属性
| Prop                    | Type  | Default  | Description
| ----------------------- |:-----:| :-------:| -------
| radius                  | int   | 1400     |
| fillColor               | string| 000000FF |
| stroke                  | object| {width: 5, color: 'AA000000'} |
| center                  | object| {latitude: 0, longitude: 0}       |

##### Polyline Props 属性
| Prop                    | Type  | Default  | Description
| ----------------------- |:-----:| :-------:| -------
| points                  | array | [{latitude: 0, longitude: 0}]     |
| color                   | string| AAFF0000 |

##### Polygon Props 属性
| Prop                    | Type  | Default  | Description
| ----------------------- |:-----:| :-------:| -------
| points                  | array | [{latitude: 0, longitude: 0}]     |
| fillColor               | string| AAFFFF00 |
| stroke                  | object| {width: 5, color: 'AA00FF00'} |


##### Text Props 属性
| Prop                    | Type  | Default  | Description
| ----------------------- |:-----:| :-------:| -------
| text                    | string|          |
| fontSize                | int   |          |
| fontColor               | string|          |
| bgColor                 | string|          |
| rotate                  | float |          |
| location                | object|{latitude: 0, longitude: 0}

##### InfoWindow Props 属性
| Prop                    | Type  | Default  | Description
| ----------------------- |:-----:| :-------:| -------
| location                | object|{latitude: 0, longitude: 0}
| visible                 | bool  | false    | 点击 marker 后才能设为 true 

<MapView>
    <Marker/>
    <Arc />
    <Circle />
    <Polyline />
    <Polygon />
    <Text />
    <InfoWindow>
        <View></View>
    </InfoWindow>
</MapView>

#### Geolocation Methods

| Method                    | Result
| ------------------------- | -------
| Promise reverseGeoCode(double lat, double lng) | `{"address": "", "province": "", "cityCode": "", "city": "", "district": "", "streetName": "", "streetNumber": ""}`
| Promise reverseGeoCodeGPS(double lat, double lng) |  `{"address": "", "province": "", "cityCode": "", "city": "", "district": "", "streetName": "", "streetNumber": ""}`
| Promise geocode(String city, String addr) | {"latitude": 0.0, "longitude": 0.0}
| Promise getCurrentPosition() | IOS: `{"latitude": 0.0, "longitude": 0.0, "address": "", "province": "", "cityCode": "", "city": "", "district": "", "streetName": "", "streetNumber": ""}` Android: `{"latitude": 0.0, "longitude": 0.0, "direction": -1, "altitude": 0.0, "radius": 0.0, "address": "", "countryCode": "", "country": "", "province": "", "cityCode": "", "city": "", "district": "", "street": "", "streetNumber": "", "buildingId": "", "buildingName": ""}`
