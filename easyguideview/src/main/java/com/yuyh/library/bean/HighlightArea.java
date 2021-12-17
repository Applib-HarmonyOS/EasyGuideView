package com.yuyh.library.bean;

import ohos.agp.components.Component;
import ohos.agp.utils.RectFloat;

/**
 * Highlighted area display
 *
 * @author yuyh
 * @date 2016/12/24
 */
public class HighlightArea {

    private Component mHightlightView;

    private int mShape;

    public HighlightArea(Component view, int shape) {
        this.setHightlightView(view);
        this.setShape(shape);
    }

    /**
     * Create a RectFloat object.
     */
    public RectFloat getRectF() {
        RectFloat rectF = new RectFloat();
        if (getHightlightView() != null) {
            int[] location = getHightlightView().getLocationOnScreen();
            rectF.left = location[0];
            rectF.top = location[1] - 130;
            rectF.right = location[0] + getHightlightView().getWidth();
            rectF.bottom = location[1] - 130 + getHightlightView().getHeight();
        }
        return rectF;
    }

    public Component getHightlightView() {
        return mHightlightView;
    }

    public void setHightlightView(Component hightlightView) {
        this.mHightlightView = hightlightView;
    }

    public int getShape() {
        return mShape;
    }

    public void setShape(int shape) {
        this.mShape = shape;
    }
}
