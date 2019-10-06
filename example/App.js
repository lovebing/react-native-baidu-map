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
  Dimensions,
} from 'react-native';

import { MapView, MapTypes, Geolocation, Overlay } from 'react-native-baidu-map';

import {
  Header,
  LearnMoreLinks,
  Colors,
  DebugInstructions,
  ReloadInstructions,
} from 'react-native/Libraries/NewAppScreen';

const { height, width } = Dimensions.get('window');

class App extends Component<Props> {
  
  state = {
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
                clusterEnabled={true}
                height={400} 
                zoom={14}
                trafficEnabled={true}
                zoomControlsVisible={true}
                mapType={MapTypes.NORMAL}
                center={{ longitude: 113.960453, latitude: 22.546045 }}
              >
                {this.state.markers.map((marker, index) => <Overlay.Marker key={`marker-` + index} location={marker.location} />)}
              </MapView>
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
  }
});

export default App;
