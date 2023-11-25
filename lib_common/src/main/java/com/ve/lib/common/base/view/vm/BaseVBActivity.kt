package com.ve.lib.common.base.view.vm

import android.content.Context
import android.content.pm.ActivityInfo
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.Toolbar
import androidx.viewbinding.ViewBinding
import com.ve.lib.application.skin.ThemeCompatActivity
import com.ve.lib.application.utils.LogUtil
import com.ve.lib.common.databinding.CommonToolbarBinding
import com.ve.lib.common.lifecycle.EventBusLifecycle
import com.ve.lib.common.utils.system.KeyBoardUtil
import com.ve.lib.common.utils.view.ToastUtil

/**
 * @author chenxz
 * @date 2018/11/19
 * @desc BaseVBActivity 泛型实化 ，内部存有binding对象
 */
abstract class BaseVBActivity<VB : ViewBinding> : BaseActivity() {

    lateinit var mBinding: VB
    /**
     * 返回绑定对象
     * return ActivityMainBinding.inflate(layoutInflater)
     */
    abstract fun attachViewBinding(): VB


    override fun initLayoutView() {
        mBinding = attachViewBinding()
        setContentView(mBinding.root)
    }


}
