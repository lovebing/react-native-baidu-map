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

export default class MapView extends Component {
  static propTypes = {
    ...View.propTypes,
    zoomControlsVisible: PropTypes.bool,
    trafficEnabled: PropTypes.bool,
    baiduHeatMapEnabled: PropTypes.bool,
    mapType: PropTypes.number,
    zoom: PropTypes.number,
    center: PropTypes.object,
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
    zoom: 10
  };

  constructor() {
    super();
  }

  _onChange(event) {
    if (typeof this.props[event.nativeEvent.type] === 'function') {
      this.props[event.nativeEvent.type](event.nativeEvent.params);
    }
  }

  renderIOS() {
    const children = this.props.children ? this.props.children : [];
    const markerMap = {};
    for (let i = 0; i < children.length; i++) {
      for (let p in children[i]) {
        if (children[i].type === Overlay.Marker) {
          const props = children[i].props;
          markerMap[props.location.latitude + ":" + props.location.longitude] = {
            title: props.title,
            latitude: props.location.latitude,
            longitude: props.location.longitude
          };
        }
      }
    }
    const markers = [];
    for (let p in markerMap) {
      markers.push(markerMap[p]);
    }
    return <BaiduMapView {...this.props} children={[]} markers={markers} onChange={this._onChange.bind(this)}/>;
  }

  renderAndroid() {
    return <BaiduMapView {...this.props} onChange={this._onChange.bind(this)}/>;
  }

  render() {
    if (Platform.OS === 'ios') {
      return this.renderIOS();
    }
    return this.renderAndroid();
  }
}

const BaiduMapView = requireNativeComponent('BaiduMapView', MapView, {
  nativeOnly: {onChange: true}
});
