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

const module = NativeModules.BaiduMapAppModule;

export default {
  openBaiduMapTransitRoute(start, end) {
    module.openBaiduMapTransitRoute(start, end);
  }
};
