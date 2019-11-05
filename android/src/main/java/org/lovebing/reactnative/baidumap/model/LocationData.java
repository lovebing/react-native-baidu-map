/**
 * Copyright (c) 2016-present, lovebing.org.
 *
 * This source code is licensed under the MIT license found in the
 * LICENSE file in the root directory of this source tree.
 */

package org.lovebing.reactnative.baidumap.model;

import com.facebook.react.bridge.ReadableMap;
import org.lovebing.reactnative.baidumap.constant.LocationDataKey;

/**
 * @author lovebing
 * @date 2019/11/02
 */
public class LocationData {

    private Double latitude;
    private Double longitude;
    private Double direction;
    private Double altitude;
    private Double speed;

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getDirection() {
        return direction;
    }

    public void setDirection(Double direction) {
        this.direction = direction;
    }

    public Double getAltitude() {
        return altitude;
    }

    public void setAltitude(Double altitude) {
        this.altitude = altitude;
    }

    public Double getSpeed() {
        return speed;
    }

    public void setSpeed(Double speed) {
        this.speed = speed;
    }

    public boolean isValid() {
        return latitude != null && longitude != null;
    }
}
