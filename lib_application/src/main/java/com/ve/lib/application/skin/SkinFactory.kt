package com.ve.lib.application.skin

import android.content.Context
import com.ve.lib.application.R

object SkinFactory {

    private const val SP_SKIN_THEME="sp_skin_theme"

    fun setTheme(skinActivity: SkinCompatActivity) {
        if ("" != getSp(skinActivity, SP_SKIN_THEME)) {
            skinActivity.setTheme(R.style.AppTheme_Sakura)
        }
    }

    fun changeTheme(skinActivity: SkinCompatActivity){
        if ("default" == getSp(skinActivity, SP_SKIN_THEME)) {
            putSp(skinActivity, SP_SKIN_THEME, "")
        } else {
            putSp(skinActivity, SP_SKIN_THEME, "default")
        }
    }
    private fun putSp(context: Context, key: String, value: String) {
        context.getSharedPreferences("default", Context.MODE_PRIVATE).also {
            it.edit().putString(key, value).apply()
        }
    }

    private fun getSp(context: Context, key: String) =
        context.getSharedPreferences("default", Context.MODE_PRIVATE).getString(key, "")
}