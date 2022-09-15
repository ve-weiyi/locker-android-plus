package com.ve.lib.common.base.view.vm

import android.content.Context
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.MenuItem
import android.view.MotionEvent
import android.view.WindowManager
import androidx.appcompat.widget.Toolbar
import androidx.viewbinding.ViewBinding
import com.ve.lib.application.skin.SkinActivity
import com.ve.lib.common.utils.log.LogUtil
import com.ve.lib.common.utils.system.KeyBoardUtil
import com.ve.lib.common.utils.view.ToastUtil
import org.greenrobot.eventbus.EventBus

/**
 * @author chenxz
 * @date 2018/11/19
 * @desc BaseActivity 泛型实化 ，内部存有binding对象
 */
abstract class BaseActivity<VB : ViewBinding> : SkinActivity(), IView<VB> {

    lateinit var mBinding: VB
    protected open var mViewName: String? = this.javaClass.simpleName


    protected val mContext: Context by lazy { this }

    /**
     * 是否使用 EventBus
     */
    open fun useEventBus(): Boolean = false

    /**
     * 1. onCreate()，创建时调用，初始化操作写在这里，如指定布局文件，成员变量绑定对应ID等。
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        LogUtil.d("$mViewName onCreate")
        /**************************/
        //设置窗口软键盘的交互模式,保证用户要进行输入的输入框肯定在用户的视野范围里面
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
        //由系统选择显示方向，不同的设备可能会有所不同。（旋转手机，界面会跟着旋转）
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED

        mBinding = attachViewBinding()
        setContentView(mBinding.root)

        if (useEventBus()) {
            EventBus.getDefault().register(this)
        }
        initialize(savedInstanceState)
    }


    /**
     * 设置标题栏，调用此方法后标题栏颜色与主题色保持一致
     */
    protected fun initToolbar(
        toolbar: Toolbar,
        title: String = "无标题",
        homeAsUpEnabled: Boolean = true
    ) {
        toolbar.title = title
        //需要先设置设置toolbar
        setSupportActionBar(toolbar)
        //左侧添加一个默认的返回图标
        supportActionBar?.setDisplayHomeAsUpEnabled(homeAsUpEnabled)
        //设置返回键可用
        supportActionBar?.setHomeButtonEnabled(homeAsUpEnabled)
    }


    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        if (ev?.action == MotionEvent.ACTION_UP) {
            val v = currentFocus
            // 如果不是落在EditText区域，则需要关闭输入法
            if (KeyBoardUtil.isHideKeyboard(v, ev)) {
                KeyBoardUtil.hideKeyBoard(this, v)
            }
        }
        return super.dispatchTouchEvent(ev)
    }

    /**
     * 返回键选中处理
     */
    //需要在onCreate设置 setHasOptionsMenu()
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                LogUtil.d(mViewName + "action id=" + item.itemId)
                onBackPressed()
                return true;
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        // Fragment 逐个出栈
        val count = supportFragmentManager.backStackEntryCount
        if (count == 0) {
            super.onBackPressed()
        } else {
            supportFragmentManager.popBackStack()
        }
    }

    /**
     * 3. onResume()，在活动准备好与用户交互时调用，此时活动一定处于栈顶，且在运行状态。
     *  onResume（）和onPause（）方法是调用比较频繁的，在这两个方法里面一般做很小耗时的操作，以增强用户体验。
     */
    override fun onResume() {
        // 动态注册网络变化广播

        super.onResume()
    }

    /**
     *  4.onPause()，准备去启动或恢复另一活动时调用，当系统遇到紧急情况需要恢复内存，那么onStop()，onDestory()可能不被执行，
     *  因此你应当在onPause里保存一些至关重要的状态属性，这些属性会被保存到物理内存中。但此方法执行速度一定要快，否则会影响新栈顶活动的使用。
     */
    override fun onPause() {
        super.onPause()
    }

    /**
     * 6.    onDestory()，被销毁前用，之后该Activity进入销毁状态，一般在这里释放内存。
     */
    override fun onDestroy() {
        super.onDestroy()
        if (useEventBus()) {
            EventBus.getDefault().unregister(this)
        }

        //ColorUtil.fixInputMethodManagerLeak(this)
        ToastUtil.release()
    }

    /**
     * 当activity有可能被系统回收的情况下，而且是在onStop()之前。注意是有可能，
     * 如果是已经确定会被销毁，比如用户按下了返回键，或者调用了finish()方法销毁activity，则onSaveInstanceState不会被调用。
     *
        总结下，onSaveInstanceState(Bundle outState)会在以下情况被调用：
        1、当用户按下HOME键时。
        2、从最近应用中选择运行其他的程序时。
        3、按下电源按键（关闭屏幕显示）时。
        4、从当前activity启动一个新的activity时。
        5、屏幕方向切换时(无论竖屏切横屏还是横屏切竖屏都会调用)。
    在前4种情况下，当前activity的生命周期为：
    onPause -> onSaveInstanceState -> onStop。
    比如第5种情况屏幕方向切换时，activity生命周期如下：
    onPause -> onSaveInstanceState -> onStop -> onDestroy -> onCreate -> onStart -> onRestoreInstanceState -> onResume

    而按HOME键返回桌面，又马上点击应用图标回到原来页面时，activity生命周期如下：
    onPause -> onSaveInstanceState -> onStop -> onRestart -> onStart -> onResume
    因为activity没有被系统回收，因此onRestoreInstanceState没有被调用。
     */
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
    }

    /**
     * onRestoreInstanceState什么时机被调用？
     * onRestoreInstanceState(Bundle savedInstanceState)只有在activity确实是被系统回收，重新创建activity的情况下才会被调用。

    三、onCreate()里也有Bundle参数，可以用来恢复数据，它和onRestoreInstanceState有什么区别？
    因为onSaveInstanceState 不一定会被调用，所以onCreate()里的Bundle参数可能为空，如果使用onCreate()来恢复数据，一定要做非空判断。
    而onRestoreInstanceState的Bundle参数一定不会是空值，因为它只有在上次activity被回收了才会调用。
    而且onRestoreInstanceState是在onStart()之后被调用的。有时候我们需要onCreate()中做的一些初始化完成之后再恢复数据，用onRestoreInstanceState会比较方便。
     */
    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
    }

    override fun onRestart() {
        LogUtil.msg(mViewName)
        super.onRestart()
    }
}
