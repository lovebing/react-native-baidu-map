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

export default class Arc extends Component {
  static propTypes = {
    ...View.propTypes,
    stroke: PropTypes.object,
    points: PropTypes.array,
    dash: PropTypes.bool
  };

  static defaultProps = {
    dash: false,
    stroke: {
      width: 5,
      color: 'AA000000'
    },
    points: [{latitude: 0, longitude: 0}, {latitude: 0, longitude: 0}, {latitude: 0, longitude: 0}]
  };
  
  constructor() {
    super();
  }

  render() {
    return <BaiduMapOverlayArc {...this.props} />;
  }
}
const BaiduMapOverlayArc = requireNativeComponent('BaiduMapOverlayArc', Arc);
