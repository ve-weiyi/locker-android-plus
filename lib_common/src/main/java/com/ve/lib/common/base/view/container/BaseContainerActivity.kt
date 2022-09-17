package com.ve.lib.common.base.view.container

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.ve.lib.common.base.view.vm.BaseActivity
import com.ve.lib.common.utils.system.LogUtil

/**
 * @Author  weiyi
 * @Date 2022/4/13
 * @Description  current project lockit-android
 */
abstract class BaseContainerActivity<VB : ViewBinding> : BaseActivity<VB>() {

    companion object {

        const val FRAGMENT_TITLE_KEY: String = "ContainerActivity.title"
        const val FRAGMENT_CLASS_NAME_KEY: String = "ContainerActivity.fragment"
        const val FRAGMENT_ARGUMENTS_KEY = "ContainerActivity.args"

        /**
         * person.javaClass == person::class.java == Person::class.java
         * person.javaClass != Person::class
         * className 请使用Fragment::class.java.name，不要使用 Fragment.javaClass.name
         * ContainerActivity.start(this,title,Fragment::class.java.name, bundle)
         */
        fun start(context: Context, fragmentClassName: String, title: String? = null, fragmentBundle: Bundle? = null) {
            Intent(context, this::class.java).run {
                putExtra(FRAGMENT_TITLE_KEY, title)
                putExtra(FRAGMENT_CLASS_NAME_KEY, fragmentClassName)
                putExtra(FRAGMENT_ARGUMENTS_KEY,fragmentBundle)
                //或者
//                if (fragmentBundle != null) {
//                    putExtras(fragmentBundle)
//                }
                context.startActivity(this)
            }
        }
    }

    lateinit var fragmentTitle: String
    lateinit var fragmentClassName: String
    lateinit var fragmentArguments: Bundle

    override fun initialize(saveInstanceState: Bundle?) {

        //从bundle中取出数据
        fragmentTitle  = intent.getStringExtra(FRAGMENT_TITLE_KEY) ?: "标题"
        fragmentClassName = intent.getStringExtra(FRAGMENT_CLASS_NAME_KEY) ?: ""
        fragmentArguments = intent.getBundleExtra(FRAGMENT_ARGUMENTS_KEY) ?: Bundle()

        if(fragmentClassName.isEmpty()){
            LogUtil.msg("fragment class name is null")
        }else{
            transactionFragment(fragmentClassName,fragmentArguments)
        }
    }

    /**
     * PS:
     * 通过FragmentManager的FragmentTransaction添加Fragment时，
     * add方法的第二个参数tag不要直接传入TestFragment::class.java.simpleName，
     * 因为一旦Fragment发生了混淆，可能会出现多个添加的不同Fragment的tag相同的情况，影响后续使用
     */
    protected fun transactionFragment(fragmentClassName: String, bundle: Bundle? = null) {
        val fragmentClass = Class.forName(fragmentClassName) //完整类名
        val fragment = fragmentClass.newInstance() as Fragment
        fragment.arguments = bundle
        supportFragmentManager.beginTransaction()
            .replace(attachContainerResId(), fragment, fragmentTitle)
            .commit()
    }

    abstract fun attachContainerResId(): Int
}