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

    private lateinit var ss: SpannableString

    fun builder(str: String): SpannerHelper {
        ss = SpannableString(str)
        return this
    }


    object Builder{
        var textSize:Int?=null
        var textStyle:Int?=null
        var textScale:Int?=null

        var foregroundColor=0
        var backGroundColor=0

        var underLineSpan=false
        var deleteLine=false
        var subscript=false

        var textClickListener=0

        /**
         *
         */
        var flag=Spanned.SPAN_EXCLUSIVE_EXCLUSIVE

        fun build(text:String,start: Int,end: Int): SpannableString {
            val ss=SpannableString(text)


            textSize?.let {
                ss.setSpan(AbsoluteSizeSpan(it), start, end, flag)
            }

            textStyle?.let {
                ss.setSpan(StyleSpan(it), start, end, flag)

            }
            return ss
        }
    }


    /**
     * SpannableString ss=new SpannableString("str");
     * 设置字体大小，用px
     */
    fun setSizeSpanUsePx(start: Int, end: Int, pxSize: Int): SpannerHelper {
        ss.setSpan(AbsoluteSizeSpan(pxSize), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        return this
    }

    /**
     * 设置字体大小，用dip
     */
    fun setSizeSpanUseDip(start: Int, end: Int, dipSize: Int): SpannerHelper {
        ss.setSpan(AbsoluteSizeSpan(dipSize, true), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        return this
    }

    /**
     * / **
     * 设置字体相对大小
     *
     * @param relativeSize 相对大小 如：0.5f，2.0f
     */
    fun setRelativeSizeSpan(start: Int, end: Int, relativeSize: Float): SpannerHelper {
        ss.setSpan(RelativeSizeSpan(relativeSize), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        return this
    }

    /**
     * 设置字体
     *
     * @param typeface 字体类型 如：default，efault-bold,monospace,serif,sans-serif
     * @return
     */
    fun setTypeFaceSpan(start: Int, end: Int, typeface: String?): SpannerHelper {
        ss.setSpan(TypefaceSpan(typeface), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        return this
    }

    /**
     * 设置字体形体
     *
     * @param style 字体类型 如： Typeface.NORMAL正常 Typeface.BOLD粗体 Typeface.ITALIC斜体
     * Typeface.BOLD_ITALIC粗斜体
     * @return
     */
    fun setStyleSpan(start: Int, end: Int, style: Int): SpannerHelper {
        ss.setSpan(StyleSpan(style), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        return this
    }

    /**
     * 设置字体下划线
     */
    fun setUnderLineSpan(start: Int, end: Int): SpannerHelper {
        ss.setSpan(UnderlineSpan(), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        return this
    }

    /**
     * 设置字体删除线
     */
    fun setDeleteLineSpan(start: Int, end: Int): SpannerHelper {
        ss.setSpan(StrikethroughSpan(), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        return this
    }

    /**
     * 设置上标
     */
    fun setSuperscriptSpan(start: Int, end: Int): SpannerHelper {
        ss.setSpan(SuperscriptSpan(), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        return this
    }

    /**
     * 设置放大系数
     */
    fun setScaleSpan(start: Int, end: Int, scale: Float): SpannerHelper {
        ss.setSpan(ScaleXSpan(scale), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        return this
    }

    /**
     * 设置下标
     */
    fun setSubscriptSpan(start: Int, end: Int): SpannerHelper {
        ss.setSpan(SubscriptSpan(), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        return this
    }

    /**
     * 设置前景色(字体颜色)
     *
     * @param color 颜色值 如Color.BLACK ,Color.parseColor("#CCCCCC")
     * @return
     */
    fun setForegroundColorSpan(start: Int, end: Int, color: Int): SpannerHelper {
        ss.setSpan(ForegroundColorSpan(color), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        return this
    }
    /**
     * 设置背景色
     */
    fun setBackGroundColorSpan(start: Int, end: Int, color: Int): SpannerHelper {
        ss.setSpan(BackgroundColorSpan(color), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        return this
    }

    fun setTextClickListener(start: Int, end: Int, clickableSpan: ClickableSpan): SpannerHelper {
        ss.setSpan(clickableSpan, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        return this
    }
}