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

//class SkinFactory (activity: AppCompatActivity) : LayoutInflater.Factory2 {
//
//    var key: String
//    private var delegate: AppCompatDelegate
//    private var context: Context
//    init {
//        key = activity.javaClass.name
//        this.delegate = activity.delegate
//        context = activity
//        LayoutInflaterCompat.setFactory2(activity.layoutInflater, this)
//    }
//    val attrViews: MutableList<AttrView> = mutableListOf()
//
//    override fun onCreateView(name: String, context: Context, attrs: AttributeSet): View? {
//        return null
//    }
//
//    override fun onCreateView(parent: View?, name: String, context: Context, attrs: AttributeSet): View? {
//
//        val obtainStyledAttributes = context.obtainStyledAttributes(attrs, R.styleable.SkinSupport)
//        val isEnable = obtainStyledAttributes.getBoolean(R.styleable.SkinSupport_enableSkin, true)
//        obtainStyledAttributes.recycle()
//        var createView: View? = null
//        //如果控件支持换肤
//        if (isEnable) {
//            LogUtil.msg("$parent  $name $context $attrs")
//            //调用系统方法创建控件
//            createView = delegate.createView(parent, name, context, attrs)
//            if(createView==null)
//                return null
//
//            val attrView = AttrView(createView)
//            for (i in 0 until attrs.attributeCount) {
//                val attributeName = attrs.getAttributeName(i)
//                //如果是支持换肤的属性
//                if (isSupportAttr(attributeName)) {
//                    val attributeValue = attrs.getAttributeValue(i)
//                    //# 直接写死的颜色 不处理
//                    //?2130903258 ?colorPrimary 这样的 解析主题，找到id，再去找资源名称和类型
//                    //@2131231208 @color/red 直接就是id，根据id找到资源名称和类型
//                    if (attributeValue.startsWith("?")) {
//                        val attrId = attributeValue.substring(1)
//                        val resIdFromTheme = getResIdFromTheme(context, attrId.toInt())
//                        if (resIdFromTheme > 0) {
//                            attrView.attrs.add(AttrView.AttrItem(attributeName, resIdFromTheme))
//                        }
//                    } else if (attributeValue.startsWith("@")) {
//                        attrView.attrs.add(
//                            AttrView.AttrItem(
//                                attributeName,
//                                attributeValue.substring(1).toInt()
//                            )
//                        )
//                    }
//                }
//            }
//            attrViews.add(attrView)
//        }
//        return createView
//    }
//
//    /**
//     * 解析主题，找到资源id，其实就是方案一里面的方法
//     */
//    private fun getResIdFromTheme(context: Context, attrId: Int): Int {
//        val typedValue = TypedValue()
//        val success = context.theme.resolveAttribute(attrId, typedValue, true)
//        //typedValue.resourceId 可能为0
//        return typedValue.resourceId
//    }
//
//    private fun isSupportAttr(attrName: String): Boolean {
//        if ("textColor" == attrName || "text" == attrName) {
//            return true
//        }
//        return false
//    }
//
//    fun changeSkin(context: Context) {
//        //这个是在Factory2中找到的所有支持换肤的控件
//        attrViews.forEach {
//            if (ViewCompat.isAttachedToWindow(it.view)) {
//                changAttrView(context, it)
//            }
//        }
//    }
//
//    fun changAttrView(context: Context, attrView: AttrView) {
//        //将每一个换肤控件的属性进行应用
//        attrView.attrs.forEach {
//
//        }
//    }
//
//    fun dynamicAddSkin(v: View): AttrView {
//        val attrView = AttrView(v)
//        attrViews.add(attrView)
//        return attrView
//    }
//}
