package com.yuyh.library.bean;

import ohos.agp.components.Component.ClickedListener;

/**
 * Confirm class for Ok button.
 *
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
     * Constructor for creating Confirm.
     *
     * @param text text
     * @param textSize textSize
     * @param listener listener
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
