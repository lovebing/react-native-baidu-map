/**
 * Sample React Native App
 * https://github.com/facebook/react-native
 *
 * @format
 * @flow
 * @lint-ignore-every XPLATJSCOPYRIGHT1
 */

import React, {Component} from 'react';
import {Platform, StyleSheet, Text, View, Dimensions, Button} from 'react-native';
import { MapView, MapTypes, Geolocation, Overlay } from 'react-native-baidu-map';
const {height, width} = Dimensions.get('window');

type Props = {};
export default class App extends Component<Props> {
  state = {
    infoWindowProps: {
      location: {
        longitude: 0,
        latitude: 0
      },
      visible: false,
      title: ''
    }
  };

  render() {
    const { infoWindowProps } = this.state;
    return (
      <View style={styles.container}>
        <MapView 
          width={width} 
          height={400} 
          zoom={18}
          center={{ longitude: 113.960453, latitude: 22.546045 }}
          onMarkerClick={(e) => {
            this.setState({
              infoWindowProps: {
                title: e.title,
                location: e.position,
                visible: !infoWindowProps.visible
              }
            });
          }}>
          <Overlay.Marker
            title="高新园地铁站"
            rotate={50}
            location={{ longitude: 113.960453, latitude: 22.546045 }} />
          <Overlay.InfoWindow 
            title={infoWindowProps.title} 
            location={infoWindowProps.location} 
            visible={infoWindowProps.visible} />
          
        </MapView>
      </View>
    );
  }
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
    backgroundColor: '#F5FCFF',
  }
});
