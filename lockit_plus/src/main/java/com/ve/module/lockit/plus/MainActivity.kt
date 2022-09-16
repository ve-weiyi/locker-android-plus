package com.ve.module.lockit.plus

import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Route
import com.ve.lib.application.skin.SkinCompatActivity

import com.ve.lib.common.router.ARouterPath
import com.ve.module.lockit.plus.databinding.ActivityMainBinding


@Route(path = ARouterPath.MAIN_HOME)
class MainActivity : SkinCompatActivity() {

    lateinit var mBinding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
//        if ("default" != getSp(this, "theme")) {
//            setTheme(R.style.AppTheme_Sakura)
//        }
        super.onCreate(savedInstanceState)
        mBinding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        mBinding.btn1.setOnClickListener {
            onChangeTheme2()
        }
        mBinding.btn2.setOnClickListener {
            onChangeTheme2()
        }
    }

//    private fun toggleTheme() {
//        if ("default" == getSp(this, "theme")) {
//            putSp(this, "theme", "")
//        } else {
//            putSp(this, "theme", "default")
//        }
//    }
//
//    private fun onChangeTheme2() {
//        toggleTheme()
//        val intent = intent
//        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
//        finish()
//        overridePendingTransition(0, 0) //不设置进入退出动画
//        startActivity(intent)
//        overridePendingTransition(0, 0)
//    }
}