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

package com.yuyh.easyguideview.slice;

import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.agp.components.Button;
import ohos.agp.components.Component;
import ohos.agp.components.ComponentContainer;
import ohos.agp.components.DependentLayout;
import ohos.agp.components.Image;
import ohos.agp.components.LayoutScatter;
import ohos.agp.components.StackLayout;
import ohos.agp.window.service.WindowManager;
import com.yuyh.easyguideview.ResourceTable;
import com.yuyh.library.EasyGuide;
import com.yuyh.library.support.Hshape;
import com.yuyh.library.support.OnStateChangedListener;

/**
 * Sample app to test the EasyGuideView library functionality.
 */
public class MainAbilitySlice extends AbilitySlice {

    EasyGuide easyGuide;
    StackLayout mParentView;

    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_ability_main);
        setTheme(ohos.global.systemres.ResourceTable.Theme_theme_hide_title_bar);
        getAbility().getWindow().addFlags(WindowManager.LayoutConfig.MARK_FULL_SCREEN);
        mParentView = (StackLayout) findComponentById(ResourceTable.Id_parentLayout);
        Button firstButton = (Button) findComponentById(ResourceTable.Id_menu_button_first);
        firstButton.setClickedListener(new Component.ClickedListener() {
            @Override
            public void onClick(Component component) {
                btnShowFirst(component);
            }
        });


        Button secondButton = (Button) findComponentById(ResourceTable.Id_menu_button_second);
        secondButton.setClickedListener(component -> btnShowSecond(component));
    }

    @Override
    public void onActive() {
        super.onActive();
    }

    @Override
    public void onForeground(Intent intent) {
        super.onForeground(intent);
    }

    private void btnShowFirst(Component view) {
        int[] loc = view.getLocationOnScreen();
        if (easyGuide != null && easyGuide.isShowing()) {
            easyGuide.dismiss();
        }
        easyGuide = new EasyGuide.Builder(MainAbilitySlice.this)
                .addHightArea(view, Hshape.RECTANGLE)
                .addParentView(mParentView)
                .addIndicator(ResourceTable.Media_arrow_right_top, loc[0], loc[1] - 130 + view.getHeight())
                .addMessage(getString(ResourceTable.String_message_menu_content), 50)
                .setPositiveButton(getString(ResourceTable.String_menu_ok), 50, new Component.ClickedListener() {
                    @Override
                    public void onClick(Component v) {
                        easyGuide.dismiss();
                    }
                })
                .performViewClick(true)
                .dismissAnyWhere(false)
                .build();
        easyGuide.show();
    }

    private void btnShowSecond(Component view) {
        int[] loc = view.getLocationOnScreen();
        Component tipsView = createTipsView();
        if (easyGuide != null && easyGuide.isShowing()) {
            easyGuide.dismiss();
        }
        easyGuide = new EasyGuide.Builder(MainAbilitySlice.this)
                .addHightArea(view, Hshape.RECTANGLE)
                .addParentView(mParentView)
                .addView(tipsView, 0, loc[1] + view.getHeight() - 130, new DependentLayout.LayoutConfig(
                        ComponentContainer.LayoutConfig.MATCH_PARENT, ComponentContainer.LayoutConfig.MATCH_CONTENT))
                .dismissAnyWhere(false)
                .build();
        easyGuide.setOnStateChangedListener(new OnStateChangedListener() {
            @Override
            public void onShow() {
                //Give required implementation as per requirement
            }

            @Override
            public void onDismiss() {
                //Give required implementation as per requirement
            }

            @Override
            public void onHeightlightViewClick(Component view) {
                //Give required implementation as per requirement
            }
        });
        easyGuide.show();
    }


    private Component createTipsView() {
        Component component = LayoutScatter.getInstance(this).parse(ResourceTable.Layout_tips_view, null, false);
        Image ivIsee = (Image) component.findComponentById(ResourceTable.Id_ivIsee);
        ivIsee.setClickedListener(new Component.ClickedListener() {
            @Override
            public void onClick(Component component) {
                if (easyGuide != null) {
                    easyGuide.dismiss();
                }
            }
        });
        return component;
    }
}
