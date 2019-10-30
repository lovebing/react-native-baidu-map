/**
 * Sample React Native App
 * https://github.com/facebook/react-native
 *
 * @format
 * @flow
 */

import React, {
  Component
} from 'react';

import {
  SafeAreaView,
  StyleSheet,
  ScrollView,
  View,
  Text,
  StatusBar,
  Button,
  Dimensions,
  NativeModules
} from 'react-native';

import { BaiduMapManager, MapView, MapTypes, Geolocation, Overlay, MapApp } from 'react-native-baidu-map';

import {
  Header,
  LearnMoreLinks,
  Colors,
  DebugInstructions,
  ReloadInstructions,
} from 'react-native/Libraries/NewAppScreen';

BaiduMapManager.initSDK("sIMQlfmOXhQmPLF1QMh4aBp8zZO9Lb2A");

const { height, width } = Dimensions.get('window');

class App extends Component<Props> {
  
  state = {
    location: {},
    markers: [
      {
        location: {
          longitude: 113.960453,
          latitude: 22.546045
        }
      },
      {
        location: {
          longitude: 113.961453,
          latitude: 22.547045
        }
      },
      {
        location: {
          longitude: 113.962453, 
          latitude: 22.548045
        }
      },
      {
        location: {
          longitude: 113.963453, 
          latitude: 22.545045
        }
      },
      {
        location: {
          longitude: 113.964453, 
          latitude: 22.544045
        }
      }
    ]
  };

  getCurrentPosition() {
    Geolocation.getCurrentPosition()
      .then((data) => {
        this.setState({ location: data });
      });
  }

  openTransitRoute() {
    var startPoint = {
      longitude: 113.904453, 
      latitude: 22.544045
    };
    var endPoint = {
      longitude: 113.994453, 
      latitude: 22.544045
    };
    MapApp.openTransitRoute(startPoint, endPoint);
  }

  startLocating() {
    Geolocation.startLocating((resp) => {
      console.warn(resp);
    });
  }

  stopLocating() {
    Geolocation.stopLocating();
  }
  
  componentDidMount() {
    
  }

  render() {
    return (
    <>
      <StatusBar barStyle="dark-content" />
        <SafeAreaView>
          <ScrollView
            contentInsetAdjustmentBehavior="automatic"
            style={styles.scrollView}>
            <View style={styles.body}>
              <MapView 
                width={width} 
                height={400} 
                zoom={13}
                trafficEnabled={true}
                zoomControlsVisible={true}
                mapType={MapTypes.NORMAL}
                center={{ longitude: 113.950453, latitude: 22.546045 }}
              >
                <Overlay.Marker rotate={45} icon={{ uri: 'https://mapopen-website-wiki.cdn.bcebos.com/homePage/images/logox1.png' }} location={{ longitude: 113.975453, latitude: 22.510045 }} />
                <Overlay.Cluster>
                  <Overlay.Marker location={{ longitude: 113.969453, latitude: 22.530045 }} />
                  <Overlay.Marker location={{ longitude: 113.968453, latitude: 22.531045 }} />
                  <Overlay.Marker location={{ longitude: 113.967453, latitude: 22.532045 }} />
                  <Overlay.Marker location={{ longitude: 113.966453, latitude: 22.533045 }} />
                  <Overlay.Marker location={{ longitude: 113.965453, latitude: 22.534045 }} />
                  <Overlay.Marker location={{ longitude: 113.965453, latitude: 22.535045 }} />
                </Overlay.Cluster>
                <Overlay.Cluster>
                  {this.state.markers.map((marker, index) => <Overlay.Marker key={`marker-` + index} location={marker.location} />)}
                </Overlay.Cluster>
                <Overlay.Polyline
                  longitude={113.960453}
                  latitude={22.546045}
                  points={[{ longitude: 113.960453, latitude: 22.546145 }, { longitude: 113.961453, latitude: 22.547045 }, { longitude: 113.962453, latitude: 22.54045 }]} />
                <Overlay.Arc
                  longitude={113.960453}
                  latitude={22.546045}
                  points={[{ longitude: 113.960453, latitude: 22.546045 }, { longitude: 113.960453, latitude: 22.546145 }, { longitude: 113.960453, latitude: 22.546245 }]} />
              </MapView>
              <View style={styles.buttonGroup}>
                <View style={styles.button}>
                  <Button onPress={ () => this.getCurrentPosition() } title="Locate Once" />
                </View>
                <View style={styles.button}>
                  <Button onPress={ () => this.openTransitRoute() } title="OpenTransitRoute" />
                </View>
              </View>
              {this.state.location.address ? (
                <View style={styles.location}>
                  <Text>当前位置：{ this.state.location.address }</Text>
                </View>
              ) : null}
            </View>
          </ScrollView>
        </SafeAreaView>
      </>
    );
  }
  
};

const styles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
    backgroundColor: '#F5FCFF',
  },
  scrollView: {

  },
  location: {
    padding: 16,
  },
  buttonGroup: {
    padding: 16,
    flexDirection: 'row'
  },
  button: {
    width: 80,
    margin: 8
  }
});

export default App;
