package com.ve.module.lockit.plus.widget

import android.text.SpannableString
import android.text.Spanned
import android.text.style.*

/**
 * @author waynie
 * @date 2022/9/2
 * @desc EufyHomeNew
 *
 * https://juejin.cn/post/6888679485827514382
 *
 * https://blog.csdn.net/zuo_er_lyf/article/details/80340819
 */
object SpannerHelper {

    class Builder(
        private var text: String,
        private var start: Int,
        private var end: Int
    ) {
        var textSize: Int? = null
        var textStyle: Int? = null
        var textScale: Float? = null

        var foregroundColor: Int? = null
        var backGroundColor: Int? = null

        var underLine = false
        var deleteLine = false
        var subscript = false

        var textClickListener :ClickableSpan?=null

        /**
         *
         */
        var exclusive = Spanned.SPAN_EXCLUSIVE_EXCLUSIVE

        fun build(): SpannableString {
            val ss = SpannableString(text)

            textSize?.let {
                ss.setSpan(AbsoluteSizeSpan(it), start, end, exclusive)
            }

            textStyle?.let {
                ss.setSpan(StyleSpan(it), start, end, exclusive)

            }

            textScale?.let {
                ss.setSpan(ScaleXSpan(it), start, end, exclusive)
            }

            foregroundColor?.let {
                ss.setSpan(ForegroundColorSpan(it), start, end, exclusive)
            }

            backGroundColor?.let {
                ss.setSpan(BackgroundColorSpan(it), start, end, exclusive)
            }

            if (underLine) {
                ss.setSpan(UnderlineSpan(), start, end, exclusive)
            }

            if (deleteLine) {
                ss.setSpan(StrikethroughSpan(), start, end, exclusive)
            }
            if (subscript) {
                ss.setSpan(SubscriptSpan(), start, end, exclusive)
            }

            if (textClickListener != null) {
                ss.setSpan(textClickListener,start,end,exclusive)
            }
            return ss
        }
    }


    /**
     * SpannableString ss=new SpannableString("str");
     * 设置字体大小，用px
     */
    fun setSizeSpanUsePx(ss: SpannableString, start: Int, end: Int, pxSize: Int): SpannableString {
        ss.setSpan(AbsoluteSizeSpan(pxSize), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        return ss
    }

    /**
     * 设置字体大小，用dip
     */
    fun setSizeSpanUseDip(
        ss: SpannableString,
        start: Int,
        end: Int,
        dipSize: Int
    ): SpannableString {
        ss.setSpan(AbsoluteSizeSpan(dipSize, true), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        return ss
    }

    /**
     * / **
     * 设置字体相对大小
     *
     * @param relativeSize 相对大小 如：0.5f，2.0f
     */
    fun setRelativeSizeSpan(ss: SpannableString, start: Int, end: Int, relativeSize: Float): SpannableString {
        ss.setSpan(RelativeSizeSpan(relativeSize), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        return ss
    }

    /**
     * 设置字体
     *
     * @param typeface 字体类型 如：default，efault-bold,monospace,serif,sans-serif
     * @return
     */
    fun setTypeFaceSpan(ss: SpannableString, start: Int, end: Int, typeface: String?): SpannableString {
        ss.setSpan(TypefaceSpan(typeface), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        return ss
    }

    /**
     * 设置字体形体
     *
     * @param style 字体类型 如： Typeface.NORMAL正常 Typeface.BOLD粗体 Typeface.ITALIC斜体
     * Typeface.BOLD_ITALIC粗斜体
     * @return
     */
    fun setStyleSpan(ss: SpannableString, start: Int, end: Int, style: Int): SpannableString {
        ss.setSpan(StyleSpan(style), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        return ss
    }

    /**
     * 设置放大系数
     */
    fun setScaleSpan(ss: SpannableString, start: Int, end: Int, scale: Float): SpannableString {
        ss.setSpan(ScaleXSpan(scale), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        return ss
    }


    /**
     * 设置字体下划线
     */
    fun setUnderLineSpan(ss: SpannableString, start: Int, end: Int): SpannableString {
        ss.setSpan(UnderlineSpan(), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        return ss
    }

    /**
     * 设置字体删除线
     */
    fun setDeleteLineSpan(ss: SpannableString, start: Int, end: Int): SpannableString {
        ss.setSpan(StrikethroughSpan(), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        return ss
    }

    /**
     * 设置上标
     */
    fun setSuperscriptSpan(ss: SpannableString, start: Int, end: Int): SpannableString {
        ss.setSpan(SuperscriptSpan(), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        return ss
    }

    /**
     * 设置下标
     */
    fun setSubscriptSpan(ss: SpannableString, start: Int, end: Int): SpannableString {
        ss.setSpan(SubscriptSpan(), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        return ss
    }

    /**
     * 设置前景色(字体颜色)
     *
     * @param color 颜色值 如Color.BLACK ,Color.parseColor("#CCCCCC")
     * @return
     */
    fun setForegroundColorSpan(ss: SpannableString, start: Int, end: Int, color: Int): SpannableString {
        ss.setSpan(ForegroundColorSpan(color), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        return ss
    }

    /**
     * 设置背景色
     */
    fun setBackGroundColorSpan(ss: SpannableString, start: Int, end: Int, color: Int): SpannableString {
        ss.setSpan(BackgroundColorSpan(color), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        return ss
    }

    fun setTextClickListener(ss: SpannableString,start: Int, end: Int, clickableSpan: ClickableSpan): SpannableString {
        ss.setSpan(clickableSpan, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        return ss
    }
}