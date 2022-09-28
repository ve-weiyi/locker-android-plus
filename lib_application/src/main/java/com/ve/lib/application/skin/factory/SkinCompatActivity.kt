package com.ve.lib.application.skin.factory

import android.content.Context
import android.os.Bundle
import android.util.AttributeSet
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.LayoutInflaterCompat
import androidx.core.view.ViewCompat
import com.ve.lib.application.R
import com.ve.lib.application.skin.factory.SkinEngine
import com.ve.lib.application.skin.factory.SkinFactory
import com.ve.lib.application.utils.LogUtil

open class SkinCompatActivity: AppCompatActivity() {

    protected lateinit var layoutFactory2 :SkinFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        layoutFactory2 = SkinFactory(this)
        SkinEngine.registerSkinFactory(layoutFactory2)
        super.onCreate(savedInstanceState)

    }


}
