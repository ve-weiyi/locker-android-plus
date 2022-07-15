package com.ve.lib.common.ext

import android.graphics.Color
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.ColorInt
import com.ve.lib.application.BaseApplication
import java.math.RoundingMode
import java.text.DecimalFormat
import java.util.*


//对于Toast的一个简单封装，也用到了扩展函数
fun String.showToast(duration:Int = Toast.LENGTH_SHORT){
    Toast.makeText(BaseApplication.context,this,duration).show()
}

fun Int.showToast(duration: Int = Toast.LENGTH_SHORT){
    Toast.makeText(BaseApplication.context,this,duration).show()
}

/**
 * 随机Color 避免纯色没有从255取值
 */
fun randomColor(): Int {
    Random().run {
        val red = nextInt(210)
        val green = nextInt(210)
        val blue = nextInt(210)
        return Color.rgb(red, green, blue)
    }
}

/**
 * 把搜索关键字变色，默认红色
 */
fun TextView.spanText(searchKey: String?, value: String?, @ColorInt color: Int = Color.RED) {
    if (!searchKey.isNullOrEmpty() && !value.isNullOrEmpty()
        && value.lowercase(Locale.getDefault())
            .contains(searchKey.lowercase(Locale.getDefault()))
    ) {
        searchKey.lowercase(Locale.getDefault()).apply {
            val builder = SpannableString(value)
            val list = value.lowercase(Locale.getDefault()).split(this)
            var recodeIndex = 0
            list.forEachIndexed { index, s ->
                if (index < list.size - 1) {
                    recodeIndex += s.length
                    builder.setSpan(
                        ForegroundColorSpan(color),
                        recodeIndex,
                        recodeIndex + length,
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                    recodeIndex += length
                }
            }
            text = builder
        }
    } else {
        text = value
    }
}

/**
 * 数据格式化
 */
fun numFormat(value: Double?): String {
    return if (value == null) "0.00" else DecimalFormat("0.00").apply {
        roundingMode = RoundingMode.HALF_UP
    }.format(value)
}