package com.ve.lib.application.skin

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.LayoutInflaterCompat
import com.ve.lib.application.R
import com.ve.lib.application.skin.factory.SkinFactory
import com.ve.lib.application.skin.factory.SkinLoader
import java.io.File

open class SkinCompatActivity: AppCompatActivity() {

    private val layoutFactory2 = SkinFactory(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        LayoutInflaterCompat.setFactory2(layoutInflater, layoutFactory2)
        super.onCreate(savedInstanceState)
    }

    private fun changeSkin() {
        SkinLoader.instance.changeTheme(this, R.style.ThemeSakura)
//        layoutFactory2.changeSkin(this)
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

//        val addAttr = layoutFactory2.dynamicAddSkin(textView)
//            .addAttr("text", R.string.test_string)
//            .addAttr("textColor", R.color.skin_test_color)
//
//        mLL.addView(textView, ViewGroup.LayoutParams(
//            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
//        ))
//        layoutFactory2.changAttrView(this, addAttr)
//
//        textView.setOnClickListener {
//            mLL.removeView(it)
//        }
    }
}