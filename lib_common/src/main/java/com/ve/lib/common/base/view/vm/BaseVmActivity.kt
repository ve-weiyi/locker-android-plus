package com.ve.lib.common.base.view.vm

import android.view.View
import android.view.WindowManager
import androidx.lifecycle.ViewModelProvider
import androidx.viewbinding.ViewBinding
import com.ve.lib.common.base.viewmodel.BaseViewModel
import com.ve.lib.common.utils.view.ToastUtil
import com.ve.lib.view.widget.multipleview.MultipleStatusView

/**
 * @author chenxz
 * @date 2019/11/1
 * @desc BaseVMActivity
 */
abstract class BaseVmActivity<VB : ViewBinding, VM : BaseViewModel> : BaseVBActivity<VB>() ,
    IVmView {

    /**
     * 数据是否加载过了
     */
    protected var hasLoadData: Boolean =false
    protected var mLayoutStatusView: MultipleStatusView? = null

    lateinit var mViewModel: VM

    /**
     * 获取ViewModel的class
     * return MainViewModel::class.java
     */
    abstract fun attachViewModelClass(): Class<VM>



    /**
     * 提示View
     */
    protected var mTipView: View ?=null
    protected lateinit var mWindowManager: WindowManager
    protected lateinit var mLayoutParams: WindowManager.LayoutParams


    override fun initLayoutView() {
        super.initLayoutView()
        mViewModel = ViewModelProvider(this).get(attachViewModelClass())

    }


    override fun initialize() {
        initObserver()
        initView()
        initListener()
        mLayoutStatusView?.setOnClickListener(mRetryClickListener)
    }

    open val mRetryClickListener: View.OnClickListener = View.OnClickListener {
        loadWebData()
    }

    override fun onResume() {
        super.onResume()
        if (!hasLoadData){
            showLoading()
            loadWebData()
            hasLoadData=true
        }
    }

    override fun initObserver() {

    }

    override fun initListener(){

    }

    override fun loadWebData(){

    }


    override fun showLoading() {
        mLayoutStatusView?.showLoading()
    }

    override fun hideLoading() {
        mLayoutStatusView?.showContent()
    }

    override fun showMsg(msg: String) {
        ToastUtil.show(msg)
    }

    override fun showError(errorMsg: String) {
        ToastUtil.show(errorMsg)
        mLayoutStatusView?.showError()
    }

}