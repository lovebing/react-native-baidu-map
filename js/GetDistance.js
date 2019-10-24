// Created by Riant 2019/10/22

import {
  NativeModules,
} from 'react-native'

const _module = NativeModules.BaiduGetDistanceModule;

export default {
  getLocationDistance({latitude, longitude}, {latitude: latitude2, longitude: longitude2}) {
    return _module.getLocationDistance(latitude, longitude, latitude2, longitude2)
  },
}
