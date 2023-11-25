package com.ve.module.sunny

import com.ve.lib.common.base.view.vm.BaseVBActivity
import com.ve.module.sunny.databinding.ActivitySunnyBinding

class SunnyActivity : BaseVBActivity<ActivitySunnyBinding>() {

    companion object {
        const val FRUIT_NAME = "game_name"
        const val FRUIT_IMAGE_ID = "game_image_id"
    }

    override fun attachViewBinding(): ActivitySunnyBinding {
        return ActivitySunnyBinding.inflate(layoutInflater)
    }

    override fun initialize() {

    }
}
