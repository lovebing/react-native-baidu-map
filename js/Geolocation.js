/**
 * Copyright (c) 2016-present, lovebing.org.
 *
 * This source code is licensed under the MIT license found in the
 * LICENSE file in the root directory of this source tree.
 */

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


const _module = NativeModules.BaiduGeolocationModule;

export default {
  geocode(city, addr) {
    return new Promise((resolve, reject) => {
      try {
        _module.geocode(city, addr);
      }
      catch (e) {
        reject(e);
        return;
      }
      DeviceEventEmitter.once('onGetGeoCodeResult', resp => {
        resolve(resp);
      });
    });
  },
  convertGPSCoor(lat, lng) {
    return _module.convertGPSCoor(lat, lng);
  },
  reverseGeoCode(lat, lng) {
    return new Promise((resolve, reject) => {
      try {
        _module.reverseGeoCode(lat, lng);
      }
      catch (e) {
        reject(e);
        return;
      }
      DeviceEventEmitter.once('onGetReverseGeoCodeResult', resp => {
        resolve(resp);
      });
    });
  },
  reverseGeoCodeGPS(lat, lng) {
    return new Promise((resolve, reject) => {
      try {
        _module.reverseGeoCodeGPS(lat, lng);
      }
      catch (e) {
        reject(e);
        return;
      }
      DeviceEventEmitter.once('onGetReverseGeoCodeResult', resp => {
        resp.latitude = parseFloat(resp.latitude);
        resp.longitude = parseFloat(resp.longitude);
        resolve(resp);
      });
    });
  },
  getCurrentPosition() {
    if (Platform.OS == 'ios') {
      return new Promise((resolve, reject) => {
        navigator.geolocation.getCurrentPosition((position) => {
          try {
            _module.reverseGeoCodeGPS(position.coords.latitude, position.coords.longitude);
          }
          catch (e) {
            reject(e);
            return;
          }
          DeviceEventEmitter.once('onGetReverseGeoCodeResult', resp => {
            resp.latitude = parseFloat(resp.latitude);
            resp.longitude = parseFloat(resp.longitude);
            resolve(resp);
          });
        }, (error) => {
          reject(error);
        }, {
          enableHighAccuracy: true,
          timeout: 20000,
          maximumAge: 1000
        });
      });
    }
    return new Promise((resolve, reject) => {
      try {
        _module.getCurrentPosition();
      }
      catch (e) {
        reject(e);
        return;
      }
      DeviceEventEmitter.once('onGetCurrentLocationPosition', resp => {
        resolve(resp);
      });
    });
  }
};
