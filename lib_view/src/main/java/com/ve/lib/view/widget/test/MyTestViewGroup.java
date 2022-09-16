package com.ve.lib.view.widget.test;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

/**
 * @Description hello word!
 *  https://www.cnblogs.com/aademeng/articles/11032763.html
 * @Author weiyi
 * @Date 2022/4/7
 */
public class MyTestViewGroup extends ViewGroup {

    public MyTestViewGroup(Context context) {
        super(context);
    }

    public MyTestViewGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyTestViewGroup(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int with = MeasureSpec.getSize(widthMeasureSpec);
        int height =  MeasureSpec.getSize(heightMeasureSpec);
        int withMode = MeasureSpec.getMode(widthMeasureSpec);
        int heigthMode = MeasureSpec.getMode(heightMeasureSpec);

        if (getChildCount() == 0) {//如果没有子View,当前ViewGroup没有存在的意义，不用占用空间
            setMeasuredDimension(0, 0);
            return;
        }
        if (withMode == MeasureSpec.AT_MOST && heigthMode == MeasureSpec.AT_MOST){
            //高度累加，宽度取最大
            setMeasuredDimension(getMaxChildWidth(),getTotleHeight());
        }else if (heigthMode == MeasureSpec.AT_MOST){
            setMeasuredDimension(with,getTotleHeight());
        }else if (withMode == MeasureSpec.AT_MOST){
            setMeasuredDimension(getMaxChildWidth(),height);
        }

    }

    /***
     * 获取子View中宽度最大的值
     */
    private int getMaxChildWidth() {
        int childCount = getChildCount();
        int maxWidth = 0;
        for (int i = 0; i < childCount; i++) {
            View childView = getChildAt(i);
            if (childView.getMeasuredWidth() > maxWidth) {
                maxWidth = childView.getMeasuredWidth();
            }
        }
        return maxWidth;
    }

    /***
     * 将所有子View的高度相加
     **/
    private int getTotleHeight() {
        int childCount = getChildCount();
        int height = 0;
        for (int i = 0; i < childCount; i++) {
            View childView = getChildAt(i);
            height += childView.getMeasuredHeight();
        }
        return height;
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        int count = getChildCount();
        int currentHeigth = 0;
        //将子View逐个摆放
        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            int childHeigth = child.getMeasuredHeight();
            child.layout(left, currentHeigth, right + child.getMeasuredWidth(), currentHeigth + childHeigth);
            currentHeigth += childHeigth;
        }

    }
}
