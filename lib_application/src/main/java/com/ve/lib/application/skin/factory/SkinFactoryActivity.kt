package com.ve.lib.application.skin.factory

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.ve.lib.application.BaseApplication.Companion.mContext
import com.ve.lib.application.R

class SkinFactoryActivity : SkinCompatActivity() {
    private lateinit var mLL: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_skin_factory)
        mLL = findViewById(R.id.ll_add_view)

    }
    fun onChangeSkin(view: View) {
        //io操作，需要异步，这里省略了
//        val file = File(getExternalFilesDir(null), "skin.apk")
//        SkinEngine.instance.loadResource(this, file.absolutePath)
        SkinEngine.changeTheme(this, R.style.ThemeSakura)
    }

    fun resetSkin(view: View) {
        SkinEngine.reset()
        SkinEngine.changeTheme(mContext, R.style.AppTheme)
    }

    fun onAddView(view: View) {
        val textView = TextView(this)

        val addAttr = layoutFactory2.dynamicAddSkin(textView)
            .addAttr("text", com.google.android.material.R.string.chip_text)
            .addAttr("textColor", R.color.colorAccent)

        mLL.addView(textView, ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
        ))

//        layoutFactory2.changAttrView(this.theme, addAttr)

        textView.setOnClickListener {
            mLL.removeView(it)
        }
    }
}