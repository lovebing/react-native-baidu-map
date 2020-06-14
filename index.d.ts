import { Component } from "react";
import { ImageSourcePropType, ViewProps } from "react-native";

/**
 * Baidu Map SDK modules and views for React Native(Android & iOS), support react native 0.61+.
 */
declare namespace ReactNativeBaiduMap {
  /**
   * 坐标对象
   */
  export interface Location {
    /**
     * 纬度
     */
    latitude: number;

    /**
     * 纬度
     */
    longitude: number;
  }

  export interface Stroke {
    width: number;
    color: string;
  }

  /**
   * 颜色渐变对象
   */
  export interface Gradient {
    colors: Array<string>;
    startPoints: Array<number>
  }

  type CommonCallBack<T> = (l: T) => void;

  /**
   * onMapStatusChange 回调事件参数
   */
  export interface onMapStatusChangeEvent {
    overlook: number;
    target: Location & {
      zoom: number;
    };
  }

  /**
   * onMarkerClick 回调事件参数
   */
  export interface onMarkerClickEvent {
    position: Location;
    title?: string;
  }

  export interface MapViewProps extends ViewProps {
    /**
     * @default true
     * @supported Android only
     */
    zoomControlsVisible?: boolean;

    /**
     * @default false
     */
    trafficEnabled?: boolean;

    /**
     * @default false
     */
    baiduHeatMapEnabled?: boolean;

    /**
     * 允许手势缩放
     * @default true
     */
    zoomGesturesEnabled?: boolean;

    /**
     * 允许拖动
     * @default true
     */
    scrollGesturesEnabled?: boolean;

    /**
     * NONE: 0; NORMAL: 1; SATELLITE: 2;
     * @default 1
     */
    mapType?: 0 | 1 | 2;

    /**
     * @default 10
     */
    zoom?: number;

    /**
     * 是否显示定位
     * @default false
     */
    showsUserLocation?: boolean;

    /**
     * 定位信息 {latitude: 0, longitude: 0}
     * @default null
     */
    locationData?: Location;

    /**
     * 地图默认中心点
     * @default null
     */
    center?: Location;

    /**
     * @default undefined
     * @supported Android only
     */
    onMapStatusChangeStart?: Function;

    /**
     * @default undefined
     */
    onMapStatusChange?: CommonCallBack<onMapStatusChangeEvent>;

    /**
     * @default undefined
     * @supported Android only
     */
    onMapStatusChangeFinish?: Function;

    /**
     * @default undefined
     */
    onMapLoaded?: Function;

    /**
     * @default undefined
     */
    onMapClick?: CommonCallBack<Location>;

    /**
     * @default undefined
     */
    onMapDoubleClick?: Function;

    /**
     * @default undefined
     */
    onMarkerClick?: CommonCallBack<onMarkerClickEvent>;

    /**
     * @default undefined
     */
    onMapPoiClick?: Function;
  }

  export class MapView extends Component<MapViewProps> {}

  export namespace Overlay {
    export interface MarkerProps extends ViewProps {
      /**
       * @default undefined
       */
      title?: string;

      /**
       * @default {latitude: 0, longitude: 0}
       */
      location: Location;

      /**
       * @default null
       */
      perspective?: boolean;

      /**
       * @default null
       */
      flat?: boolean;

      /**
       * @default 0
       */
      rotate?: number;

      /**
       * icon 图片，同 `Image` 的 source 属性
       * @default null
       */
      icon?: ImageSourcePropType;

      /**
       * 动画效果：drop/grow/jump (iOS 仅支持 drop)
       */
      animateType?: string;

      /**
       * @default 0
       */
      alpha?: number;
    }

    export class Marker extends Component<MarkerProps> {}

    export interface ClusterProps extends ViewProps {}

    export class Cluster extends Component<ClusterProps> {}

    export interface ArcProps extends ViewProps {
      /**
       * @default {width: 5, color: 'AA000000'}
       */
      stroke: Stroke;

      /**
       * 数值长度必须为 3
       */
      points: [Location, Location, Location];

      /**
       * 是否为虚线，仅 iOS
       */
      dash: boolean;
    }

    export class Arc extends Component<ArcProps> {
      /**
       * @default 1400
       */
      radius?: number;

      /**
       * @default '000000FF'
       */
      fillColor?: string;

      /**
       * @default {width: 5, color: 'AA000000'}
       */
      stroke?: {
        width: number;
        color: string;
      };

      center: Location;
    }

    export interface CircleProps extends ViewProps {
      radius: number;
      fillColor: string;
      stroke: Stroke;
      center: Location;
    }

    export class Circle extends Component<CircleProps> {}

    export interface PolylineProps extends ViewProps {
      points: Location[];
      stroke: Stroke;
    }

    export class Polyline extends Component<PolylineProps> {}

    export interface PolygonProps extends ViewProps {
      points: Location[];

      /**
       * 8位(AARRGGBB)
       * @default 'AAFFFF00'
       */
      fillColor?: string;

      /**
       * color: 8位(AARRGGBB)
       * @default {width: 5, color: 'AA00FF00'}
       */
      stroke?: { width: number; color: string };
    }

    export class Polygon extends Component<PolygonProps> {}

    export interface TextProps extends ViewProps {
      text: string;

      fontSize?: number;

      fontColor?: string;

      bgColor?: string;

      rotate?: number;

      location: Location;
    }

    export class Text extends Component<TextProps> {}

    export interface InfoWindowProps extends ViewProps {
      /**
       * 相对于 point 在 y 轴的偏移量，仅 Android
       */
      offsetY: number;
    }

    export class InfoWindow extends Component<InfoWindowProps> {}

    export interface HeatMapProps extends ViewProps {
      points: Array<Location>;
      gradient: Gradient;
    }

    /**
     * 自定义热力图
     */
    export class HeatMap extends Component<HeatMapProps> {}
  }

  export namespace Geolocation {
    export type CoorType = "bd09ll" | "gcj02";

    /**
     * 经纬度查询地址详情
     * @param lat 纬度
     * @param lng 经度
     */
    export function reverseGeoCode(
      lat: number,
      lng: number
    ): Promise<{
      address: string;
      province: string;
      cityCode: string;
      city: string;
      district: string;
      streetName: string;
      streetNumber: string;
    }>;

    /**
     * GPS 的经纬度查询地址详情
     * @param lat 纬度
     * @param lng 经度
     */
    export function reverseGeoCodeGPS(
      lat: number,
      lng: number
    ): Promise<{
      address: string;
      province: string;
      cityCode: string;
      city: string;
      district: string;
      streetName: string;
      streetNumber: string;
    }>;

    /**
     * 地址转换为坐标
     * @param city 城市
     * @param addr 地址
     */
    export function geocode(city: string, addr: string): Promise<Location>;

    /**
     * 获取当前定位信息
     * @param coorType 定位类型
     */
    export function getCurrentPosition(
      coorType?: CoorType
    ): Promise<{
      latitude: number;
      longitude: number;
      address: string;
      province: string;
      cityCode: string;
      city: string;
      district: string;
      streetName: string;
      streetNumber: string;
    }>;

    /**
     * 开始持续定位
     * @param listener 持续定位触发的监听函数
     * @param coorType 定位类型
     * @exports
     * startLocating(() => { console.log() }, 'bd09ll');
     */
    export function startLocating(listener: Function, coorType: CoorType): void;

    /**
     * 停止持续定位
     */
    export function stopLocating(): void;
  }

  /**
   * 两个坐标的距离
   */
  export namespace GetDistance {
    /**
     * 计算两个坐标的距离
     * @param l1 坐标1
     * @param l2 坐标2
     * @example
     * const d = getLocationDistance({latitude: 0.0, longitude: 0.0}, {latitude: 0.0, longitude: 0.0});
     * console.log(d.distance);
     */
    export function getLocationDistance(
      l1: Location,
      l2: Location
    ): Promise<{
      distance: number;
    }>;
  }

  /**
   * 调起百度地图客户端 iOS：必须在 Info.plist 中进行如下配置，否则不能调起百度地图客户端
   * ```
   * <key>LSApplicationQueriesSchemes</key>
   * <array>
   *  <string>baidumap</string>
   * </array>
   * ```
   */
  export namespace MapApp {
    export interface OpenLocation extends Location {
      name: string;
    }

    /**
     * 调起百度地图驾车规划
     * @param sl 起点坐标
     * @param el 终点坐标
     * @example openDrivingRoute({latitude: 0.0, longitude: 0.0, name: ''}, {latitude: 0.0, longitude: 0.0, name: ''})
     */
    export function openDrivingRoute(sl: OpenLocation, el: OpenLocation): void;

    /**
     * 调起百度地图公交路线
     * @param sl 起点坐标
     * @param el 终点坐标
     * @example openTransitRoute({latitude: 0.0, longitude: 0.0, name: ''}, {latitude: 0.0, longitude: 0.0, name: ''})
     */
    export function openTransitRoute(sl: OpenLocation, el: OpenLocation): void;

    /**
     * 调起百度地图步行路线
     * @param sl 起点坐标
     * @param el 终点坐标
     * @example openWalkNavi({latitude: 0.0, longitude: 0.0, name: ''}, {latitude: 0.0, longitude: 0.0, name: ''})
     */
    export function openWalkNavi(sl: OpenLocation, el: OpenLocation): void;
  }

  export namespace BaiduMapManager {
    /**
     * iOS 初始化 SDK
     * @param appKey
     */
    export function initSDK(appKey: string): void;

    /**
     * 是否有定位权限
     */
    export function hasLocationPermission(): Promise<boolean>;
  }
}

export = ReactNativeBaiduMap;
