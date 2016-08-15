/**
 * @author lovebing
 */

import React, {
  Component,
  PropTypes
} from 'react';

import {
  MapView,
  MapTypes,
  MapModule
} from './react-native-baidu-map.js';

import {
  AppRegistry,
  StyleSheet,
  Text,
  View,
  TouchableHighlight
} from 'react-native';

import Dimensions from 'Dimensions';

class Buttton extends Component {
  static propTypes = {
    label: PropTypes.string,
    onPress: PropTypes.func
  };

  static defaultProps = {
    label: 'Buttton',
    onPress() {

    }
  };
  render() {
    return (
      <TouchableHighlight 
        style={styles.btn}
        onPress={this.props.onPress}>
        <Text style={{color: 'white'}}>{this.props.label}</Text>
      </TouchableHighlight>
    );
  }
};

export default class BaiduMapDemo extends Component {

  constructor() {
    super();

    this.zoom = 10;
  }

  componentDidMount() {
  }

  render() {
    return (
      <View style={styles.container}>
        <MapView 
          zoom={this.zoom}
          style={styles.map}
          onMapClick={(e) => {
            
          }}
        />
        <View style={styles.row}>
          <Buttton label="普通地图" onPress={() => {
            MapModule.setMapType(MapTypes.NORMAL);
          }} />
          <Buttton label="卫星地图" onPress={() => {
            MapModule.setMapType(MapTypes.SATELLITE);
          }} />
        </View>

        <Buttton label="定位" onPress={() => {
          MapModule.getCurrentPosition()
            .then(data => {
              MapModule.moveToCenter(data.latitude, data.longitude, 15);
              MapModule.setMarker(data.latitude, data.longitude);
            })
            .catch(e =>{
              console.warn(e)
            })
        }} />
        <View style={styles.row}>
          <Buttton label="放大" onPress={() => {
            this.zoom++;
            MapModule.setZoom(this.zoom);
          }} />
          <Buttton label="缩小" onPress={() => {
            if(this.zoom > 0) {
              this.zoom--;
              MapModule.setZoom(this.zoom);
            }
            
          }} />
        </View>


      </View>
    );
  }
}

const styles = StyleSheet.create({
  row: {
    flexDirection: 'row'
  },
  btn: {
    height: 48,
    width: 120,
    borderRadius: 4,
    alignItems: 'center',
    justifyContent: 'center',
    backgroundColor: '#cccccc',
    margin: 4
  },
  container: {
    flex: 1,
    justifyContent: 'flex-start',
    alignItems: 'center',
    backgroundColor: '#F5FCFF',
  },
  map: {
    width: Dimensions.get('window').width,
    height: Dimensions.get('window').height - 200,
    marginBottom: 16
  }
});
