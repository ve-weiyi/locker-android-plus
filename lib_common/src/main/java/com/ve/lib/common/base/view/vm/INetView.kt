package com.ve.lib.common.base.view.vm

import com.ve.lib.view.widget.multipleview.MultipleStatusView

/**
 * @Description hello word!
 * @Author  weiyi
 * @Date 2022/3/19
 */
interface INetView {

//    /**
//     * check login
//     */
//    var isLogin: Boolean
//
//    /**
//     * 缓存上一次的网络状态
//     */
//    var hasNetwork: Boolean
    
    /**
     * 多种状态的 View 的切换
     */
    var mLayoutStatusView: MultipleStatusView?
    
    
    /**
     * 无网状态—>有网状态 的自动重连操作，子类可重写该方法
     */
    open fun doReConnected() {
       
    }
}