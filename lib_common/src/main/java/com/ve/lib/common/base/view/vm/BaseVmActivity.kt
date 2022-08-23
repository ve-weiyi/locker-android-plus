package com.ve.lib.common.base.view.vm

import android.content.Context
import android.graphics.PixelFormat
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.lifecycle.ViewModelProvider
import androidx.viewbinding.ViewBinding
import com.ve.lib.common.R
import com.ve.lib.common.base.viewmodel.BaseViewModel
import com.ve.lib.common.config.AppConfig
import com.ve.lib.common.event.NetworkChangeEvent
import com.ve.lib.common.network.util.NetWorkUtil
import com.ve.lib.common.widget.multipleview.MultipleStatusView
import com.ve.lib.common.utils.sp.PreferenceUtil
import com.ve.lib.common.utils.view.ToastUtil
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * @author chenxz
 * @date 2019/11/1
 * @desc BaseVMActivity
 */
abstract class BaseVmActivity<VB : ViewBinding, VM : BaseViewModel> : BaseActivity<VB>() ,
    IVmView<VM> {

    override var hasLoadData: Boolean =false
    override var isLogin: Boolean by PreferenceUtil(AppConfig.LOGIN_KEY, false)
    override var hasNetwork: Boolean by PreferenceUtil(AppConfig.HAS_NETWORK_KEY, true)
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


    override fun initialize(saveInstanceState: Bundle?) {
        mViewModel = ViewModelProvider(this).get(attachViewModelClass())

        initTipView()
        initObserver()
        initView()
        initListener()
        mLayoutStatusView?.setOnClickListener(mRetryClickListener)
        checkNetwork(NetWorkUtil.isConnected())
    }

    open val mRetryClickListener: View.OnClickListener = View.OnClickListener {
        initWebData()
    }

    /**
     * step 1.初始化 liveData.observe.订阅
     * 基类订阅，有逻辑的话，复写的时候super不要去掉
     */
    override fun initObserver() {

    }

    /**
     * step 4.设置监听器
     */
    override fun initListener(){

    }
    /**
     * step 5.填充界面时所需要的data,从仓库获取或者网络抓取.
     * 这个方法应在onResume()中调用
     */
    override fun initWebData(){

    }

    override fun onResume() {
        super.onResume()
        if (!hasLoadData){
            showLoading()
            initWebData()
            hasLoadData=true
        }
    }

    /**
     * 初始化 TipView
     */
    private fun initTipView() {
        mTipView = layoutInflater.inflate(R.layout.layout_network_tip, null)
        mWindowManager = getSystemService(Context.WINDOW_SERVICE) as WindowManager
        mLayoutParams = WindowManager.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.TYPE_APPLICATION,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
            PixelFormat.TRANSLUCENT
        )
        mLayoutParams.gravity = Gravity.TOP
        mLayoutParams.x = 0
        mLayoutParams.y = 0
    }

    /**
     * Network Change
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onNetworkChangeEvent(event: NetworkChangeEvent) {
        hasNetwork = event.isConnected
        checkNetwork(event.isConnected)
    }

    private fun checkNetwork(isConnected: Boolean) {
        if (enableNetworkTip()) {
            if (isConnected) {
                doReConnected()
                if (mTipView != null && mTipView?.parent != null) {
                    mWindowManager.removeView(mTipView)
                }
            } else {
                if (mTipView?.parent == null) {
                    mTipView?.setOnClickListener {
                        mTipView?.visibility=View.GONE
                    }
                    mWindowManager.addView(mTipView, mLayoutParams)
                }
            }
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