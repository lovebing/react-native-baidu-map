# react-native-baidu-map [![npm version](https://img.shields.io/npm/v/react-native-baidu-map.svg?style=flat)](https://www.npmjs.com/package/react-native-baidu-map)

Baidu Map SDK modules and views for React Native(Android & IOS), support react native 0.61.2+

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
- 显示定位
- 添加一些常用的方法

#### iOS
- 完善 Marker
- 显示定位
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
cp -R package.json js index.js ios android LICENSE README.md example/node_modules/react-native-baidu-map/
rm -rf example/node_modules/react-native-baidu-map/ios/RCTBaiduMap.xcodeproj

```
#### 使用 npm 源
npm install react-native-baidu-map --save

### 原生模块导入
`react-native link react-native-baidu-map`

如果 iOS 项目包含 Podfile，会自动加上 react-native-baidu-map 的依赖，只需要执行 `pod install`，不需要做其它处理。
如果没有 Podfile，则需要手动导入百度地图和定位 SDK 的依赖，参考 http://lbsyun.baidu.com/index.php?title=iossdk/guide/create-project/oc
和 http://lbsyun.baidu.com/index.php?title=ios-locsdk/guide/create-project/manual-create


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

| Method                    | Result
| ------------------------- | -------
| Promise reverseGeoCode(double lat, double lng) | `{"address": "", "province": "", "cityCode": "", "city": "", "district": "", "streetName": "", "streetNumber": ""}`
| Promise reverseGeoCodeGPS(double lat, double lng) |  `{"address": "", "province": "", "cityCode": "", "city": "", "district": "", "streetName": "", "streetNumber": ""}`
| Promise geocode(String city, String addr) | {"latitude": 0.0, "longitude": 0.0}
| Promise getCurrentPosition() | iOS: `{"latitude": 0.0, "longitude": 0.0, "address": "", "province": "", "cityCode": "", "city": "", "district": "", "streetName": "", "streetNumber": ""}` Android: `{"latitude": 0.0, "longitude": 0.0, "direction": -1, "altitude": 0.0, "radius": 0.0, "address": "", "countryCode": "", "country": "", "province": "", "cityCode": "", "city": "", "district": "", "street": "", "streetNumber": "", "buildingId": "", "buildingName": ""}`

#### GetDistance Methods
| Method                    | Result
| ------------------------- | -------
| Promise getLocationDistance([double lat, double lng], [double lat, double lng]) | `{"distance": 0}`
