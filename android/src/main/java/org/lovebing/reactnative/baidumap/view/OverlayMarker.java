/**
 * Copyright (c) 2016-present, lovebing.org.
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
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import android.view.View;
import android.view.ViewGroup;
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

public class OverlayMarker extends View implements OverlayView {

    private String title;
    private LatLng position;
    private Float rotate;
    private Boolean flat;
    private Boolean perspective;
    private BitmapDescriptor iconBitmapDescriptor;
    private Marker marker;
    private DataSource<CloseableReference<CloseableImage>> dataSource;
    private DraweeHolder<?> imageHolder;
    private volatile boolean loadingImage = false;

    private final ControllerListener<ImageInfo> imageControllerListener =
            new BaseControllerListener<ImageInfo>() {
                @Override
                public void onFinalImageSet(
                        String id,
                        @Nullable final ImageInfo imageInfo,
                        @Nullable Animatable animatable) {
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

    public OverlayMarker(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public OverlayMarker(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
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
    public OverlayMarker(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public LatLng getPosition() {
        return position;
    }

    public void setPosition(LatLng position) {
        this.position = position;
        if (marker != null) {
            marker.setPosition(position);
        }
    }

    public BitmapDescriptor getIconBitmapDescriptor() {
        return iconBitmapDescriptor;
    }

    public void setIconBitmapDescriptor(BitmapDescriptor iconBitmapDescriptor) {
        this.iconBitmapDescriptor = iconBitmapDescriptor;
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

    public Boolean getFlat() {
        return flat;
    }

    public void setFlat(Boolean flat) {
        this.flat = flat;
        if (marker != null) {
            marker.setFlat(flat);
        }
    }

    public Boolean getPerspective() {
        return perspective;
    }

    public void setPerspective(Boolean perspective) {
        this.perspective = perspective;
        if (marker != null) {
            marker.setPerspective(perspective);
        }
    }

    public void setIcon(String uri) {
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
    public void remove() {
        if (marker != null) {
            marker.remove();
            marker = null;
        }
    }

    private void addOverlay(BaiduMap baiduMap) {
        BitmapDescriptor icon;
        if (getIconBitmapDescriptor() != null) {
            icon = getIconBitmapDescriptor();
        } else {
            icon = BitmapDescriptorFactory.fromResource(R.mipmap.icon_gcoding);
        }
        MarkerOptions option = new MarkerOptions()
                .position(position)
                .title(getTitle())
                .alpha(getAlpha())
                .icon(icon);
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
