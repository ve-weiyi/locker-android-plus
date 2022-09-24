package com.ve.lib.application.skin

import android.content.Context
import com.ve.lib.application.R

object SkinFactory {
    private const val SP_SKIN_THEME = "sp_skin_theme"


    fun setTheme(skinActivity: SkinCompatActivity) {
        val theme= getSp(skinActivity,SP_SKIN_THEME)
        when (theme) {
            SkinConstants.THEME_DEFAULT -> {
                skinActivity.setTheme(R.style.AppTheme)
            }
            SkinConstants.THEME_NIGHT -> {
                skinActivity.setTheme(R.style.ThemeNight)
            }
            SkinConstants.THEME_SAKURA -> {
                skinActivity.setTheme(R.style.ThemeSakura)
            }
        }
    }

    fun switchTheme(skinActivity: SkinCompatActivity, theme: String = SkinConstants.THEME_DEFAULT) {
        when (theme) {
            SkinConstants.THEME_DEFAULT -> {
                skinActivity.setTheme(R.style.AppTheme)
            }
            SkinConstants.THEME_NIGHT -> {
                skinActivity.setTheme(R.style.ThemeNight)
            }
            SkinConstants.THEME_SAKURA -> {
                skinActivity.setTheme(R.style.ThemeSakura)
            }
        }
        putSp(skinActivity, SP_SKIN_THEME, theme)
    }


    private fun putSp(context: Context, key: String, value: String) {
        context.getSharedPreferences("default", Context.MODE_PRIVATE).also {
            it.edit().putString(key, value).apply()
        }
    }

    private fun getSp(context: Context, key: String) =
        context.getSharedPreferences("default", Context.MODE_PRIVATE).getString(key, "")
}