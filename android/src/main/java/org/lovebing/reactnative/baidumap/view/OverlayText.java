/**
 * Copyright (c) 2016-present, lovebing.org.
 *
 * This source code is licensed under the MIT license found in the
 * LICENSE file in the root directory of this source tree.
 */

package org.lovebing.reactnative.baidumap.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.Text;
import com.baidu.mapapi.map.TextOptions;
import com.baidu.mapapi.model.LatLng;

public class OverlayText extends View implements OverlayView {

    private LatLng position;
    private String text;
    private Integer fontSize;
    private Integer fontColor;
    private Integer bgColor;
    private Float rotate;
    private Text textOverlay;

    public OverlayText(Context context) {
        super(context);
    }

    public OverlayText(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public OverlayText(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(21)
    public OverlayText(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public LatLng getPosition() {
        return position;
    }

    public void setPosition(LatLng position) {
        this.position = position;
        if (textOverlay != null) {
            textOverlay.setPosition(position);
        }
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
        if (textOverlay != null) {
            textOverlay.setText(text);
        }
    }

    public Integer getFontSize() {
        return fontSize;
    }

    public void setFontSize(Integer fontSize) {
        this.fontSize = fontSize;
        if (textOverlay != null) {
            textOverlay.setFontSize(fontSize);
        }
    }

    public Integer getFontColor() {
        return fontColor;
    }

    public void setFontColor(Integer fontColor) {
        this.fontColor = fontColor;
        if (textOverlay != null) {
            textOverlay.setFontColor(fontColor);
        }
    }

    public Integer getBgColor() {
        return bgColor;
    }

    public void setBgColor(Integer bgColor) {
        this.bgColor = bgColor;
        if (textOverlay != null) {
            textOverlay.setBgColor(bgColor);
        }
    }

    public Float getRotate() {
        return rotate;
    }

    public void setRotate(Float rotate) {
        this.rotate = rotate;
        if (textOverlay != null) {
            textOverlay.setRotate(rotate);
        }
    }

    @Override
    public void addTopMap(BaiduMap baiduMap) {
        TextOptions option = new TextOptions()
                .text(text)
                .position(position);
        if (fontSize != null) {
            option.fontSize(fontSize);
        }
        if (fontColor != null) {
            option.fontColor(fontColor);
        }
        if (bgColor != null) {
            option.bgColor(bgColor);
        }
        if (rotate != null) {
            option.rotate(rotate);
        }
        textOverlay = (Text) baiduMap.addOverlay(option);
    }

    @Override
    public void remove() {
        if (textOverlay != null) {
            textOverlay.remove();
            textOverlay = null;
        }
    }
}
