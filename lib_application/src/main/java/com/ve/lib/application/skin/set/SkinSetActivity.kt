package com.ve.lib.application.skin.set

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.ve.lib.application.R
import com.ve.lib.application.skin.ThemeCompatActivity

class SkinSetActivity : ThemeCompatActivity() {

    private lateinit var mLL: LinearLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_skin_set)
        mLL = findViewById(R.id.ll_add_view)
    }

    val flagTheme=true

    fun resetSkin(view: View) {
        switchTheme(R.style.AppTheme)
    }

    fun onChangeSkin(view: View) {
        switchTheme(R.style.ThemeSakura)
    }


    private var plan = 0
    fun onAddView(view: View) {
        val textView = TextView(this)
        textView.text = "动态添加的控件"
        if (plan % 2 == 0) {
            textView.setTextColor(
                getThemeColor(
                    this,
                    R.attr.colorBrand,
                    Color.BLACK
                )
            )
        } else {
            textView.setTextColor(
                getThemeColor(
                    this,
                    R.attr.sc_btn,
                    Color.BLACK
                )
            )
        }
        plan++
        mLL.addView(
            textView, ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
            )
        )
    }
}