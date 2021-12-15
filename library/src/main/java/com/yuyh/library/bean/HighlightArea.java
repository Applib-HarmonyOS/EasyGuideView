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

package com.yuyh.library.bean;

import ohos.agp.utils.RectFloat;
import ohos.agp.components.Component;

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
        this.setmHightlightView(view);
        this.setmShape(shape);
    }

    public RectFloat getRectF() {
        RectFloat rectF = new RectFloat();
        if (getmHightlightView() != null) {
            int[] location = getmHightlightView().getLocationOnScreen();
            rectF.left = location[0];
            rectF.top = location[1] - 130;
            rectF.right = location[0] + getmHightlightView().getWidth();
            rectF.bottom = location[1] - 130 + getmHightlightView().getHeight();
        }
        return rectF;
    }

    public Component getmHightlightView() {
        return mHightlightView;
    }

    public void setmHightlightView(Component mHightlightView) {
        this.mHightlightView = mHightlightView;
    }

    public int getmShape() {
        return mShape;
    }

    public void setmShape(int mShape) {
        this.mShape = mShape;
    }
}
