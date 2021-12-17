package com.yuyh.library.bean;

import ohos.agp.components.Component;
import ohos.agp.components.DependentLayout.LayoutConfig;

/**
 * @author yuyh
 * @date 2016/12/24
 */
public class TipsView {

    private Component view;

    private int resId = -1;

    private int offsetX;

    private int offsetY;

    private ohos.agp.components.DependentLayout.LayoutConfig params;

    public TipsView(int resId, int offsetX, int offsetY) {
        this.setResId(resId);
        this.setOffsetX(offsetX);
        this.setOffsetY(offsetY);
    }

    /**
     * Constructor for creating Tipview.
     *
     * @param view view
     * @param offsetX offsetX
     * @param offsetY offsetY
     */
    public TipsView(Component view, int offsetX, int offsetY) {
        this.setView(view);
        this.setOffsetX(offsetX);
        this.setOffsetY(offsetY);
    }

    /**
     * Constructor for creating Tipview with layout params.
     *
     * @param view view
     * @param offsetX offsetX
     * @param offsetY offsetY
     * @param params params
     */
    public TipsView(Component view, int offsetX, int offsetY, LayoutConfig params) {
        this.setView(view);
        this.setOffsetX(offsetX);
        this.setOffsetY(offsetY);
        this.setParams(params);
    }

    public Component getView() {
        return view;
    }

    public void setView(Component view) {
        this.view = view;
    }

    public int getResId() {
        return resId;
    }

    public void setResId(int resId) {
        this.resId = resId;
    }

    public int getOffsetX() {
        return offsetX;
    }

    public void setOffsetX(int offsetX) {
        this.offsetX = offsetX;
    }

    public int getOffsetY() {
        return offsetY;
    }

    public void setOffsetY(int offsetY) {
        this.offsetY = offsetY;
    }

    public LayoutConfig getParams() {
        return params;
    }

    public void setParams(LayoutConfig params) {
        this.params = params;
    }
}
