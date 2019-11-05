# react-native-baidu-map [![npm version](https://img.shields.io/npm/v/react-native-baidu-map.svg?style=flat)](https://www.npmjs.com/package/react-native-baidu-map)

Baidu Map SDK modules and views for React Native(Android & iOS), support react native 0.61.2+

百度地图 React Native 模块，支持 react native 0.61.2+。
使用百度地图SDK最新版本。
Android 版导入的 SDK 包含以下模块：
- 基础定位
- 基础地图（含室内图）
- 检索功能、LBS云检索
- 计算工具

### 在线交流
QQ群：561086908

### 近期 TODO:
#### Android
- 完善 Marker
- 完善坐标转换
- 添加一些常用的方法

#### iOS
- 完善 Marker
- 完善坐标标转换
- 完善 Cluster（点聚合）
- 支持 Overlay（覆盖物）
- 添加一些常用的方法

Marker icon 的实现参考了 https://github.com/react-native-community/react-native-maps 的相关代码。

![Android](https://raw.githubusercontent.com/lovebing/react-native-baidu-map/master/images/android.jpg)
![iOS](https://raw.githubusercontent.com/lovebing/react-native-baidu-map/master/images/ios.jpg)


### Dev & Test 开发和测试说明
react-native doesn't support symlinks. see https://stackoverflow.com/questions/44061155/react-native-npm-link-local-dependency-unable-to-resolve-module. Can't install local package by using `npm link`.

react-native 不支持软链，参考：
https://stackoverflow.com/questions/44061155/react-native-npm-link-local-dependency-unable-to-resolve-module
所以不能使用 npm link 的方式安装本地的包


### Environments 环境要求
1.JS
- node: 8.0+
- react-native: 0.61.+
2.Android
- Android SDK: api 28+
- gradle: 4.10.1
- Android Studio: 3.1.3+

3.iOS
- XCode: 8.0+


### Install 安装
#### 使用本地的包 （以 example 为例）
```shell
mkdir example/node_modules/react-native-baidu-map
cp -R package.json js index.js ios android LICENSE README.md react-native-baidu-map.podspec example/node_modules/react-native-baidu-map/

```
#### 使用 npm 源
npm install react-native-baidu-map --save

### 原生模块导入
`react-native link react-native-baidu-map`

#### Android

如果使用 react-native 0.61 以下的版本会报错，需要修改一下 react-native-baidu-map 的 build.gradle，
把 `compileOnly 'com.facebook.react:react-native:0.61.2'` 改为 `compileOnly 'com.facebook.react:react-native:0.60.1'`

如下：

![修改 build.gradle](http://repo.codeboot.net/resources/images/react-native-baidu-map/react-native-0.60-config.png)


#### iOS
如果 iOS 项目包含 Podfile，会自动加上 react-native-baidu-map 的依赖，只需要执行 `pod install`，不需要做其它处理。
如果没有 Podfile，则需要手动导入百度地图和定位 SDK 的依赖，参考 http://lbsyun.baidu.com/index.php?title=iossdk/guide/create-project/oc
和 http://lbsyun.baidu.com/index.php?title=ios-locsdk/guide/create-project/manual-create

### 初始化
#### Android
AndroidManifest.xml 设置

必要的权限

```
    <uses-permission android:name="android.permission.INTERNET" />
    <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
    <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
    <uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" />
    <uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />
```

application 下添加名为 com.baidu.lbsapi.API_KEY 的 meta，如

```
<meta-data
        android:name="com.baidu.lbsapi.API_KEY"
        android:value="uDRdqQMGQeoPGn5CwMmIYicdUIVv1YST" />
```

如果没有开启 androidx，需要手动修改 AppUtils 相应的包

#### iOS
使用 BaiduMapManager.initSDK 方法设置 api key，如
```
import { BaiduMapManager } from 'react-native-baidu-map'
BaiduMapManager.initSDK('sIMQlfmOXhQmPLF1QMh4aBp8zZO9Lb2A');
```

### Usage 使用方法

    import { MapView, MapTypes, Geolocation, Overlay, MapApp } from 'react-native-baidu-map'

#### MapView Props 属性
| Prop                    | Type  | Default  | Description
| ----------------------- |:-----:| :-------:| -------
| zoomControlsVisible     | bool  | true     | Android only
| trafficEnabled          | bool  | false    |
| baiduHeatMapEnabled     | bool  | false    |
| zoomGesturesEnabled     | bool  | true     | 允许手势缩放
| scrollGesturesEnabled   | bool  | true     | 允许拖动
| mapType                 | number| 1        |
| zoom                    | number| 10       |
| showsUserLocation       | bool  | false    | 是否显示定位
| locationData            | object| null     | 定位信息 {latitude: 0, longitude: 0}
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
    const { Marker, Cluster, Arc, Circle, Polyline, Polygon, Text, InfoWindow } = Overlay;

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

##### Cluster 点聚合

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
| strokeColor             | string| AAFF0000 | 6位(RRGGBB)或8位(AARRGGBB)
| lineWidth               | int| 1|

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

```jsx
<MapView>
    <Marker/>
    <Cluster>
        <Marker/>
    </Cluster>
    <Arc />
    <Circle />
    <Polyline />
    <Polygon />
    <Text />
    <InfoWindow>
        <View />
    </InfoWindow>
</MapView>
```

Marker 示例
```jsx
<MapView>
    <Overlay.Marker rotate={45} icon={{ uri: 'https://mapopen-website-wiki.cdn.bcebos.com/homePage/images/logox1.png' }} location={{ longitude: 113.975453, latitude: 22.510045 }} />
    <Overlay.Marker location={{ longitude: 113.969453, latitude: 22.530045 }} />
</MapView>
```

Cluster 示例

```jsx
<MapView>
    <Cluster>
        <Marker location={{ longitude: 113.969453, latitude: 22.530045 }} />
        <Marker location={{ longitude: 113.968453, latitude: 22.531045 }} />
        <Marker location={{ longitude: 113.967453, latitude: 22.532045 }} />
        <Marker location={{ longitude: 113.966453, latitude: 22.533045 }} />
        <Marker location={{ longitude: 113.965453, latitude: 22.534045 }} />
        <Marker location={{ longitude: 113.965453, latitude: 22.535045 }} />
    </Cluster>
</MapView>
```

#### Geolocation Methods

| Method                    | Description | Result
| ------------------------- | ---------------- | -------
| Promise reverseGeoCode(double lat, double lng) | | `{"address": "", "province": "", "cityCode": "", "city": "", "district": "", "streetName": "", "streetNumber": ""}`
| Promise reverseGeoCodeGPS(double lat, double lng) | |  `{"address": "", "province": "", "cityCode": "", "city": "", "district": "", "streetName": "", "streetNumber": ""}`
| Promise geocode(String city, String addr) | | {"latitude": 0.0, "longitude": 0.0}
| Promise getCurrentPosition(String coorType) | coorType 可为 `bd09ll` 或 `gcj02`，默认 `bd09ll`|`{"latitude": 0.0, "longitude": 0.0, "address": "", "province": "", "cityCode": "", "city": "", "district": "", "streetName": "", "streetNumber": ""}` Android: `{"latitude": 0.0, "longitude": 0.0, "direction": -1, "altitude": 0.0, "radius": 0.0, "address": "", "countryCode": "", "country": "", "province": "", "cityCode": "", "city": "", "district": "", "street": "", "streetNumber": "", "buildingId": "", "buildingName": ""}`
| startLocating(function listener, String coorType) | 开始持续定位 |
| stopLocating  | 停止持续定位 |

#### GetDistance Methods
| Method                    | Result
| ------------------------- | -------
| Promise getLocationDistance({latitude: 0.0, longitude: 0.0}, {latitude: 0.0, longitude: 0.0}) | `{"distance": 0.0}`

#### MapApp Methods 调起百度地图客户端
| Method                    | Description
| ------------------------- | -------
| openDrivingRoute({latitude: 0.0, longitude: 0.0, name: ''}, {latitude: 0.0, longitude: 0.0}, name: '') | 调起百度地图驾车规划
| openTransitRoute({latitude: 0.0, longitude: 0.0, name: ''}, {latitude: 0.0, longitude: 0.0}, name: '') | 调起百度地图公交路线
| openWalkNavi({latitude: 0.0, longitude: 0.0, name: ''}, {latitude: 0.0, longitude: 0.0}, name: '') | 调起百度地图步行路线

##### iOS 
必须在 Info.plist 中进行如下配置，否则不能调起百度地图客户端
```
<key>LSApplicationQueriesSchemes</key>
<array>
    <string>baidumap</string>
</array>
```

### 鸣谢
[![jetbrains](https://raw.githubusercontent.com/lovebing/react-native-baidu-map/master/images/jetbrains.png)](https://www.jetbrains.com/?from=react-native-baidu-map)

