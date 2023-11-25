package com.ve.module.lockit.plus.ui.page.test

import android.content.res.ColorStateList
import android.content.res.Configuration
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.ve.module.lockit.plus.R

class ScrollingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scrolling)

        setSystemBarTheme(false)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
//        setSupportActionBar(toolbar)
//        if (supportActionBar != null) {
//            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
//        }


//        Glide.with(this).load(R.drawable.material_design_3).apply(new RequestOptions().fitCenter()).into(image_scrolling_top);
    }

    override fun onResume() {
        super.onResume()
//        val configuration = resources.configuration
//        if (configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
//            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)
//            val collapsing_toolbar_layout = findViewById<CollapsingToolbarLayout>(R.id.collapsing_toolbar_layout)
//            collapsing_toolbar_layout.setExpandedTitleTextColor(ColorStateList.valueOf(Color.TRANSPARENT))
//        } else {
//            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)
//        }
    }

    /**
     * Changes System Bar Theme.
     */
    private fun setSystemBarTheme(isDark: Boolean) {


        //        val controller = ViewCompat.getWindowInsetsController(window.decorView)
//        controller?.isAppearanceLightStatusBars = isDark

//        // 设置状态栏反色
//        controller?.isAppearanceLightStatusBars = true
//        // 取消状态栏反色
//        controller?.isAppearanceLightStatusBars = false
//        // 设置导航栏反色
//        controller?.isAppearanceLightNavigationBars = true
//        // 取消导航栏反色
//        controller?.isAppearanceLightNavigationBars = false
//        // 隐藏状态栏
//        controller?.hide(WindowInsets.Type.statusBars())
//        // 显示状态栏
//        controller?.show(WindowInsets.Type.statusBars())
//        // 隐藏导航栏
//        controller?.hide(WindowInsets.Type.navigationBars())
//        // 显示导航栏
//        controller?.show(WindowInsets.Type.navigationBars())
//        // 同时隐藏状态栏和导航栏
//        controller?.hide(WindowInsets.Type.systemBars())
//        // 同时隐藏状态栏和导航栏
//        controller?.show(WindowInsets.Type.systemBars())

        // Fetch the current flags.
        val curFlags = window.decorView.systemUiVisibility
        // Update the SystemUiVisibility dependening on whether we want a Light or Dark theme.
        window.decorView.systemUiVisibility =
            if (isDark) curFlags and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv() else curFlags or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
    }
}