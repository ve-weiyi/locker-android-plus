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
abstract class BaseVmActivity<VB : ViewBinding, VM : BaseViewModel> : BaseActivity<VB>() ,
    IVmView<VM> {

    override var hasLoadData: Boolean =false
    override var mLayoutStatusView: MultipleStatusView? = null

    lateinit var mViewModel: VM

    /**
     * 提示View
     */
    protected var mTipView: View ?=null
    protected lateinit var mWindowManager: WindowManager
    protected lateinit var mLayoutParams: WindowManager.LayoutParams

    /**
     * 是否需要显示 TipView
     */
    open fun enableNetworkTip(): Boolean = true


    override fun initialize() {
        mViewModel = ViewModelProvider(this).get(attachViewModelClass())

        initObserver()
        initView()
        initListener()
        mLayoutStatusView?.setOnClickListener(mRetryClickListener)
    }

    open val mRetryClickListener: View.OnClickListener = View.OnClickListener {
        loadWebData()
    }

    override fun initData() {

    }

    override fun initObserver() {

    }

    override fun initListener(){

    }
    /**
     * step 5.填充界面时所需要的data,从仓库获取或者网络抓取.
     * 这个方法应在onResume()中调用
     */
    override fun loadWebData(){

    }

    override fun onResume() {
        super.onResume()
        if (!hasLoadData){
            showLoading()
            loadWebData()
            hasLoadData=true
        }
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