package com.ve.lib.application.skin.set

import android.content.Context
import com.ve.lib.application.R
import com.ve.lib.application.skin.ThemeCompatActivity

object SkinManager {
    private const val SP_SKIN_THEME = "sp_skin_theme"

    fun setTheme(skinActivity: ThemeCompatActivity) {
        val theme = getSp(skinActivity, SP_SKIN_THEME)
        when (theme) {
            R.style.AppTheme -> {
                skinActivity.setTheme(R.style.AppTheme)
            }
            R.style.ThemeNight -> {
                skinActivity.setTheme(R.style.ThemeNight)
            }
            R.style.ThemeSakura -> {
                skinActivity.setTheme(R.style.ThemeSakura)
            }
            else->{
                skinActivity.setTheme(R.style.AppTheme)
            }
        }
    }

    fun switchTheme(skinActivity: ThemeCompatActivity, theme: Int) {

        skinActivity.setTheme(theme)
        putSp(skinActivity, SP_SKIN_THEME, theme)
    }


    private fun putSp(context: Context, key: String, value: Int) {
        context.getSharedPreferences("default", Context.MODE_PRIVATE).also {
            it.edit().putInt(key, value).apply()
        }
    }

    private fun getSp(context: Context, key: String) =
        context.getSharedPreferences("default", Context.MODE_PRIVATE).getInt(key, 0)
}