# react-native-baidu-map [![npm version](https://img.shields.io/npm/v/react-native-baidu-map.svg?style=flat)](https://www.npmjs.com/package/react-native-baidu-map)

Baidu Map SDK modules and view for React Native(Android & IOS), support react native 0.57+

百度地图 React Native 模块，支持 react native 0.57+，已更新到最新的百度地图SDK版本。

Overlay for IOS 重构中。

Marker icon 的实现参考了 https://github.com/react-native-community/react-native-maps 的相关代码。

![Android](https://raw.githubusercontent.com/lovebing/react-native-baidu-map/master/images/android.jpg)
![IOS](https://raw.githubusercontent.com/lovebing/react-native-baidu-map/master/images/ios.jpg)


### Dev & Test 开发和测试说明
react-native doesn't support symlinks. see https://stackoverflow.com/questions/44061155/react-native-npm-link-local-dependency-unable-to-resolve-module. Can't install local package by using `npm link`.

react-native 不支持软链，参考：
https://stackoverflow.com/questions/44061155/react-native-npm-link-local-dependency-unable-to-resolve-module
所以不能使用 npm link 的方式安装本地的包


### Environments 环境要求
1.JS
- node: 8.0+

2.Android
- Android SDK: api 28+
- gradle: 4.5
- Android Studio: 3.1.3+

3.IOS
- XCode: 8.0+


### Install 安装
#### 使用本地的包 （以 example 为例）
```shell
mkdir example/node_modules/react-native-baidu-map
cp -R package.json js index.js ios android LICENSE README.md example/node_modules/react-native-baidu-map/
rm -rf example/node_modules/react-native-baidu-map/ios/RCTBaiduMap.xcodeproj

```
#### 使用 npm 源
npm install react-native-baidu-map --save

### 原生模块导入

#### Android Studio
`react-native link react-native-baidu-map`

#### IOS/Xcode
使用 pod

Podfile 增加
```
  pod 'React', :path => '../node_modules/react-native', :subspecs => [
    'Core',
    'CxxBridge',
    'DevSupport', 
    'RCTText',
    'RCTNetwork',
    'RCTWebSocket', 
    'RCTAnimation'
  ]
  pod 'yoga', :path => '../node_modules/react-native/ReactCommon/yoga'
  pod 'DoubleConversion', :podspec => '../node_modules/react-native/third-party-podspecs/DoubleConversion.podspec'
  pod 'glog', :podspec => '../node_modules/react-native/third-party-podspecs/glog.podspec'
  pod 'Folly', :podspec => '../node_modules/react-native/third-party-podspecs/Folly.podspec'

  pod 'react-native-baidu-map', :podspec => '../node_modules/react-native-baidu-map/ios/react-native-baidu-map.podspec'
```

##### AppDelegate.m init 初始化
    #import "RCTBaiduMapViewManager.h"
    - (BOOL)application:(UIApplication *)application didFinishLaunchingWithOptions:(NSDictionary *)launchOptions
    {
        ...
        [RCTBaiduMapViewManager initSDK:@"api key"];
        ...
    }

### Usage 使用方法

    import { MapView, MapTypes, Geolocation, Overlay } from 'react-native-baidu-map'

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

#### Overlay 覆盖物
    const { Marker, Arc, Circle, Polyline, Polygon, Text, InfoWindow } = Overlay;

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

