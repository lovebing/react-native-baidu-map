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


const _module = NativeModules.RouteModule;

export default {
  routePlan(name,startCity,endName,endCity,type) {
    return new Promise((resolve, reject) => {
      try {
        _module.onRoutePlan(name,startCity,endName,endCity,type);
      }
      catch (e) {
        reject(e);
        return;
      }
      DeviceEventEmitter.once('onGetPlanSearch', resp => {
        resolve(resp);
      });
    });
  },
  routePlanByCoor(startPoint,endPoint,type) {
    return new Promise((resolve, reject) => {
      try {
        _module.onRoutPlanByCoordinate(startPoint,endPoint,type);
      }
      catch (e) {
        reject(e);
        return;
      }
      DeviceEventEmitter.once('onGetPlanSearch', resp => {
        resolve(resp);
      });
    });
  },

};
