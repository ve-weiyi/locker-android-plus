package com.ve.lib.application.skin.darkmode;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.Resources.Theme;
import android.graphics.drawable.GradientDrawable;
import android.util.ArrayMap;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.LayoutInflater.Factory2;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StyleRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.view.LayoutInflaterCompat;


import com.ve.lib.application.utils.LogUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;


public class SkinFactory implements Factory2 {

    String key;
    private final AppCompatDelegate appCompatDelegate;
    private final LayoutInflater layoutInflater;
    private final Resources mResources;

    private final TypedValue outTypedValue;
    private final List<SkinEntry> skinEntryList;

    private static final String[] CLASS_PREFIX_LIST = new String[]{"android.widget.", "android.webkit.", "android.app."};

    public SkinFactory(AppCompatActivity activity) {
        this.key = activity.getClass().getName();
        this.appCompatDelegate = activity.getDelegate();
        this.layoutInflater = activity.getLayoutInflater();
        this.mResources = activity.getResources();
        this.outTypedValue = new TypedValue();
        this.skinEntryList = new ArrayList<>();
        LayoutInflaterCompat.setFactory2(activity.getLayoutInflater(), this);
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull String name, @NonNull Context context, @NonNull AttributeSet attrs) {
        return null;
    }

    @Override
    public final View onCreateView(View parent, @NonNull String name, @NonNull Context context, @NonNull AttributeSet attrs) {

        View view = this.appCompatDelegate.createView(parent, name, context, attrs);
        ArrayMap<String, Integer> attrMap = new ArrayMap<>();
        int attributeCount = attrs.getAttributeCount();
        for (int i = 0; i < attributeCount; i++) {
            String attributeValue = attrs.getAttributeValue(i);
            if (attributeValue.startsWith("?")) {
                int attributeValueRes = Integer.parseInt(attributeValue.substring(1));
                for (int skinAttrValueRes : SkinEngine.getInstance().skinAttrValueResArray) {
                    if (skinAttrValueRes != attributeValueRes) {
                        continue;
                    }
                    attrMap.put(attrs.getAttributeName(i), attributeValueRes);
                }
            }
        }

        if (attrMap.size() != 0) {
            if (view == null) {
                if (!name.contains(".")) {
                    for (String prefix : CLASS_PREFIX_LIST) {
                        try {
                            view = this.layoutInflater.createView(name, prefix, attrs);
                        } catch (ClassNotFoundException e) {
                            LogUtil.d(this,e.getMessage());
                        }
                        if (view != null) {
                            break;
                        }
                    }
                } else {
                    try {
                        view = this.layoutInflater.createView(name, null, attrs);
                    } catch (ClassNotFoundException e) {
                        LogUtil.d(this,e.getMessage());
                    }
                }
            }

            if (view != null) {
                skinEntryList.add(new SkinEntry(view, attrMap));
            }

        }
        return view;
    }

    public final void changeSkin(@StyleRes int selectedThemeRes) {
        Theme cacheTheme = SkinEngine.getInstance().getCacheTheme(selectedThemeRes);

        for (SkinEntry skinEntry : skinEntryList) {
            View skinView = skinEntry.getSkinView();
            ArrayMap<String, Integer> skinAttrMap = skinEntry.getSkinAttrMap();
            for (Entry<String, Integer> next : skinAttrMap.entrySet()) {
                String attrName = next.getKey();
                int attrValueRes = next.getValue();
                cacheTheme.resolveAttribute(attrValueRes, outTypedValue, true);
                if ("shape_drawable".equals(attrName)) {
                    GradientDrawable gradientDrawable = (GradientDrawable) skinView.getBackground().mutate();
                    gradientDrawable.setColor(outTypedValue.data);
                    skinView.setBackground(gradientDrawable);
                } if ("background".equals(attrName)) {
                    String resourceTypeName = this.mResources.getResourceTypeName(outTypedValue.resourceId);
                    if ("color".equals(resourceTypeName)) {
                        skinView.setBackgroundColor(outTypedValue.data);
                    } else if ("drawable".equals(resourceTypeName)) {
                        skinView.setBackgroundResource(outTypedValue.resourceId);
                    }
                } else if ("textColor".equals(attrName)) {
                    if (skinView instanceof TextView) {
                        ((TextView) skinView).setTextColor(outTypedValue.data);
                    }
                } else if ("src".equals(attrName)) {
                    if (skinView instanceof ImageView) {
                        ((ImageView) skinView).setImageResource(outTypedValue.resourceId);
                    }
                } else {
                    changeSkinByWidget(skinView, attrName, outTypedValue.resourceId, outTypedValue.data);
                }
            }
        }
    }

    public void changeSkinByWidget(@NonNull View widget, @NonNull String attrName, int attrResourceId, int attrValue) {
    }

    public final void clear() {
        this.skinEntryList.clear();
    }
}
