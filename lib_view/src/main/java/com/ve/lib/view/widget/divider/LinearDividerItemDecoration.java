package com.ve.lib.view.widget.divider;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ve.lib.common.utils.ui.DisplayUtil;


/**
 * recycler com.ve.lib.view 横竖方向使用的divider
 * https://blog.csdn.net/u013626215/article/details/109130265
 * @author wangjian
 */
public class LinearDividerItemDecoration extends RecyclerView.ItemDecoration {

    public static final int HORIZONTAL = 0;
    public static final int VERTICAL = 1;
    private static final String TAG = "DividerItem";
    private static final int[] ATTRS = new int[]{android.R.attr.listDivider};
    private Context mContext;
    private Drawable mDivider;
    private int mOrientation;
    private int mDividerHeight;
    private final Rect mBounds = new Rect();

    private boolean showLastDivider=false;

    private int mLeft=0;
    private int mRight=0;
    private int mTop=0;
    private int mBottom=0;

    public void setPosition(int left,int top,int right,int bottom){
        mLeft=left;
        mTop=top;
        mRight=right;
        mBottom=bottom;
    }

    public LinearDividerItemDecoration(Context context, int orientation) {
        this.mContext = context;
        TypedArray a = context.obtainStyledAttributes(ATTRS);
        this.mDivider = a.getDrawable(0);
//        this.mDivider =context.getDrawable(R.drawable.recycler_divider_16_dp);
        if (this.mDivider == null) {
            Log.w("DividerItem", "@android:attr/listDivider was not set in the theme used for this LinearDividerItemDecoration. Please set that attribute all call setDrawable()");
        }
//        InsetDrawable insetDrawable=new InsetDrawable(divider, inset, 0, inset, 0);
        a.recycle();
        this.setOrientation(orientation);
    }
    public LinearDividerItemDecoration(Context context) {
        this(context, LinearLayoutManager.VERTICAL);
    }
    /**
     * 设置divider方向
     *
     * @param orientation 方向
     */
    public void setOrientation(int orientation) {
        if (orientation != HORIZONTAL && orientation != VERTICAL) {
            throw new IllegalArgumentException("Invalid orientation. It should be either HORIZONTAL or VERTICAL");
        } else {
            this.mOrientation = orientation;
        }
    }

    /**
     * 是否显示最后一条分割线
     */
    public void setShowLastDivider(boolean showLastDivider) {
        this.showLastDivider = showLastDivider;
    }

    /**
     * 设置分割线drawable
     *
     * @param drawable drawable
     */
    public void setDrawable(@NonNull Drawable drawable) {
        this.mDivider = drawable;
        this.mDividerHeight = mDivider.getIntrinsicHeight();
    }

    /**
     * 设置分割线drawable
     *
     * @param dividerColor 分割线颜色
     * @param dividerHeight 分割线高度，单位dp
     */
    public void setDrawable(@ColorInt int dividerColor, int dividerHeight) {
        this.mDivider = new ColorDrawable(dividerColor);
        this.mDividerHeight = DisplayUtil.dip2px(dividerHeight);
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        if (parent.getLayoutManager() != null && this.mDivider != null) {
            if (this.mOrientation == VERTICAL) {
                this.drawVertical(c, parent);
            } else {
                this.drawHorizontal(c, parent);
            }
        }
    }

    private void drawVertical(Canvas canvas, RecyclerView parent) {
        canvas.save();
        int left;
        int right;
        if (parent.getClipToPadding()) {
            left = parent.getPaddingLeft();
            right = parent.getWidth() - parent.getPaddingRight();
            canvas.clipRect(left, parent.getPaddingTop(), right, parent.getHeight() - parent.getPaddingBottom());
        } else {
            left = 0;
            right = parent.getWidth();
        }

        int childCount = parent.getChildCount();
        childCount = showLastDivider ? childCount : childCount - 1;

        for (int i = 0; i < childCount; ++i) {
            View child = parent.getChildAt(i);
            parent.getDecoratedBoundsWithMargins(child, this.mBounds);
            int bottom = this.mBounds.bottom + Math.round(child.getTranslationY());
            int top = bottom - this.mDividerHeight;
            this.mDivider.setBounds(left, top, right, bottom);
            this.mDivider.draw(canvas);
        }

        canvas.restore();
    }

    private void drawHorizontal(Canvas canvas, RecyclerView parent) {
        canvas.save();
        int top;
        int bottom;
        if (parent.getClipToPadding()) {
            top = parent.getPaddingTop();
            bottom = parent.getHeight() - parent.getPaddingBottom();
            canvas.clipRect(parent.getPaddingLeft(), top, parent.getWidth() - parent.getPaddingRight(), bottom);
        } else {
            top = 0;
            bottom = parent.getHeight();
        }

        int childCount = parent.getChildCount();
        childCount = showLastDivider ? childCount : childCount - 1;

        for (int i = 0; i < childCount; ++i) {
            View child = parent.getChildAt(i);
            parent.getLayoutManager().getDecoratedBoundsWithMargins(child, this.mBounds);
            int right = this.mBounds.right + Math.round(child.getTranslationX());
            int left = right - this.mDividerHeight;
            this.mDivider.setBounds(left, top, right, bottom);
            this.mDivider.draw(canvas);
        }

        canvas.restore();
    }

    //由于Divider也有宽高，每一个Item需要向下或者向右偏移，给divider留出绘制空间
    // 这个是item的宽和高
    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        if (mDivider == null) {
            outRect.set(0, 0, 0, 0);
            return;
        }
        if (mOrientation == VERTICAL) {
            //parent.getChildCount() 不能拿到item的总数
            //https://stackoverflow.com/questions/29666598/android-recyclerview-finding-out-first
            // -and-last-com.ve.lib.view-on-itemdecoration
            int lastPosition = state.getItemCount() - 1;
            int position = parent.getChildAdapterPosition(view);
            if (position == lastPosition) {
                if (showLastDivider) {
                    // item com.ve.lib.view 距离下面的view间距是divider的高度
                    outRect.set(0, 0, 0, mDividerHeight);
                } else {
                    outRect.set(0, 0, 0, 0);
                }
            } else {
                outRect.set(0, 0, 0, mDividerHeight);
            }
        } else {
            int lastPosition = state.getItemCount() - 1;
            int position = parent.getChildAdapterPosition(view);
            if (showLastDivider || position < lastPosition) {
                outRect.set(0, 0, mDividerHeight, 0);
            } else {
                outRect.set(0, 0, 0, 0);
            }
        }
    }
}
