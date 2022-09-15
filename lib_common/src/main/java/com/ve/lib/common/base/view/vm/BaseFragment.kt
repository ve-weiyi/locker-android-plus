package com.ve.lib.common.base.view.vm

import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.ve.lib.common.utils.log.LogUtil
import org.greenrobot.eventbus.EventBus

/**
 * @author chenxz
 * @date 2018/11/19
 * @desc BaseFragment
 */
abstract class BaseFragment<VB : ViewBinding> : Fragment() , IView<VB> {

    /** 当前fragment的视图绑定 */
    protected open var binding: VB? = null
    protected open val mBinding get() = binding!!
    protected open var mViewName: String  = this.javaClass.simpleName
    protected val mContext:Context by lazy { requireActivity() }
    protected var mEventBus:EventBus?=null


    /**
     * 是否使用 EventBus
     */
    open fun useEventBus(): Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = attachViewBinding()
        return mBinding.root
    }

    /**
     * 视图创建完毕
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        LogUtil.d("$mViewName onCreate")
        //注册订阅者
        if (useEventBus()) {
            if (!EventBus.getDefault().isRegistered(this))//加上判断,注销订阅者
            {
                mEventBus=EventBus.getDefault()
                EventBus.getDefault().register(this)
            }
        }
        initialize(savedInstanceState)
    }


    /**
     * 懒加载，当Fragment显示的时候再请求数据
     * 如果数据不需要每次都刷新，可以先判断数据是否存在
     * 不存在 -> 请求数据，存在 -> 什么都不做
     */
    override fun onResume() {
        super.onResume()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (EventBus.getDefault().isRegistered(this))//加上判断,注销订阅者
            EventBus.getDefault().unregister(this)
        binding = null
        LogUtil.d(javaClass.simpleName+" onDestroy")
    }



    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        //inflater.inflate(R.menu.menu_search, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
//            else -> {
//                showMsg("该功能未实现，请留意后续版本。"+item.title+"-->"+item.itemId)
//            }
        }
        return super.onOptionsItemSelected(item)
    }
}