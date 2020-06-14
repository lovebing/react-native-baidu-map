/*
 * Copyright (c) 2016-present, lovebing.net.
 *
 * This source code is licensed under the MIT license found in the
 * LICENSE file in the root directory of this source tree.
 */

package org.lovebing.reactnative.baidumap.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Animatable;
import android.net.Uri;
import android.util.AttributeSet;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.baidu.mapapi.clusterutil.clustering.ClusterItem;
import com.baidu.mapapi.map.*;
import com.baidu.mapapi.model.LatLng;
import com.facebook.common.references.CloseableReference;
import com.facebook.datasource.DataSource;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.controller.ControllerListener;
import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.DraweeHolder;
import com.facebook.imagepipeline.core.ImagePipeline;
import com.facebook.imagepipeline.image.CloseableImage;
import com.facebook.imagepipeline.image.CloseableStaticBitmap;
import com.facebook.imagepipeline.image.ImageInfo;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

import org.lovebing.reactnative.baidumap.R;
import org.lovebing.reactnative.baidumap.model.IconInfo;
import org.lovebing.reactnative.baidumap.util.BitmapUtil;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public class OverlayMarker extends ViewGroup implements OverlayView, ClusterItem {

    // TODO 1. 下载中的情况。 2. 清理零引用的 key
    private static final Map<String, BitmapDescriptor> BITMAP_DESCRIPTOR_MAP = new ConcurrentHashMap<>();

    private String title;
    private int titleOffsetY = -100;
    private MarkerOptions.MarkerAnimateType animateType = MarkerOptions.MarkerAnimateType.none;
    private LatLng position;
    private Float rotate;
    private Boolean flat;
    private Boolean perspective;
    private BitmapDescriptor iconBitmapDescriptor;
    private Marker marker;
    private DataSource<CloseableReference<CloseableImage>> dataSource;
    private DraweeHolder<?> imageHolder;
    private IconInfo iconInfo;
    private OverlayInfoWindow overlayInfoWindow;
    private volatile boolean loadingImage = false;
    private InfoWindow titleInfoWindow;
    private View iconView;

    private final ControllerListener<ImageInfo> imageControllerListener =
            new BaseControllerListener<ImageInfo>() {
                @Override
                public void onFinalImageSet(String id, final ImageInfo imageInfo, Animatable animatable) {
                    Log.i("onFinalImageSet", id);
                    CloseableReference<CloseableImage> imageReference = null;
                    try {
                        imageReference = dataSource.getResult();
                        if (imageReference != null) {
                            CloseableImage image = imageReference.get();
                            if (image != null && image instanceof CloseableStaticBitmap) {
                                CloseableStaticBitmap closeableStaticBitmap = (CloseableStaticBitmap) image;
                                Bitmap bitmap = closeableStaticBitmap.getUnderlyingBitmap();
                                if (bitmap != null) {
                                    bitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
                                    iconBitmapDescriptor = BitmapDescriptorFactory.fromBitmap(bitmap);
                                    BITMAP_DESCRIPTOR_MAP.put(iconInfo.getUri(), iconBitmapDescriptor);
                                }
                            }
                        }
                    } finally {
                        dataSource.close();
                        if (imageReference != null) {
                            CloseableReference.closeSafely(imageReference);
                        }
                        loadingImage = false;
                    }
                }
            };

    public OverlayMarker(Context context) {
        super(context);
        init();
    }

    public OverlayMarker(Context context,  AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public OverlayMarker(Context context,  AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public Marker getMarker() {
        return marker;
    }

    protected void init() {
        GenericDraweeHierarchy genericDraweeHierarchy = new GenericDraweeHierarchyBuilder(getResources())
                .setActualImageScaleType(ScalingUtils.ScaleType.FIT_CENTER)
                .setFadeDuration(0)
                .build();
        imageHolder = DraweeHolder.create(genericDraweeHierarchy, getContext());
        imageHolder.onAttach();
    }

    @TargetApi(21)
    public OverlayMarker(Context context,  AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

    }

    public InfoWindow getInfoWindow(LatLng position) {
        if (overlayInfoWindow != null) {
            return overlayInfoWindow.getInfoWindow(position);
        }
        if (title != null && title.length() > 0) {
            if (titleInfoWindow == null) {
                Button button = new Button(getContext());
                button.setVisibility(GONE);
                button.setText(title);
                titleInfoWindow = new InfoWindow(BitmapDescriptorFactory.fromView(button), position, titleOffsetY, new InfoWindow.OnInfoWindowClickListener() {
                    @Override
                    public void onInfoWindowClick() {

                    }
                });
            } else {
                titleInfoWindow.setPosition(position);
            }
            return titleInfoWindow;
        }
        return null;
    }

    public void setOverlayInfoWindow(OverlayInfoWindow overlayInfoWindow) {
        this.overlayInfoWindow = overlayInfoWindow;
    }

    public void setIconView(View iconView) {
        this.iconView = iconView;
        if (marker != null) {
            iconBitmapDescriptor = BitmapUtil.createBitmapDescriptor(iconView);
            marker.setIcon(iconBitmapDescriptor);
        }
    }

    public void setAnimateType(String animateType) {
        if (animateType == null) {
            return;
        }
        switch (animateType) {
            case "drop":
                this.animateType = MarkerOptions.MarkerAnimateType.drop;
                break;
            case "grow":
                this.animateType = MarkerOptions.MarkerAnimateType.grow;
                break;
            case "jump":
                this.animateType = MarkerOptions.MarkerAnimateType.jump;
                break;
            default:
                this.animateType = MarkerOptions.MarkerAnimateType.none;
        }
        if (marker != null) {
            marker.setAnimateType(this.animateType.ordinal());
        }
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setTitleOffsetY(int titleOffsetY) {
        this.titleOffsetY = titleOffsetY;
    }

    @Override
    public LatLng getPosition() {
        return position;
    }

    public void setPosition(LatLng position) {
        this.position = position;
        if (marker != null) {
            marker.setPosition(position);
        }
    }

    public Float getRotate() {
        return rotate;
    }

    public void setRotate(Float rotate) {
        this.rotate = rotate;
        if (marker != null) {
            marker.setRotate(rotate);
        }
    }

    public void setFlat(Boolean flat) {
        this.flat = flat;
        if (marker != null) {
            marker.setFlat(flat);
        }
    }

    public void setPerspective(Boolean perspective) {
        this.perspective = perspective;
        if (marker != null) {
            marker.setPerspective(perspective);
        }
    }

    public void setIcon(IconInfo iconInfo) {
        if (iconInfo.getUri() == null || iconInfo.getUri().length() == 0) {
            return;
        }
        if (BITMAP_DESCRIPTOR_MAP.containsKey(iconInfo.getUri())) {
            iconBitmapDescriptor = BITMAP_DESCRIPTOR_MAP.get(iconInfo.getUri());
            return;
        }
        Log.i("download", iconInfo.getUri());
        this.iconInfo = iconInfo;
        String uri = iconInfo.getUri();
        if (uri == null) {
            iconBitmapDescriptor = null;
        } else if (uri.startsWith("http://") || uri.startsWith("https://") ||
                uri.startsWith("file://") || uri.startsWith("asset://")) {
            loadingImage = true;
            ImageRequest imageRequest = ImageRequestBuilder
                    .newBuilderWithSource(Uri.parse(uri))
                    .build();
            ImagePipeline imagePipeline = Fresco.getImagePipeline();
            dataSource = imagePipeline.fetchDecodedImage(imageRequest, this);
            DraweeController controller = Fresco.newDraweeControllerBuilder()
                    .setImageRequest(imageRequest)
                    .setControllerListener(imageControllerListener)
                    .setOldController(imageHolder.getController())
                    .build();
            imageHolder.setController(controller);
        } else {
            iconBitmapDescriptor = getBitmapDescriptorByName(uri);
        }
    }

    @Override
    public BitmapDescriptor getBitmapDescriptor() {
        BitmapDescriptor result;
        if (iconBitmapDescriptor != null) {
            result = iconBitmapDescriptor;
        } else {
            result = BitmapDescriptorFactory.fromResource(R.mipmap.icon_gcoding);
        }
        if (iconInfo != null
                && iconInfo.getWidth() > 0
                && iconInfo.getHeight() > 0) {
            result = BitmapDescriptorFactory.fromBitmap(BitmapUtil.resizeBitmap(result.getBitmap(),
                    iconInfo.getWidth(), iconInfo.getHeight()));
        }
        return result;
    }

    @Override
    public void addTopMap(BaiduMap baiduMap) {
        if (loadingImage) {
            new Thread(() -> {
                while (loadingImage) {
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        break;
                    }
                }
                addOverlay(baiduMap);
            }).start();
        } else {
            addOverlay(baiduMap);
        }
    }

    @Override
    public void removeFromMap(BaiduMap baiduMap) {
        if (marker != null) {
            marker.remove();
            marker = null;
            overlayInfoWindow = null;
            titleInfoWindow = null;
            iconView = null;
            iconBitmapDescriptor = null;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OverlayMarker that = (OverlayMarker) o;
        return position.latitude == that.position.latitude
                && position.longitude == that.position.longitude;
    }

    @Override
    public int hashCode() {
        return Objects.hash(position.latitude, position.longitude);
    }

    private void addOverlay(BaiduMap baiduMap) {
        if (iconView != null) {
            iconBitmapDescriptor = BitmapUtil.createBitmapDescriptor(iconView);
        }
        MarkerOptions option = new MarkerOptions()
                .position(position)
                .alpha(getAlpha())
                .animateType(animateType)
                .icon(getBitmapDescriptor());
        if (rotate != null) {
            option.rotate(rotate);
        }
        if (flat != null) {
            option.flat(flat);
        }
        if (perspective != null) {
            option.perspective(perspective);
        }
        marker = (Marker) baiduMap.addOverlay(option);
    }

    private int getDrawableResourceByName(String name) {
        return getResources().getIdentifier(
                name,
                "drawable",
                getContext().getPackageName());
    }

    private BitmapDescriptor getBitmapDescriptorByName(String name) {
        return BitmapDescriptorFactory.fromResource(getDrawableResourceByName(name));
    }
}
