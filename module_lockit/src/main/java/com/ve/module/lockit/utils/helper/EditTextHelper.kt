package com.ve.module.lockit.utils.helper

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import com.ve.lib.common.vutils.LogUtil


/**
 * @Author  weiyi
 * @Date 2022/4/30
 * @Description  current project lockit-android
 */
object EditTextHelper {

    fun setEditListener(
        view:EditText,
        before: ((s: CharSequence?, start: Int, count: Int, after: Int) -> Unit)? =null,
        on: ((s: CharSequence?, start: Int, before: Int, count: Int) -> Unit)? =null,
        after: (s: Editable?) -> Unit
    ) {

        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                LogUtil.msg("before " + s.toString())
                before?.invoke(s, start, count, after)
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                LogUtil.msg("on " + s.toString())
                on?.invoke(s, start,before, count)
            }

            override fun afterTextChanged(s: Editable?) {
                LogUtil.msg("after " + s.toString())
                after.invoke(s)
            }
        }

        view.addTextChangedListener(textWatcher)
    }
}