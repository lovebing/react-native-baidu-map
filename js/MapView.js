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
import MapTypes from './MapTypes';
import Overlay from './Overlay';

export default class MapView extends Component {
  static propTypes = {
    ...View.propTypes,
    zoomControlsVisible: PropTypes.bool,
    trafficEnabled: PropTypes.bool,
    baiduHeatMapEnabled: PropTypes.bool,
    clusterEnabled: PropTypes.bool,
    mapType: PropTypes.number,
    zoom: PropTypes.number,
    showsUserLocation: PropTypes.bool,
    scrollGesturesEnabled: PropTypes.bool, //是否允许拖动
    zoomGesturesEnabled: PropTypes.bool,//是否充许手势缩放
    center: PropTypes.object,
    locationData: PropTypes.object,
    onMapStatusChangeStart: PropTypes.func,
    onMapStatusChange: PropTypes.func,
    onMapStatusChangeFinish: PropTypes.func,
    onMapLoaded: PropTypes.func,
    onMapClick: PropTypes.func,
    onMapDoubleClick: PropTypes.func,
    onMarkerClick: PropTypes.func,
    onMapPoiClick: PropTypes.func
  };

  static defaultProps = {
    zoomControlsVisible: true,
    trafficEnabled: false,
    baiduHeatMapEnabled: false,
    mapType: MapTypes.NORMAL,
    center: null,
    zoom: 10,
    scrollGesturesEnabled: true,
    zoomGesturesEnabled: true,
    showsUserLocation: false
  };

  constructor() {
    super();
  }

  _onChange(event) {
    if (typeof this.props[event.nativeEvent.type] === 'function') {
      this.props[event.nativeEvent.type](event.nativeEvent.params);
    }
  }

  render() {
    let childrenCount = 0;
    if (this.props.children && this.props.children.length) {
      for (let i = 0; i < this.props.children.length; i++) {
        const child = this.props.children[i];
        if (child.length) {
          childrenCount += child.length;
        } else {
          childrenCount++;
        }
      }
    }
    return <BaiduMapView {...this.props} childrenCount={childrenCount} onChange={this._onChange.bind(this)}/>;
  }
  
}

const BaiduMapView = requireNativeComponent('BaiduMapView', MapView, {
  nativeOnly: {onChange: true}
});
