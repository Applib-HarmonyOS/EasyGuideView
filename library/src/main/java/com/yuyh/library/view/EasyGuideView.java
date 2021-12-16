/*
 * Copyright (C) 2020-21 Application Library Engineering Group
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.yuyh.library.view;

import ohos.agp.components.AttrSet;
import ohos.agp.components.Component.DrawTask;
import ohos.agp.components.Component;
import ohos.agp.components.Component.BindStateChangedListener;
import ohos.agp.components.DependentLayout;
import ohos.agp.render.BlendMode;
import ohos.agp.render.Canvas;
import ohos.agp.render.MaskFilter;
import ohos.agp.render.Paint;
import ohos.agp.render.PixelMapHolder;
import ohos.agp.utils.Color;
import ohos.agp.utils.RectFloat;
import ohos.agp.window.service.DisplayAttributes;
import ohos.agp.window.service.DisplayManager;
import ohos.app.Context;
import ohos.media.image.PixelMap;
import ohos.media.image.common.PixelFormat;
import ohos.media.image.common.Size;

import com.yuyh.library.bean.HighlightArea;
import com.yuyh.library.support.Hshape;
import java.util.List;

/**
 * @author yuyh
 * @date 2016/12/24
 */
public class EasyGuideView extends DependentLayout implements DrawTask, BindStateChangedListener {

    private int mScreenWidth;

    private int mScreenHeight;

    private int mBgColor = 0xaa000000;

    private float mStrokeWidth;

    private Paint mPaint;

    private PixelMap mBitmap;

    private RectFloat mBitmapRect;

    private RectFloat outRect = new RectFloat();

    private Canvas mCanvas;

    private List<HighlightArea> mHighlightList;

    private BlendMode mode;

    private boolean check = true;

    public EasyGuideView(Context context) {
        this(context, null);
        addDrawTask(this);
    }

    public EasyGuideView(Context context, AttrSet attrs) {
        this(context, attrs, 0);
        addDrawTask(this, DrawTask.BETWEEN_BACKGROUND_AND_CONTENT);
    }

    /**
     * Constructor for creating EasyGuideView
     */
    public EasyGuideView(Context context, AttrSet attrs, int defStyleAttr) {
        super(context, attrs, "");
        DisplayAttributes metrics = DisplayManager.getInstance().getDefaultDisplay(context).get().getAttributes();
        mScreenWidth = metrics.width;
        mScreenHeight = metrics.height;
        initView();
        addDrawTask(this, DrawTask.BETWEEN_BACKGROUND_AND_CONTENT);
    }

    private void initView() {
        initPaint();
        mBitmapRect = new RectFloat();
        mode = BlendMode.CLEAR;
        setClickable(true);
    }

    private void initPaint() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        Color hmosColor = EasyGuideView.changeParamToColor(mBgColor);
        mPaint.setColor(hmosColor);
        mPaint.setMaskFilter(new MaskFilter(10, MaskFilter.Blur.INNER));
    }

    private void initCanvas() {
        if (mBitmapRect.getWidth() > 0 && mBitmapRect.getHeight() > 0) {
            PixelMap.InitializationOptions in = new PixelMap.InitializationOptions();
            in.pixelFormat = PixelFormat.ARGB_8888;
            in.size = new Size((int) mBitmapRect.getWidth(), (int) mBitmapRect.getHeight());
            mBitmap = PixelMap.create(in);
        } else {
            PixelMap.InitializationOptions in = new PixelMap.InitializationOptions();
            in.pixelFormat = PixelFormat.ARGB_8888;
            in.size = new Size(10, 10);
            mBitmap = PixelMap.create(in);
        }
        mStrokeWidth = Math.max(Math.max(mBitmapRect.left, mBitmapRect.top),
                Math.max(mScreenWidth - mBitmapRect.right, mScreenHeight - mBitmapRect.bottom));
        outRect.left = mBitmapRect.left - mStrokeWidth / 2;
        outRect.top = mBitmapRect.top - mStrokeWidth / 2;
        outRect.right = mBitmapRect.right + mStrokeWidth / 2;
        outRect.bottom = mBitmapRect.bottom + mStrokeWidth / 2;
        mCanvas = new Canvas();
        mCanvas.drawColor(mBgColor, ohos.agp.render.Canvas.PorterDuffMode.SRC_IN);
    }

    /**
     * 设置高亮区域
     *
     * @param list
     */
    public void setHightLightAreas(List<HighlightArea> list) {
        mHighlightList = list;
        if (list != null && !list.isEmpty()) {
            for (HighlightArea area : list) {
                mBitmapRect.fuse(area.getRectF());
            }
        }
        initCanvas();
    }

    @Override
    public void onDraw(Component component, ohos.agp.render.Canvas canvas) {
        if (mHighlightList != null && mHighlightList.size() > 0) {
            mPaint.setBlendMode(mode);
            mPaint.setStyle(ohos.agp.render.Paint.Style.FILL_STYLE);
            for (HighlightArea area : mHighlightList) {
                RectFloat rectF = area.getRectF();
                rectF.shrink(-mBitmapRect.left, -mBitmapRect.top);
                switch (area.getShape()) {
                    case Hshape.CIRCLE:
                        mCanvas.drawCircle(rectF.getHorizontalCenter(), rectF.getVerticalCenter(),
                                Math.min(area.getHightlightView().getWidth(), area.getHightlightView().getHeight()) / 2, mPaint);
                        break;
                    case Hshape.RECTANGLE:
                        mCanvas.drawRect(rectF, mPaint);
                        break;
                    case Hshape.OVAL:
                        mCanvas.drawOval(rectF, mPaint);
                        break;
                    default:
                        break;
                }
            }
            Paint paint = new Paint();
            paint.setColor(Color.RED);
            PixelMapHolder pixelMapHolder = EasyGuideView.changeParamToPixelMapHolder(mBitmap);
            canvas.drawPixelMapHolder(pixelMapHolder, mBitmapRect.left, mBitmapRect.top, paint);
            mPaint.setBlendMode(null);
            mPaint.setStyle(ohos.agp.render.Paint.Style.STROKE_STYLE);
            mPaint.setStrokeWidth(mStrokeWidth + 0.1f);
            if (check) {
                check = false;
                canvas.drawRect(outRect, mPaint);
            }
        }
    }

    public void recyclerBitmap() {
        if (mBitmap != null) {
            mBitmap.release();
            mBitmap = null;
        }
    }

    @Override
    public void onComponentBoundToWindow(Component component) {
        //Give required implementation
    }

    @Override
    public void onComponentUnboundFromWindow(Component component) {
        recyclerBitmap();
    }

    public static Color changeParamToColor(int color) {
        return new Color(color);
    }

    public static PixelMapHolder changeParamToPixelMapHolder(PixelMap pixelMap) {
        return new PixelMapHolder(pixelMap);
    }
}
