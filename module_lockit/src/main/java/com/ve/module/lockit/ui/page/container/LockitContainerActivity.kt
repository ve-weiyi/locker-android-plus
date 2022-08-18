package com.ve.module.lockit.ui.page.container

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.ve.lib.common.base.view.vm.BaseActivity
import com.ve.lib.common.utils.log.LogUtil
import com.ve.module.lockit.R
import com.ve.module.lockit.databinding.LockitActivityContainerBinding

/**
 * @Author  weiyi
 * @Date 2022/4/12
 * @Description  current project lockit-android
 */
class LockitContainerActivity :BaseActivity<LockitActivityContainerBinding>(){

    override fun attachViewBinding(): LockitActivityContainerBinding {
        return LockitActivityContainerBinding.inflate(layoutInflater)
    }

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
//        @Deprecated("这个方法已废弃，请使用start(this,Fragment,title, bundle)")
        fun start(context: Context, fragmentClassName: String, title: String? = null, fragmentBundle: Bundle? = null) {
            Intent(context, LockitContainerActivity::class.java).run {
                //启动模式
//                flags=Intent.FLAG_ACTIVITY_SINGLE_TOP
                putExtra(FRAGMENT_TITLE_KEY, title)
                putExtra(FRAGMENT_CLASS_NAME_KEY, fragmentClassName)
                putExtra(FRAGMENT_ARGUMENTS_KEY,fragmentBundle)
                //或者
//                if (fragmentBundle != null) {
//                    putExtras(fragmentBundle)
//                }
                LogUtil.msg("start to "+fragmentClassName)
                //startActivity(this,fragmentBundle) 会报错
                context.startActivity(this,)
            }
        }

        @Deprecated("已废弃，Intent 无法传递 class 类型", replaceWith = ReplaceWith("this.start(context,fragmentClass.name,title,fragmentBundle)"))
        fun start(context: Context, fragmentClass:Class<*>, title: String? = null, fragmentBundle: Bundle? = null) {
            val fragmentClassName=fragmentClass.name
            start(context, fragmentClassName, title, fragmentBundle)
        }
    }



    lateinit var fragmentTitle: String
    lateinit var fragmentClassName: String
    lateinit var fragmentArguments: Bundle

    override fun initialize(saveInstanceState: Bundle?) {

        //从bundle中取出数据
        fragmentTitle  = intent.getStringExtra(FRAGMENT_TITLE_KEY) ?: ""
        fragmentClassName = intent.getStringExtra(FRAGMENT_CLASS_NAME_KEY) ?: ""
        fragmentArguments = intent.getBundleExtra(FRAGMENT_ARGUMENTS_KEY) ?: Bundle()


        if(fragmentClassName.isEmpty()){
            LogUtil.msg("fragment class name is null")
        }else{
            transactionFragment(fragmentClassName,fragmentArguments)
        }
        if(fragmentTitle.isEmpty()){
            mBinding.extToolbar.appBarLayout.visibility=View.GONE
        }else{
            initToolbar(mBinding.extToolbar.toolbar,fragmentTitle)
        }
    }

    /**
     * 如果需要把 title ,className 也传过去，可以transactionFragment(fragmentClassName,saveInstanceState)
     */
    private fun transactionFragment(fragmentClassName: String, bundle: Bundle? = null) {
        val fragmentClass = Class.forName(fragmentClassName) //完整类名
        val fragment = fragmentClass.newInstance() as Fragment
        fragment.arguments = bundle
        supportFragmentManager.beginTransaction()
            .replace(R.id.ext_container, fragment, fragmentTitle)
            .commit()
    }
}