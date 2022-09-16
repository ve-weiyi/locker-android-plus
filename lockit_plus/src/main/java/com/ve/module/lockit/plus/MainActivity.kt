package com.ve.module.lockit.plus

import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Route
import com.gyf.immersionbar.ImmersionBar
import com.ve.lib.application.skin.SkinCompatActivity
import com.ve.lib.common.base.view.vm.BaseActivity

import com.ve.lib.common.router.ARouterPath
import com.ve.module.lockit.plus.databinding.ActivityMainBinding


@Route(path = ARouterPath.MAIN_HOME)
class MainActivity : BaseActivity<ActivityMainBinding>(){
    override fun attachViewBinding(): ActivityMainBinding {
        return ActivityMainBinding.inflate(layoutInflater)
    }

    override fun initialize(saveInstanceState: Bundle?) {
        initHeaderInfo(mBinding.extToolbar,"首页", enableBack = false)

    }

}