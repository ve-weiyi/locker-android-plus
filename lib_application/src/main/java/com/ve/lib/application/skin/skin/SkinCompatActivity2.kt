package com.ve.lib.application.skin.skin

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.LayoutInflaterCompat
import com.ve.lib.application.R
import java.io.File

open class SkinCompatActivity2: AppCompatActivity() {

    private val layoutFactory2 =SkinFactory(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        LayoutInflaterCompat.setFactory2(layoutInflater, layoutFactory2)
        super.onCreate(savedInstanceState)
    }

    private fun changeSkin() {
        layoutFactory2.changeSkin(this)
    }

    fun onChangeSkin(view: View) {
        //io操作，需要异步，这里省略了
        val file = File(getExternalFilesDir(null), "skin.apk")
        SkinLoader.instance.loadResource(this, file.absolutePath)
        changeSkin()
    }

    fun resetSkin(view: View) {
        SkinLoader.instance.reset()
        changeSkin()
    }

    fun onAddView(view: View) {
        val textView = TextView(this)

        val addAttr = layoutFactory2.dynamicAddSkin(textView)
//            .addAttr("text", R.string.test_string)
//            .addAttr("textColor", R.color.skin_test_color)
    }
}