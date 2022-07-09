package com.ve.module.locker.ui.page.setting

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.ve.lib.common.base.view.container.BaseContainerActivity
import com.ve.lib.common.base.view.vm.BaseActivity
import com.ve.lib.common.vutils.LogUtil
import com.ve.module.locker.R
import com.ve.module.locker.databinding.LockerActivitySettingBinding

/**
 * @Author  weiyi
 * @Date 2022/4/13
 * @Description  current project locker-android
 *
 * 设置页面容器
 */
class LockerSettingActivity: BaseActivity<LockerActivitySettingBinding>(){

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
        fun start(context: Context, fragmentClassName: String?=null, title: String? = null, fragmentBundle: Bundle? = null) {
            Intent(context, LockerSettingActivity::class.java).run {
                putExtra(FRAGMENT_TITLE_KEY, title)
                putExtra(FRAGMENT_CLASS_NAME_KEY, fragmentClassName)
                putExtra(FRAGMENT_ARGUMENTS_KEY,fragmentBundle)
                //启动模式
//                flags=Intent.FLAG_ACTIVITY_SINGLE_TOP
                context.startActivity(this)
            }
        }
    }

    lateinit var fragmentTitle: String
    lateinit var fragmentClassName: String
    lateinit var fragmentArguments: Bundle

    override fun attachViewBinding(): LockerActivitySettingBinding {
        return LockerActivitySettingBinding.inflate(layoutInflater)
    }

    override fun initialize(saveInstanceState: Bundle?) {
        //从bundle中取出数据
        fragmentTitle  = intent.getStringExtra(BaseContainerActivity.FRAGMENT_TITLE_KEY) ?: "设置"
        fragmentClassName = intent.getStringExtra(BaseContainerActivity.FRAGMENT_CLASS_NAME_KEY) ?: ""
        fragmentArguments = intent.getBundleExtra(BaseContainerActivity.FRAGMENT_ARGUMENTS_KEY) ?: Bundle()

        LogUtil.msg(fragmentTitle+fragmentClassName+fragmentArguments)
        initToolbar(mBinding.extToolbar.toolbar,fragmentTitle)

        if(fragmentClassName.isEmpty()){
            LogUtil.msg("fragment class name is null")
            transactionFragment(LockerSettingFragment::class.java.name,fragmentArguments)
        }else{
            transactionFragment(fragmentClassName,fragmentArguments)
        }
    }

    protected fun transactionFragment(fragmentClassName: String, bundle: Bundle? = null) {
        val fragmentClass = Class.forName(fragmentClassName) //完整类名
        val fragment = fragmentClass.newInstance() as Fragment
        fragment.arguments = bundle
        supportFragmentManager.beginTransaction()
            .replace(R.id.ext_container, fragment, fragmentTitle)
            .commit()
    }

}