// Created by Riant 2019/10/22

import {
  NativeModules,
} from 'react-native'

const _module = NativeModules.BaiduGetDistanceModule;

export default {
  getLocationDistance([lat1, lng1], [lat2, lng2]) {
    return _module.getLocationDistance(lat1, lng1, lat2, lng2)
  },
}
