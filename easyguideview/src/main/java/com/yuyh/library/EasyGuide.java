package com.yuyh.library;

import ohos.aafwk.ability.AbilitySlice;
import ohos.agp.components.Component;
import ohos.agp.components.Component.ClickedListener;
import ohos.agp.components.ComponentContainer;
import ohos.agp.components.DependentLayout;
import ohos.agp.components.DirectionalLayout;
import ohos.agp.components.Image;
import ohos.agp.components.StackLayout;
import ohos.agp.components.Text;
import ohos.agp.components.element.ShapeElement;
import ohos.agp.utils.Color;
import ohos.agp.utils.LayoutAlignment;
import ohos.agp.utils.TextAlignment;
import ohos.agp.window.service.DisplayManager;
import ohos.app.Context;
import ohos.multimodalinput.event.MmiPoint;
import ohos.multimodalinput.event.TouchEvent;
import com.yuyh.library.bean.Confirm;
import com.yuyh.library.bean.HighlightArea;
import com.yuyh.library.bean.Message;
import com.yuyh.library.bean.TipsView;
import com.yuyh.library.constant.Constants;
import com.yuyh.library.support.OnStateChangedListener;
import com.yuyh.library.view.EasyGuideView;

import java.util.ArrayList;
import java.util.List;

/**
 * EasyGuide implemenation.
 * https://github.com/smuyyh/EasyGuideView.
 *
 * @author yuyh
 * @date 2016/12/24
 */
public class EasyGuide {

    private AbilitySlice mAbilitySlice;

    private StackLayout mParentView;

    private EasyGuideView mGuideView;

    private List<HighlightArea> mAreas;

    private List<TipsView> mIndicators;

    private List<Message> mMessages;

    private Confirm mConfirm;

    private boolean dismissAnyWhere;

    private boolean performViewClick;

    private OnStateChangedListener listener;

    /**
     * Constructor for creating EasyGuide.
     *
     * @param abilitySlice           abilitySlice
     * @param areas                  areas
     * @param indicators             indicators
     * @param messages               messages
     * @param confirm                confirm
     * @param dismissandperformclick dismissandperformclick
     * @param componentContainer     componentContainer
     */
    public EasyGuide(AbilitySlice abilitySlice, List<HighlightArea> areas, List<TipsView> indicators,
                     List<Message> messages, Confirm confirm, boolean[] dismissandperformclick,
                     StackLayout componentContainer) {
        this.mAbilitySlice = abilitySlice;
        this.mAreas = areas;
        this.mIndicators = indicators;
        this.mMessages = messages;
        this.mConfirm = confirm;
        this.dismissAnyWhere = dismissandperformclick[0];
        this.performViewClick = dismissandperformclick[1];
        mParentView = componentContainer;
    }


    /**
     * Add the listener.
     *
     * @param listener listener
     */
    public void setOnStateChangedListener(OnStateChangedListener listener) {
        this.listener = listener;
    }

    /**
     * Show dialog.
     */
    public void show() {
        mGuideView = new EasyGuideView(mAbilitySlice);
        mGuideView.setHightLightAreas(mAreas);
        DirectionalLayout tipView = new DirectionalLayout(mAbilitySlice);
        tipView.setAlignment(LayoutAlignment.HORIZONTAL_CENTER);
        tipView.setLayoutConfig(new DirectionalLayout.LayoutConfig(
                ComponentContainer.LayoutConfig.MATCH_PARENT, ComponentContainer.LayoutConfig.MATCH_CONTENT));
        tipView.setOrientation(DirectionalLayout.VERTICAL);
        if (mIndicators != null) {
            for (TipsView tipsView : mIndicators) {
                addView(tipsView.getView(), tipsView.getOffsetX(), tipsView.getOffsetY(), tipsView.getParams());
            }
        }
        if (mMessages != null) {
            int padding = dip2px(mAbilitySlice, 5);
            for (Message message : mMessages) {
                Text tvMsg = new Text(mAbilitySlice);
                tvMsg.setLayoutConfig(new ComponentContainer.LayoutConfig(
                        ComponentContainer.LayoutConfig.MATCH_PARENT, ComponentContainer.LayoutConfig.MATCH_CONTENT));
                tvMsg.setPadding(padding, padding, padding, padding);
                tvMsg.setTextAlignment(TextAlignment.CENTER);
                tvMsg.setText(message.getMessagetext());
                Color hmosColor = EasyGuide.changeParamToColor(ohos.agp.utils.Color.WHITE.getValue());
                tvMsg.setTextColor(hmosColor);
                tvMsg.setTextSize(message.getTextSize() == -1 ? 12 : message.getTextSize());
                tipView.addComponent(tvMsg);
            }
        }
        if (mConfirm != null) {
            Text tvConfirm = new Text(mAbilitySlice);
            tvConfirm.setTextAlignment(TextAlignment.CENTER);
            tvConfirm.setText(mConfirm.getText());
            Color hmosColor1 = EasyGuide.changeParamToColor(ohos.agp.utils.Color.WHITE.getValue());
            tvConfirm.setTextColor(hmosColor1);
            tvConfirm.setTextSize(mConfirm.getTextSize() == -1 ? 13 : mConfirm.getTextSize());
            tvConfirm.setBackground(new ShapeElement(mAbilitySlice.getContext(), ResourceTable.Graphic_btn_selector));
            ohos.agp.components.DirectionalLayout.LayoutConfig params = new DirectionalLayout.LayoutConfig(
                    ComponentContainer.LayoutConfig.MATCH_CONTENT, ComponentContainer.LayoutConfig.MATCH_CONTENT);
            params.setMarginTop(dip2px(mAbilitySlice, 10));
            tvConfirm.setLayoutConfig(params);
            int lr = dip2px(mAbilitySlice, 8);
            int tb = dip2px(mAbilitySlice, 5);
            tvConfirm.setPadding(lr, tb, lr, tb);
            tvConfirm.setClickedListener(mConfirm.getListener() != null
                    ? mConfirm.getListener() : new Component.ClickedListener() {
                        @Override
                            public void onClick(Component v) {
                                dismiss();
                            }
                    });
            tipView.addComponent(tvConfirm);
        }
        addView(tipView, Constants.CENTER, Constants.CENTER, new DependentLayout.LayoutConfig(
                ComponentContainer.LayoutConfig.MATCH_PARENT, ComponentContainer.LayoutConfig.MATCH_CONTENT));
        mParentView.addComponent(mGuideView, new StackLayout.LayoutConfig(
                ComponentContainer.LayoutConfig.MATCH_PARENT, ComponentContainer.LayoutConfig.MATCH_PARENT));
        addListener();
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
            final Component view = area.getHightlightView();
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
     * Dismiss the dialog.
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

    private void addView(Component view, int offsetX, int offsetY, DependentLayout.LayoutConfig params) {
        if (params == null) {
            params = new DependentLayout.LayoutConfig(
                    ComponentContainer.LayoutConfig.MATCH_CONTENT, ComponentContainer.LayoutConfig.MATCH_CONTENT);
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

    /**
     * Is dialog is showing or not.
     */
    public boolean isShowing() {
        return mParentView.getChildIndex(mGuideView) > 0;
    }

    private boolean inRangeOfView(Component view, TouchEvent ev) {
        int[] location = view.getLocationOnScreen();
        int x = location[0];
        int y = location[1] - 130;
        MmiPoint point = ev.getPointerScreenPosition(0);
        return point.getX() >= x && point.getX() <= (x + view.getWidth())
                && point.getY() >= y && point.getY() <= (y + view.getHeight());
    }

    /**
     * Builder class for creating easyguide object.
     */
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

        /**
         * Add Highlight area.
         *
         * @param view  view
         * @param shape shape
         */
        public Builder addHightArea(Component view, int shape) {
            HighlightArea area = new HighlightArea(view, shape);
            areas.add(area);
            return this;
        }

        /**
         * Add Highlight area.
         *
         * @param area area
         */
        public Builder addHightLightArea(HighlightArea area) {
            areas.add(area);
            return this;
        }

        /**
         * Add indicator.
         *
         * @param resId resId
         * @param offX  offX
         * @param offY  offY
         */
        public Builder addIndicator(int resId, int offX, int offY) {
            Image ivIndicator = new Image(activity);
            ivIndicator.setPixelMap(resId);
            views.add(new TipsView(ivIndicator, offX, offY));
            return this;
        }

        /**
         * Add parent view.
         *
         * @param container container
         */
        public Builder addParentView(StackLayout container) {
            componentContainer = container;
            return this;
        }

        /**
         * Add view.
         *
         * @param view   view
         * @param offX   offX
         * @param offY   offY
         * @param params params
         */
        public Builder addView(Component view, int offX, int offY, DependentLayout.LayoutConfig params) {
            views.add(new TipsView(view, offX, offY, params));
            return this;
        }

        /**
         * Add message.
         *
         * @param message  message
         * @param textSize textSize
         */
        public Builder addMessage(String message, int textSize) {
            messages.add(new Message(message, textSize));
            return this;
        }

        /**
         * Add positive button.
         *
         * @param btnText  btnText
         * @param textSize textSize
         */
        public Builder setPositiveButton(String btnText, int textSize) {
            this.confirm = new Confirm(btnText, textSize);
            return this;
        }

        /**
         * Add positive button with listener.
         *
         * @param btnText  btnText
         * @param textSize textSize
         * @param listener listener
         */
        public Builder setPositiveButton(String btnText, int textSize, ClickedListener listener) {
            this.confirm = new Confirm(btnText, textSize, listener);
            return this;
        }

        /**
         * dismiss any where.
         *
         * @param dismissAnyWhere dismissAnyWhere
         */
        public Builder dismissAnyWhere(boolean dismissAnyWhere) {
            this.dismissAnyWhere = dismissAnyWhere;
            return this;
        }

        /**
         * perform view click.
         *
         * @param performViewClick performViewClick
         */
        public Builder performViewClick(boolean performViewClick) {
            this.performViewClick = performViewClick;
            return this;
        }

        /**
         * build method for object creation.
         */
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
