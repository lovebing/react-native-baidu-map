import {
  requireNativeComponent,
  NativeModules,
  Platform,
  DeviceEventEmitter
} from 'react-native';

import React, {
  Component,
  PropTypes
} from 'react';

import Geolocation from './Geolocation';

const _module = NativeModules.BaiduMapModule;

export default {
  setMarker (lat, lng) {
    console.warn('This method was deprecated, please use MapView prop instead');
  },
  setMapType (type) {
    console.warn('This method was deprecated, please use MapView prop instead');
  },
  setZoom (zoom) {
    console.warn('This method was deprecated, please use MapView prop instead');
  },
  moveToCenter (lat, lng, zoom) {
    console.warn('This method was deprecated, please use MapView prop instead');
  },
  geocode(city, addr) {
    console.warn('This method was deprecated, please use Geolocation.geocode instead');
    return Geolocation.geocode(city, addr);
  },
  reverseGeoCode(lat, lng) {
    console.warn('This method was deprecated, please use Geolocation.reverseGeoCode instead');
    return Geolocation.reverseGeoCode(lat, lng);
  },
  reverseGeoCodeGPS(lat, lng) {
    console.warn('This method was deprecated, please use Geolocation.reverseGeoCodeGPS instead');
    return Geolocation.reverseGeoCodeGPS(lat, lng);
  },
  getCurrentPosition() {
    console.warn('This method was deprecated, please use Geolocation.getCurrentPosition instead');
    return Geolocation.getCurrentPosition();
  }
};