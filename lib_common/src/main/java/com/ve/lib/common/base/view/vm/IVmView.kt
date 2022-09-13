package com.ve.lib.common.base.view.vm

import android.os.Bundle
import com.ve.lib.common.base.viewmodel.BaseViewModel

/**
 * @Description 懒加载页面
 *
 * 懒加载一般用于tablayout viewpager 页面
 * 但是我们希望在actviity可见的时候,只显示当前的fragment,这个时候呢,第一个可见的fragment数据加载了,
 * 但是其他fragment此时是不可见的,那么就不是很希望将其他几个fragment的数据也加载了;
 * @Author  weiyi
 * @Date 2022/3/19
 */
interface IVmView<VM : BaseViewModel> : INetView {

    /**
     * 数据是否加载过了
     */
    var hasLoadData: Boolean

    /**
     * 获取ViewModel的class
     * return MainViewModel::class.java
     */
    fun attachViewModelClass(): Class<VM>

    /**
     * step 1.初始化 liveData.observe.订阅
     */
    fun initObserver()

    /**
     * step 2.初始化view相关数据, 需要在view初始化之前完成
     */
    fun initData()

    /**
     * step 3.初始化view相关, 绑定数据在此时完成
     */
    fun initView()

    /**
     * step 4.填充界面时所需要的data,从仓库获取或者网络抓取
     */
    fun initWebData()

    /**
     * step 5.设置监听器
     */
    fun initListener()

}