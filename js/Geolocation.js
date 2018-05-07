// @flow

import {
  requireNativeComponent,
  NativeModules,
  Platform,
  NativeEventEmitter
} from "react-native";

import React, { Component, PropTypes } from "react";

const _module = NativeModules.BaiduGeolocationModule;
const geoLocationEmitter = new NativeEventEmitter(_module);

export default {
  geocode(city, addr) {
    return new Promise((resolve, reject) => {
      try {
        _module.geocode(city, addr);
      } catch (e) {
        reject(e);
        return;
      }

      const subscription = geoLocationEmitter.addListener(
        "onGetGeoCodeResult",
        resp => {
          resolve(resp);
          subscription.remove();
        }
      );
    });
  },
  reverseGeoCode(lat, lng) {
    return new Promise((resolve, reject) => {
      try {
        _module.reverseGeoCode(lat, lng);
      } catch (e) {
        reject(e);
        return;
      }

      const subscription = geoLocationEmitter.addListener(
        "onGetReverseGeoCodeResult",
        resp => {
          resolve(resp);
          subscription.remove();
        }
      );
    });
  },
  reverseGeoCodeGPS(lat, lng) {
    return new Promise((resolve, reject) => {
      try {
        _module.reverseGeoCodeGPS(lat, lng);
      } catch (e) {
        reject(e);
        return;
      }

      const subscription = geoLocationEmitter.addListener(
        "onGetReverseGeoCodeResult",
        resp => {
          resp.latitude = parseFloat(resp.latitude);
          resp.longitude = parseFloat(resp.longitude);
          resolve(resp);

          subscription.remove();
        }
      );
    });
  },
  getCurrentPosition() {
    if (Platform.OS == "ios") {
      return new Promise((resolve, reject) => {
        navigator.geolocation.getCurrentPosition(
          position => {
            try {
              _module.reverseGeoCodeGPS(
                position.coords.latitude,
                position.coords.longitude
              );
            } catch (e) {
              reject(e);
              return;
            }

            const subscription = geoLocationEmitter.addListener(
              "onGetReverseGeoCodeResult",
              resp => {
                resp.latitude = parseFloat(resp.latitude);
                resp.longitude = parseFloat(resp.longitude);
                resolve(resp);

                subscription.remove();
              }
            );
          },
          error => {
            reject(error);
          },
          {
            enableHighAccuracy: true,
            timeout: 20000,
            maximumAge: 1000
          }
        );
      });
    }
    return new Promise((resolve, reject) => {
      try {
        _module.getCurrentPosition();
      } catch (e) {
        reject(e);
        return;
      }

      const subscription = geoLocationEmitter.addListener(
        "onGetCurrentLocationPosition",
        resp => {
          resolve(resp);

          subscription.remove();
        }
      );
    });
  }
};
