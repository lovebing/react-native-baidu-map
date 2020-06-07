/**
 * Copyright (c) 2016-present, lovebing.org.
 *
 * This source code is licensed under the MIT license found in the
 * LICENSE file in the root directory of this source tree.
 */

import {
  requireNativeComponent,
  View
} from 'react-native';

import React, { Component } from 'react';
import PropTypes from 'prop-types';

export default class MarkerIcon extends Component {
  static propTypes = {
    ...View.propTypes
  };

  constructor() {
    super();
  }

  render() {
    return <BaiduMapOverlayMarkerIcon {...this.props} />;
  }
}
const BaiduMapOverlayMarkerIcon = requireNativeComponent('BaiduMapOverlayMarkerIcon', MarkerIcon);