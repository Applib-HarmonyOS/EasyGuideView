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

import ohos.agp.components.Component.ClickedListener;

/**
 * @author yuyh
 * @date 2016/12/24
 */
public class Confirm {

    private String text;

    private int textSize = -1;

    private ClickedListener listener;

    public Confirm(String text, int textSize) {
        this.setText(text);
        this.setTextSize(textSize);
    }
    /**
     * Constructor for creating Confirm
     */
    public Confirm(String text, int textSize, ClickedListener listener) {
        this.setText(text);
        this.setTextSize(textSize);
        this.setListener(listener);
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getTextSize() {
        return textSize;
    }

    public void setTextSize(int textSize) {
        this.textSize = textSize;
    }

    public ClickedListener getListener() {
        return listener;
    }

    public void setListener(ClickedListener listener) {
        this.listener = listener;
    }
}
