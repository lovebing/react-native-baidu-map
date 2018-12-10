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

export default class OverlayText extends Component {
  static propTypes = {
    ...View.propTypes,
    text: PropTypes.string,
    fontSize: PropTypes.number,
    fontColor: PropTypes.string,
    bgColor: PropTypes.number,
    rotate: PropTypes.number,
    location: PropTypes.object
  };

  static defaultProps = {
    location: {
      latitude: 0,
      longitude: 0
    }
  };

  constructor() {
    super();
  }

  render() {
    if (Platform.OS === 'ios') {
      return <View {...this.props} />;
    }
    return <BaiduMapOverlayText {...this.props} />;
  }
}
const BaiduMapOverlayText = requireNativeComponent('BaiduMapOverlayText', OverlayText);