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
  Geolocation
} from 'react-native-baidu-map';

import {
  Button,
  AppRegistry,
  StyleSheet,
  Text,
  View,
  TouchableHighlight
} from 'react-native';

import Dimensions from 'Dimensions';

export default class BaiduMapDemo extends Component {

  constructor() {
    super();

    this.state = {
      mayType: MapTypes.NORMAL,
      zoom: 15,
      center: {
        longitude: 113.981718,
        latitude: 22.542449
      },
      trafficEnabled: false,
      baiduHeatMapEnabled: false,
      markers: [{
        longitude: 113.981718,
        latitude: 22.542449,
        title: "Window of the world"
      },{
        longitude: 113.995516,
        latitude: 22.537642,
        title: ""
      }]
    };
  }

  componentDidMount() {
  }

  render() {
    return (
      <View style={styles.container}>
        <MapView 
          trafficEnabled={this.state.trafficEnabled}
          baiduHeatMapEnabled={this.state.baiduHeatMapEnabled}
          zoom={this.state.zoom}
          mapType={this.state.mapType}
          center={this.state.center}
          marker={this.state.marker}
          markers={this.state.markers}
          style={styles.map}
          onMarkerClick={(e) => {
            console.warn(JSON.stringify(e));
          }}
          onMapClick={(e) => {
          }}
        >
        </MapView>

        <View style={styles.row}>
          <Button title="Normal" onPress={() => {
            this.setState({
              mapType: MapTypes.NORMAL
            });
          }} />
          <Button style={styles.btn} title="Satellite" onPress={() => {
            this.setState({
              mapType: MapTypes.SATELLITE
            });
          }} />

          <Button style={styles.btn} title="Locate" onPress={() => {
            console.warn('center', this.state.center);
            Geolocation.getCurrentPosition()
              .then(data => {
                console.warn(JSON.stringify(data));
                this.setState({
                  zoom: 15,
                  marker: {
                    latitude: data.latitude,
                    longitude: data.longitude,
                    title: 'Your location'
                  },
                  center: {
                    latitude: data.latitude,
                    longitude: data.longitude,
                    rand: Math.random()
                  }
                });
              })
              .catch(e =>{
                console.warn(e, 'error');
              })
          }} />
        </View>

        <View style={styles.row}>
          <Button title="Zoom+" onPress={() => {
            this.setState({
              zoom: this.state.zoom + 1
            });
          }} />
          <Button title="Zoom-" onPress={() => {
            if(this.state.zoom > 0) {
              this.setState({
                zoom: this.state.zoom - 1
              });
            }
            
          }} />
        </View>

        <View style={styles.row}>
          <Button title="Traffic" onPress={() => {
            this.setState({
              trafficEnabled: !this.state.trafficEnabled
            });
          }} />

          <Button title="Baidu HeatMap" onPress={() => {
            this.setState({
              baiduHeatMapEnabled: !this.state.baiduHeatMapEnabled
            });
          }} />
        </View>
      </View>
    );
  }
}

const styles = StyleSheet.create({
  row: {
    flexDirection: 'row',
    height: 40
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
