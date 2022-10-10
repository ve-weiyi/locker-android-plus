package com.ve.lib.common.base.view.container


import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.WindowInsets
import android.widget.FrameLayout
import com.ve.lib.application.utils.LogUtil


/**
 * @author waynie
 * @date 2022/10/9
 * @desc lockit-android
 */
class WindowInsetsFrameLayout  constructor(
    context: Context,
    attrs: AttributeSet?,
    defStyleAttr: Int
) :
    FrameLayout(context, attrs, defStyleAttr) {

    constructor(context: Context) : this(context, null) {

    }
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0) {

    }


    override fun onApplyWindowInsets(insets: WindowInsets): WindowInsets {
        val childCount = childCount
        for (index in 0 until childCount) {
            getChildAt(index).dispatchApplyWindowInsets(insets)
        }
        return insets
    }

    init {
        setOnHierarchyChangeListener(object : OnHierarchyChangeListener {
            override fun onChildViewAdded(parent: View?, child: View?) {
                requestApplyInsets()
            }

            override fun onChildViewRemoved(parent: View?, child: View?) {}
        })
    }
}
