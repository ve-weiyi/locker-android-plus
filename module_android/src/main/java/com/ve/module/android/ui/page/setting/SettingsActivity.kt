package com.ve.module.android.ui.page.setting

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.ve.lib.common.base.view.vm.BaseActivity
import com.ve.module.android.R
import com.ve.module.android.databinding.ActivitySettingsBinding


class SettingsActivity : BaseActivity<ActivitySettingsBinding>(){

    private val EXTRA_SHOW_FRAGMENT = "show_fragment"
    private val EXTRA_SHOW_FRAGMENT_ARGUMENTS = "show_fragment_args"
    private val EXTRA_SHOW_FRAGMENT_TITLE = "show_fragment_title"

    override fun attachViewBinding(): ActivitySettingsBinding {
        return ActivitySettingsBinding.inflate(layoutInflater)
    }

    override fun initialize(saveInstanceState: Bundle?) {

        val initFragment: String = intent.getStringExtra(EXTRA_SHOW_FRAGMENT) ?: ""
        val initArguments: Bundle = intent.getBundleExtra(EXTRA_SHOW_FRAGMENT_ARGUMENTS) ?: Bundle()
        val initTitle: String = intent.getStringExtra(EXTRA_SHOW_FRAGMENT_TITLE)
            ?: "设置"

//        if (savedInstanceState == null) {
//            supportFragmentManager
//                .beginTransaction()
//                .replace(R.id.settings_container, SettingsFragment())
//                .commit()
//        }

        if (initFragment.isEmpty()) {
            //java.name是包含路径名，初始化时用
            setupFragment(SettingsFragment::class.java.name, initArguments)
        } else {
            setupFragment(initFragment, initArguments)
        }


        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        mBinding.extToolbar.toolbar.run {
            title = initTitle
            setSupportActionBar(this)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }

    }

    private fun setupFragment(fragmentName: String, args: Bundle) {

        val c=Class.forName(fragmentName)
        val fragment: Fragment = c.newInstance() as Fragment
        //反射获得类
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.settings_container, fragment)
            .commit()
    }

    private fun onBuildStartFragmentIntent(fragmentName: String, args: Bundle?, title: String?): Intent {
        val intent = Intent(Intent.ACTION_MAIN)
        intent.setClass(this, javaClass)
        intent.putExtra(EXTRA_SHOW_FRAGMENT, fragmentName)
        intent.putExtra(EXTRA_SHOW_FRAGMENT_ARGUMENTS, args)
        intent.putExtra(EXTRA_SHOW_FRAGMENT_TITLE, title)
        return intent
    }


    fun startWithFragment(
        fragmentName: String, args: Bundle?,
        resultTo: Fragment?, resultRequestCode: Int, title: String?
    ) {
        val intent = onBuildStartFragmentIntent(fragmentName, args, title)
        if (resultTo == null) {
            startActivity(intent)
        } else {
            //startActivityForResult()替代方案 registerForActivityResult
            resultTo.startActivityForResult(intent, resultRequestCode)
        }
    }

//    //
//    override fun onColorChooserDismissed(dialog: ColorChooserDialog) {
//    }
//
//    //选择颜色回调
//    override fun onColorSelection(dialog: ColorChooserDialog, selectedColor: Int) {
//        if (!dialog.isAccentMode) {
//            //设置主题颜色
//            SettingUtil.setColor(selectedColor)
//        }
//        initColor()
//        EventBus.getDefault().post(ColorEvent(true))
//    }

}