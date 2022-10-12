package com.ve.module.lockit.plus.ui.page.test

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.ve.lib.application.utils.LogUtil
import com.ve.lib.common.base.model.NaviMenuItem
import com.ve.module.lockit.plus.R


/**
 * @author waynie
 * @date 2022/10/11
 * @desc lockit-android
 */
class BottomTabView(context: Context, attrs: AttributeSet?, defStyleAttr: Int) :
    FrameLayout(context, attrs, defStyleAttr) {

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    private var linearLayout: LinearLayout
    private var mMenuList: MutableList<NaviMenuItem> = mutableListOf()
    private var mMenuViewList: MutableList<View> = mutableListOf()


    init {
        clipChildren = false
        var params = FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, dp2px(80F))
        params.gravity = Gravity.BOTTOM

        linearLayout = LinearLayout(context)
        linearLayout.layoutParams = params
        linearLayout.orientation = LinearLayout.HORIZONTAL
        addView(linearLayout)
    }


    fun addMenuItem(menuItem: NaviMenuItem,onClickListener: OnMenuClickListener?=null) {

        val menuView = LayoutInflater.from(context).inflate(R.layout.bottom_item, null)
        val params = LinearLayout.LayoutParams(0, dp2px(100F))
        params.gravity = Gravity.BOTTOM
        params.weight = 1F
        menuView.layoutParams = params
        linearLayout.addView(menuView)
        mMenuList.add(menuItem)
        mMenuViewList.add(menuView)

        menuView.setOnClickListener {
            onClickListener?.onItemClick(it,menuItem)

            mMenuViewList.forEachIndexed { index, view ->
                LogUtil.msg(index)
                refreshMenuView(view, mMenuList[index], mMenuList[index]==menuItem)
            }
        }
        refreshMenuView(menuView, menuItem, mMenuViewList.size == 1)
    }


    private fun dp2px(dpValue: Float): Int {
        val scale = context.resources.displayMetrics.density
        return (dpValue * scale + 0.5f).toInt()
    }


    private fun refreshMenuView(menuView: View, menu: NaviMenuItem, isSelect: Boolean) {
        LogUtil.msg(isSelect)
        val itemIcon = menuView.findViewById<ImageView>(R.id.item_icon)
        val itemTitle = menuView.findViewById<TextView>(R.id.item_title)
        val itemFrame = menuView.findViewById<ImageView>(R.id.item_frame)

        itemTitle.text = menu.fragmentTitle
        itemIcon.setImageResource(menu.menuIcon)
        itemFrame.visibility = if (isSelect) View.VISIBLE else View.GONE

        if (isSelect){
            menuView.alpha=1F
        }else{
            menuView.alpha=0.6F
        }
    }

    interface OnMenuClickListener {
        fun onItemClick(view:View,item: NaviMenuItem)
    }
}