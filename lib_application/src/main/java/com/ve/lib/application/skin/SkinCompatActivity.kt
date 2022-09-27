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

    protected val layoutFactory2 = SkinFactory(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        LayoutInflaterCompat.setFactory2(layoutInflater, layoutFactory2)
        super.onCreate(savedInstanceState)
    }



}