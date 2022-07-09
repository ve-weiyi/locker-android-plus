package com.ve.lib.common.base.view.swipe

import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar

/**
 * @Description hello word!
 * @Author  weiyi
 * @Date 2022/4/10
 */
class SwipeBackManager(mActivity: AppCompatActivity, toolbar: Toolbar, toolbarTitle :String?) {

    var mToolbar: Toolbar ?=null
    var mTitle: String ?=" not title"

    init {
        mToolbar=toolbar
        mTitle=toolbarTitle

        //设置标题栏之后才能修改菜单
        mToolbar?.run {
            title = mTitle
            mActivity.setSupportActionBar(this)
            //需要先设置设置toolbar
            mActivity.supportActionBar?.setHomeButtonEnabled(true)
            mActivity.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }
    }

}