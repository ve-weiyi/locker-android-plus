package com.ve.lib.common.base.view.vm

import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.ve.lib.common.lifecycle.EventBusLifecycle
import com.ve.lib.application.utils.LogUtil

/**
 * @author chenxz
 * @date 2018/11/19
 * @desc BaseVBFragment
 */
abstract class BaseVBFragment<VB : ViewBinding> : BaseFragment() {

    /** 当前fragment的视图绑定 */
    protected open var binding: VB? = null
    protected open val mBinding get() = binding!!

    /**
     * 返回绑定对象
     * return ActivityMainBinding.inflate(layoutInflater)
     */
    abstract fun attachViewBinding(): VB

    override fun attachLayoutView(): View? {
        binding = attachViewBinding()
        return mBinding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
        LogUtil.d(javaClass.simpleName+" onDestroy")
    }


}