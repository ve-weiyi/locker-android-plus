package com.ve.lib.common.base.view.list

/**
 * @Author  weiyi
 * @Date 2022/4/13
 * @Description  current project lockit-android
 * 开启拖拽、侧滑、加载更多、自动加载
 * 否则可能会崩溃
 */
class ListViewConfig(
    /**
     * 开启刷新
     */
    var enableRefresh: Boolean = false,

    /**
     * 开启加载更多数据
     */
    var enableLoadMore: Boolean = false,

    /**
     * 当数据不满一页时，自动加载
     */
    var enableAutoLoadMore: Boolean = false,

    /**
     *     允许拖动
     */
    var enableDrag: Boolean = false,

    /**
     *     允许侧滑
     */
    var enableSwipe: Boolean = false,

    /**
     *     向上加载更多
     */
    var enableUpFetch: Boolean = false,

) {


    init {

    }

    companion object{
        /**
         * 全部功能开启
         */
        const val ALL_OPEN=0;
        /**
         * 全部功能关闭
         */
        const val ALL_CLOSE=1;
        /**
         * 自定义功能
         */
        const val DIY=2;

        /**
         * 没有新数据
         */
        const val NO_NEW_DATA=4

        /**
         * 只支持刷新和加载更多
         */
        const val REFRASH_AND_LOADMORE=5
    }
}