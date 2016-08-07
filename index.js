import {
    requireNativeComponent,
    View,
    NativeModules,
    DeviceEventEmitter
} from 'react-native';

import React, {
    Component,
    PropTypes
} from 'react';


export const MapTypes = {
    NORMAL: 1,
    SATELLITE: 2
};

export class MapView extends Component {
    static propTypes = {
        ...View.propTypes,
        zoomControlsVisible: PropTypes.bool,
        mapType: PropTypes.number,
        zoom: PropTypes.number,
        onMapStatusChangeStart: PropTypes.func,
        onMapStatusChange: PropTypes.func,
        onMapStatusChangeFinish: PropTypes.func,
        onMapLoaded: PropTypes.func,
        onMapClick: PropTypes.func,
        onMapDoubleClick: PropTypes.func,
        onMarkerClick: PropTypes.func
    };

    static defaultProps = {
        zoomControlsVisible: true,
        mapType: MapTypes.NORMAL
    };

    constructor() {
        super();
    }

    _onChange(event) {
        if(typeof this.props[event.nativeEvent.type] === 'function') {
            this.props[event.nativeEvent.type](event.nativeEvent.params);
        }
    }

    componentDidMount() {
    }

    componentWillUnmount() {
    }

    render() {
        return <BaiduMapView {...this.props} onChange={this._onChange.bind(this)} />;
    }
}

const BaiduMapView = requireNativeComponent('RCTBaiduMapView', MapView, {
    nativeOnly: {onChange: true}
});

const _module = NativeModules.BaiduMapModule;


export const MapModule = {
    setMarker (lat, lng) {
        _module.setMarker(lat, lng);
    },
    setMapType (type) {
        _module.setMapType(type);
    },
    setZoom (zoom) {
        _module.setZoom(zoom);
    },
    moveToCenter (lat, lng, zoom) {
        _module.moveToCenter(lat, lng, zoom);
    },
    reverseGeoCode(lat, lng) {
        return new Promise((resolve, reject) => {
            try {
                _module.reverseGeoCode(lat, lng);
            }
            catch(e) {
                reject(e);
                return;
            }
            DeviceEventEmitter.once('onGetReverseGeoCodeResult', resp => {
                resolve(resp);
            });
        });
    },
    reverseGeoCodeGPS(lat, lng) {
        return new Promise((resolve, reject) => {
            try {
                _module.reverseGeoCodeGPS(lat, lng);
            }
            catch(e) {
                reject(e);
                return;
            }
            DeviceEventEmitter.once('onGetReverseGeoCodeResult', resp => {
                resolve(resp);
            });
        });
    },
    geocode(city, addr) {
        return new Promise((resolve, reject) => {
            try {
               _module.geocode(city, addr);
            }
            catch(e) {
                reject(e);
                return;
            }
            DeviceEventEmitter.once('onGetGeoCodeResult', resp => {
                resolve(resp);
            });
        });
    }
};
