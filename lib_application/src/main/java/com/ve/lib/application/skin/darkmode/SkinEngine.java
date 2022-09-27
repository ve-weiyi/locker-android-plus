package com.ve.lib.application.skin.darkmode;

import android.app.Activity;
import android.app.Application;
import android.content.res.Resources;
import android.content.res.Resources.Theme;

import androidx.annotation.NonNull;
import androidx.annotation.StyleRes;
import androidx.collection.SparseArrayCompat;

import java.util.HashMap;
import java.util.Map;


public final class SkinEngine {
    private Resources resources;
    @StyleRes
    private int selectedThemeRes;
    public int[] skinAttrValueResArray;
    public String[] changeThemeActivityNameArray;
    private SparseArrayCompat<Theme> themeCache;
    private Map<String, SkinFactory> skinFactoryMap;

    public int getSelectedThemeRes() {
        return selectedThemeRes;
    }

    public final void init(Application app, @StyleRes int selectedThemeRes, int[] skinAttrValueResArray, Class<?>[] changeThemeActivityClassArray) {
        this.resources = app.getResources();
        this.selectedThemeRes = selectedThemeRes;
        this.skinAttrValueResArray = skinAttrValueResArray;
        int changeLength = changeThemeActivityClassArray.length;
        changeThemeActivityNameArray = new String[changeLength];

        for (int i = 0; i < changeLength; i++) {
            changeThemeActivityNameArray[i] = changeThemeActivityClassArray[i].getName();
        }

        themeCache = new SparseArrayCompat<>();
        skinFactoryMap = new HashMap<>();
        app.registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacksAdapter() {
            @Override
            public void onActivityPaused(@NonNull Activity activity) {
                if (activity.isFinishing()) {
                    String key = activity.getClass().getName();
                    SkinFactory skinFactory = skinFactoryMap.remove(key);
                    if (skinFactory != null) {
                        skinFactory.clear();
                    }
                }
            }
        });
    }

    public void registerSkinFactory(@NonNull SkinFactory factory) {
        skinFactoryMap.put(factory.key, factory);
    }

    public final void changeTheme(@StyleRes int selectedThemeRes) {
        if (this.selectedThemeRes != selectedThemeRes) {
            this.selectedThemeRes = selectedThemeRes;
            for (SkinFactory factory : skinFactoryMap.values()) {
                factory.changeSkin(selectedThemeRes);
            }
        }
    }

    public Theme getCacheTheme(@StyleRes int themeRes) {
        Theme theme = themeCache.get(themeRes);
        if (theme == null) {
            theme = this.resources.newTheme();
            theme.applyStyle(themeRes, true);
            themeCache.put(themeRes, theme);
        }
        return theme;
    }

    public final void clearFactory() {
        for (SkinFactory factory : skinFactoryMap.values()) {
            factory.clear();
        }
    }

    private SkinEngine() {
    }

    public static SkinEngine getInstance() {
        return SingletonHolder.INSTANCE;
    }

    private static final class SingletonHolder {
        private static final SkinEngine INSTANCE = new SkinEngine();

        private SingletonHolder() {
        }
    }
}
