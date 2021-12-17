package com.yuyh.library.support;

import ohos.agp.components.Component;

/**
 * @author yuyh
 * @date 2016/12/25
 */
public interface OnStateChangedListener {

    void onShow();

    void onDismiss();

    void onHeightlightViewClick(Component view);
}
