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

    public TipsView(Component view, int offsetX, int offsetY) {
        this.setView(view);
        this.setOffsetX(offsetX);
        this.setOffsetY(offsetY);
    }

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
