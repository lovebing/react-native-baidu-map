import {
    requireNativeComponent,
    PropTypes,
    View,
    NativeModules,
    DeviceEventEmitter
} from 'react-native';

const MapType = {
    GENERAL: 1
};

const _BaiduMapView = {
    name: 'BaiduMapView',
    propTypes: {
        ...View.propTypes,
        showZoomControls: PropTypes.bool,
        mapType: PropTypes.number
    },
    getDefaultProps () {
        return {
            showZoomControls: true,
            mapType: MapType.GENERAL
        }
    }
};


const _BaiduMapModule = NativeModules.BaiduMapModule;

export const BaiduMapView = requireNativeComponent('RCTBaiduMapView', _BaiduMapView);

export const BaiduMapModule = {
    setMarker (lat, lng) {
        _BaiduMapModule.setMarker(lat, lng);
    },
    setMapType (type) {
        _BaiduMapModule.setMapType(type);
    },
    moveToCenter (lat, lng, zoom) {
        _BaiduMapModule.moveToCenter(lat, lng, zoom);
    },
    reverseGeoCode(lat, lng) {
        return new Promise((resolve, reject) => {
            try {
                _BaiduMapModule.reverseGeoCode(lat, lng);
            }
            catch(e) {
                reject(e);
                return;
            }
            DeviceEventEmitter.once('onGetReverseGeoCodeResult', resp => {
                console.warn('onGetReverseGeoCodeResult', 'onGetReverseGeoCodeResult')
                resolve(resp);
            });
        });
    },
    geocode(city, addr) {
        return new Promise((resolve, reject) => {
            try {
               _BaiduMapModule.geocode(city, addr); 
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