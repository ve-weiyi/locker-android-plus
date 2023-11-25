package com.ve.module.lockit.plus.ui.page.home.model

/**
 * @author waynie
 * @date 2022/10/12
 * @desc lockit-android
 */
object HomeAdapterType {


    class Bulb{
        companion object{
            const val Single=0
            const val Multi=1
        }
    }

    class Device{
        companion object{
            const val CARD = 0
            const val GRID = 1
            const val LINEAR = 2
        }
    }

    public class Code {
        companion object {
            const val Robot = "扫地机"
            const val Wetdry = "洗地机"
            const val Bulb = "球泡灯"
            const val Plug = "智能插座"
            const val Switch = "智能开关"
            const val Genie = "精灵"
        }
    }
}