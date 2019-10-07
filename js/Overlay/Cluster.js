/**
 * Copyright (c) 2016-present, lovebing.org.
 *
 * This source code is licensed under the MIT license found in the
 * LICENSE file in the root directory of this source tree.
 */

 import {
  requireNativeComponent,
  View,
  NativeModules,
  Platform,
  DeviceEventEmitter
} from 'react-native';

import React, { Component } from 'react';
import PropTypes from 'prop-types';

export default class Cluster extends Component {
  static propTypes = {
    ...View.propTypes
  };

  static defaultProps = {
    location: {
      latitude: 0,
      longitude: 0
    },
    rotate: 0
  };

  constructor() {
    super();
  }

  render() {
    return <BaiduMapOverlayCluster {...this.props} />;
  }
}
const BaiduMapOverlayCluster = requireNativeComponent('BaiduMapOverlayCluster', Cluster);
