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

package com.yuyh.library;

import com.yuyh.library.bean.Confirm;
import com.yuyh.library.bean.HighlightArea;
import com.yuyh.library.bean.Message;
import com.yuyh.library.bean.TipsView;
import com.yuyh.library.constant.Constants;
import com.yuyh.library.support.OnStateChangedListener;
import com.yuyh.library.view.EasyGuideView;

import java.util.ArrayList;
import java.util.List;

import ohos.agp.components.*;
import ohos.agp.components.element.ShapeElement;
import ohos.agp.utils.Color;
import ohos.agp.utils.TextAlignment;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;
import ohos.multimodalinput.event.TouchEvent;
import ohos.app.Context;
import ohos.agp.components.Component.ClickedListener;
import ohos.multimodalinput.event.MmiPoint;
import ohos.agp.window.service.DisplayManager;
import ohos.agp.utils.LayoutAlignment;
import ohos.aafwk.ability.AbilitySlice;

import static ohos.agp.components.ComponentContainer.LayoutConfig.MATCH_CONTENT;
import static ohos.agp.components.ComponentContainer.LayoutConfig.MATCH_PARENT;

/**
 * EasyGuide implemenation
 * <p>
 * https://github.com/smuyyh/EasyGuideView
 *
 * @author yuyh
 * @date 2016/12/24
 */
public class EasyGuide {
    private static final HiLogLabel HILOG_LABEL1 = new HiLogLabel(0, 0, "Jobin");

    private AbilitySlice mActivity;

    private StackLayout mParentView;

    private EasyGuideView mGuideView;

    private List<HighlightArea> mAreas;

    private List<TipsView> mIndicators;

    private List<Message> mMessages;

    private Confirm mConfirm;

    private boolean dismissAnyWhere;

    private boolean performViewClick;

    private OnStateChangedListener listener;

    public EasyGuide(AbilitySlice activity, List<HighlightArea> areas, List<TipsView> indicators, List<Message> messages, Confirm confirm, boolean[] dismissandperformclick, StackLayout componentContainer) {
        this.mActivity = activity;
        this.mAreas = areas;
        this.mIndicators = indicators;
        this.mMessages = messages;
        this.mConfirm = confirm;
        this.dismissAnyWhere = dismissandperformclick[0];
        this.performViewClick = dismissandperformclick[1];
        mParentView = componentContainer;
    }


    /**
     * Add the listener
     *
     * @param listener
     */
    public void setOnStateChangedListener(OnStateChangedListener listener) {
        this.listener = listener;
    }

    /**
     * Show dialog
     */
    public void show() {
        mGuideView = new EasyGuideView(mActivity);
        mGuideView.setHightLightAreas(mAreas);
        DirectionalLayout mTipView = new DirectionalLayout(mActivity);
        mTipView.setAlignment(LayoutAlignment.HORIZONTAL_CENTER);
        mTipView.setLayoutConfig(new DirectionalLayout.LayoutConfig(MATCH_PARENT, MATCH_CONTENT));
        mTipView.setOrientation(DirectionalLayout.VERTICAL);
        if (mIndicators != null) {
            for (TipsView tipsView : mIndicators) {
                addView(tipsView.getView(), tipsView.getOffsetX(), tipsView.getOffsetY(), tipsView.getParams());
            }
        }
        if (mMessages != null) {
            int padding = dip2px(mActivity, 5);
            for (Message message : mMessages) {
                Text tvMsg = new Text(mActivity);
                tvMsg.setLayoutConfig(new ComponentContainer.LayoutConfig(MATCH_PARENT, MATCH_CONTENT));
                tvMsg.setPadding(padding, padding, padding, padding);
                tvMsg.setTextAlignment(TextAlignment.CENTER);
                tvMsg.setText(message.getMessagetext());
                Color hmosColor = EasyGuide.changeParamToColor(ohos.agp.utils.Color.WHITE.getValue());
                tvMsg.setTextColor(hmosColor);
                tvMsg.setTextSize(50);
                mTipView.addComponent(tvMsg);
            }
        }
        if (mConfirm != null) {
            Text tvConfirm = new Text(mActivity);
            tvConfirm.setTextAlignment(TextAlignment.CENTER);
            tvConfirm.setText(mConfirm.getText());
            Color hmosColor1 = EasyGuide.changeParamToColor(ohos.agp.utils.Color.WHITE.getValue());
            tvConfirm.setTextColor(hmosColor1);
            tvConfirm.setTextSize(50);
            tvConfirm.setBackground(new ShapeElement(mActivity.getContext(), ResourceTable.Graphic_btn_selector));
            ohos.agp.components.DirectionalLayout.LayoutConfig params = new DirectionalLayout.LayoutConfig(MATCH_CONTENT, MATCH_CONTENT);
            params.setMarginTop(dip2px(mActivity, 10));
            tvConfirm.setLayoutConfig(params);
            int lr = dip2px(mActivity, 8);
            int tb = dip2px(mActivity, 5);
            tvConfirm.setPadding(lr, tb, lr, tb);
            tvConfirm.setClickedListener(mConfirm.getListener() != null ? mConfirm.getListener() : new Component.ClickedListener() {
                @Override
                public void onClick(Component v) {
                    dismiss();
                }
            });
            mTipView.addComponent(tvConfirm);
        }
        addView(mTipView, Constants.CENTER, Constants.CENTER, new DependentLayout.LayoutConfig(MATCH_PARENT, MATCH_CONTENT));
        mParentView.addComponent(mGuideView, new StackLayout.LayoutConfig(MATCH_PARENT, MATCH_PARENT));
        addListener();
        HiLog.error(HILOG_LABEL1, "Add View");
    }

    private void addListener() {
        if (dismissAnyWhere || performViewClick) {
            mGuideView.setClickable(true);
            addTouchListener();
        }
        if (listener != null) {
            listener.onShow();
        }
    }

    private void addTouchListener() {
        mGuideView.setTouchEventListener(new Component.TouchEventListener() {
            @Override
            public boolean onTouchEvent(Component v, TouchEvent event) {
                if (event.getAction() == TouchEvent.PRIMARY_POINT_UP) {
                    if (mAreas.size() > 0) {
                        return handleTouchPointUpEvent(event);
                    } else {
                        dismiss();
                        return false;
                    }
                }
                return true;
            }
        });
    }

    private boolean handleTouchPointUpEvent(TouchEvent event) {
        for (HighlightArea area : mAreas) {
            final Component view = area.getmHightlightView();
            if (view != null && inRangeOfView(view, event)) {
                dismiss();
                if (listener != null) {
                    listener.onHeightlightViewClick(view);
                }
                if (performViewClick) {
                    view.simulateClick();
                }
            } else if (dismissAnyWhere) {
                dismiss();
            }
        }
        return true;
    }


    /**
     * Dismiss the dialog
     */
    public void dismiss() {
        mGuideView.recyclerBitmap();
        if (mParentView.getChildIndex(mGuideView) > 0) {
            mParentView.removeComponent(mGuideView);
            if (listener != null) {
                listener.onDismiss();
            }
        }
    }

    private void addView(Component view, int offsetX, int offsetY, ohos.agp.components.DependentLayout.LayoutConfig params) {
        if (params == null) {
            params = new DependentLayout.LayoutConfig(MATCH_CONTENT, MATCH_CONTENT);
        }
        if (offsetX == Constants.CENTER) {
            params.addRule(DependentLayout.LayoutConfig.HORIZONTAL_CENTER, DependentLayout.LayoutConfig.TRUE);
        } else if (offsetX < 0) {
            params.addRule(DependentLayout.LayoutConfig.ALIGN_PARENT_RIGHT, DependentLayout.LayoutConfig.TRUE);
            params.setMarginRight(-offsetX);
        } else {
            params.setMarginLeft(offsetX);
        }
        if (offsetY == Constants.CENTER) {
            params.addRule(DependentLayout.LayoutConfig.VERTICAL_CENTER, DependentLayout.LayoutConfig.TRUE);
        } else if (offsetY < 0) {
            params.addRule(DependentLayout.LayoutConfig.ALIGN_PARENT_BOTTOM, DependentLayout.LayoutConfig.TRUE);
            params.setMarginBottom(-offsetY);
        } else {
            params.setMarginTop(offsetY);
        }
        mGuideView.addComponent(view, params);
    }

    public boolean isShowing() {
        return mParentView.getChildIndex(mGuideView) > 0;
    }

    public boolean inRangeOfView(Component view, TouchEvent ev) {
        int[] location = view.getLocationOnScreen();
        int x = location[0];
        int y = location[1] - 130;
        MmiPoint point = ev.getPointerScreenPosition(0);
        return point.getX() >= x && point.getX() <= (x + view.getWidth()) && point.getY() >= y && point.getY() <= (y + view.getHeight());
    }

    public static class Builder {

        AbilitySlice activity;

        StackLayout componentContainer;

        List<HighlightArea> areas = new ArrayList<>();

        List<TipsView> views = new ArrayList<>();

        List<Message> messages = new ArrayList<>();

        Confirm confirm;

        boolean dismissAnyWhere = true;

        boolean performViewClick;

        public Builder(AbilitySlice activity) {
            this.activity = activity;
        }

        public Builder addHightArea(Component view, int shape) {
            HighlightArea area = new HighlightArea(view, shape);
            areas.add(area);
            return this;
        }

        public Builder addHightLightArea(HighlightArea area) {
            areas.add(area);
            return this;
        }

        public Builder addIndicator(int resId, int offX, int offY) {
            Image ivIndicator = new Image(activity);
            ivIndicator.setPixelMap(resId);
            views.add(new TipsView(ivIndicator, offX, offY));
            return this;
        }

        public Builder addView(Component view, int offX, int offY) {
            views.add(new TipsView(view, offX, offY));
            return this;
        }


        public Builder addParentView(StackLayout container) {
            componentContainer = container;
            return this;
        }

        public Builder addView(Component view, int offX, int offY, ohos.agp.components.DependentLayout.LayoutConfig params) {
            views.add(new TipsView(view, offX, offY, params));
            return this;
        }

        /**
         * Add message
         *
         * @param message
         * @param textSize
         * @return
         */
        public Builder addMessage(String message, int textSize) {
            messages.add(new Message(message, textSize));
            return this;
        }

        /**
         * Add positive button
         *
         * @param btnText
         * @param textSize
         * @return
         */
        public Builder setPositiveButton(String btnText, int textSize) {
            this.confirm = new Confirm(btnText, textSize);
            return this;
        }

        public Builder setPositiveButton(String btnText, int textSize, ClickedListener listener) {
            this.confirm = new Confirm(btnText, textSize, listener);
            return this;
        }

        /**
         * dismiss
         *
         * @param dismissAnyWhere
         * @return
         */
        public Builder dismissAnyWhere(boolean dismissAnyWhere) {
            this.dismissAnyWhere = dismissAnyWhere;
            return this;
        }

        /**
         * perform view click
         *
         * @param performViewClick
         * @return
         */
        public Builder performViewClick(boolean performViewClick) {
            this.performViewClick = performViewClick;
            return this;
        }

        public EasyGuide build() {
            boolean[] dismissandperform = {dismissAnyWhere, performViewClick};
            return new EasyGuide(activity, areas, views, messages, confirm, dismissandperform, componentContainer);
        }
    }

    public static int dip2px(Context context, float dpValue) {
        final float scale = DisplayManager.getInstance().getDefaultDisplay(context).get().getAttributes().scalDensity;
        return (int) (dpValue * scale + 0.5f);
    }

    public static Color changeParamToColor(int color) {
        return new Color(color);
    }

}
