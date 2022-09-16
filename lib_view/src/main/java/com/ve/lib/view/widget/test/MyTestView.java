package com.ve.lib.view.widget.test;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import com.ve.lib.common.utils.system.LogUtil;

/**
 * @Description hello word!
 * @Author weiyi
 * @Date 2022/4/7
 */
public class MyTestView extends View {
    String TAG=" ";
    public MyTestView(Context context) {
        super(context);
    }

    public MyTestView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MyTestView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		int measureWidth = measureDimension(800, widthMeasureSpec);
		int measureHeight = measureDimension(120, heightMeasureSpec);
		// 设置自定义的控件MyViewGroup的大小
		setMeasuredDimension(measureWidth, measureHeight);
    }

    public int measureDimension(int defaultSize, int measureSpec) {
        int result=0; //结果

        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        switch (specMode) {
            case MeasureSpec.AT_MOST:  // 子容器可以是声明大小内的任意大小
                LogUtil.e(TAG, "子容器可以是声明大小内的任意大小");
                LogUtil.e(TAG, "大小为:"+specSize);
                result=specSize;
                break;
            case MeasureSpec.EXACTLY: //父容器已经为子容器设置了尺寸,子容器应当服从这些边界,不论子容器想要多大的空间.  比如EditTextView中的DrawLeft
                LogUtil.e(TAG, "父容器已经为子容器设置了尺寸,子容器应当服从这些边界,不论子容器想要多大的空间");
                LogUtil.e(TAG, "大小为:"+specSize);
                result=specSize;
                break;
            case MeasureSpec.UNSPECIFIED:  //父容器对于子容器没有任何限制,子容器想要多大就多大. 所以完全取决于子view的大小
                LogUtil.e(TAG, "父容器对于子容器没有任何限制,子容器想要多大就多大");
                LogUtil.e(TAG, "大小为:"+specSize);
                LogUtil.e(TAG, "默认大小为:"+defaultSize);
                result=defaultSize;
                break;
            default:
                break;
        }
        return result;
    }
}
