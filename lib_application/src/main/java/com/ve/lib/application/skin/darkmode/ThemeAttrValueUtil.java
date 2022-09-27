package com.ve.lib.application.skin.darkmode;

import android.util.TypedValue;

import androidx.annotation.AttrRes;

public class ThemeAttrValueUtil {

    private ThemeAttrValueUtil(){}

    private static final TypedValue typedValue = new TypedValue();

    public static int getThemeResourceValue(@AttrRes int attrRes){
        SkinEngine.getInstance().getCacheTheme(SkinEngine.getInstance().getSelectedThemeRes()).resolveAttribute(attrRes, typedValue, true);
        return typedValue.data;
    }

    public static int getThemeResourceId(@AttrRes int attrRes){
        SkinEngine.getInstance().getCacheTheme(SkinEngine.getInstance().getSelectedThemeRes()).resolveAttribute(attrRes, typedValue, true);
        return typedValue.resourceId;
    }
}
