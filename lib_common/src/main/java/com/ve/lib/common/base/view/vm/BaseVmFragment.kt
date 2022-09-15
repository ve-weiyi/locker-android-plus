package com.ve.lib.common.base.view.vm

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.viewbinding.ViewBinding
import com.ve.lib.common.base.viewmodel.BaseViewModel
import com.ve.lib.common.utils.log.LogUtil
import com.ve.lib.common.utils.view.ToastUtil
import com.ve.lib.common.widget.multipleview.MultipleStatusView

/**
 * @author chenxz
 * @date 2019/11/1
 * @desc BaseVMFragment
 */
abstract class BaseVmFragment<VB : ViewBinding, VM : BaseViewModel> : BaseFragment<VB>(),
    IVmView<VM> {

    companion object {
        fun Fragment.getInstance(): Fragment {
            return javaClass.newInstance()
        }

        fun Fragment.newInstance(): Fragment {
            return javaClass.newInstance()
        }
    }

    override var hasLoadData: Boolean = false
    override var mLayoutStatusView: MultipleStatusView? = null

    lateinit var mViewModel: VM

    override fun useEventBus() = false

    override fun initialize(saveInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        mViewModel = ViewModelProvider(this).get(attachViewModelClass())
        //我不理解
        arguments?.let {
            val bundle: Bundle? = arguments //从bundle中取出数据
            LogUtil.e(mViewName + bundle.toString())
        }

        initObserver()
        initView()
        initListener()

//        arguments?.let {
//            val bundle: Bundle? = arguments //从bundle中取出数据
//            LogUtil.e(bundle.toString())
//        }
        //多种状态切换的view 重试点击事件;初始化后设置
        mLayoutStatusView?.setOnClickListener(mRetryClickListener)
    }

    open val mRetryClickListener: View.OnClickListener = View.OnClickListener {
        initWebData()
        //点击时显示原来内容
        //mLayoutStatusView?.showContent()
    }

    override fun onResume() {
        super.onResume()
        if (!hasLoadData) {
            showLoading()
            initWebData()
            hasLoadData = true
        }
    }


    /**
     * step 1.初始化 liveData.observe.订阅
     * 基类订阅，有逻辑的话，复写的时候super不要去掉
     */
    override fun initObserver() {

    }


    /**
     * step 4.填充界面时所需要的data,从仓库获取或者网络抓取
     */
    override fun initWebData() {

    }

    /**
     * step 5.设置监听器
     */
    override fun initListener() {

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