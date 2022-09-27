package com.ve.lib.application.skin.darkmode;

import android.util.ArrayMap;
import android.view.View;

final class SkinEntry {

    private final View skinView;
    private final ArrayMap<String, Integer> skinAttrMap;

    public final View getSkinView() {
        return this.skinView;
    }

    public final ArrayMap<String, Integer> getSkinAttrMap() {
        return this.skinAttrMap;
    }

    public SkinEntry(View skinView, ArrayMap<String, Integer> skinAttrMap) {
        this.skinView = skinView;
        this.skinAttrMap = skinAttrMap;
    }
}
