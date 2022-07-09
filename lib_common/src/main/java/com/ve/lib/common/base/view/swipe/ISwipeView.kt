package com.ve.lib.common.base.view.swipe

/**
 * @Description hello word!
 * @Author  weiyi
 * @Date 2022/3/18
 */

/**
 * interface for [SwipeBackActivity] and [SwipeBackPreferenceActivity]
 */
interface ISwipeView {
    var swipeBackManager:SwipeBackManager

    fun attachSwipeManager():SwipeBackManager
}
