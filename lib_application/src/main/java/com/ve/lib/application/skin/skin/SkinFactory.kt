package com.ve.lib.application.skin.skin

import android.content.Context
import android.content.res.Resources
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
import java.lang.reflect.Field

/**
 * 使用setTheme方案都需要重新创建Activity
 */
class SkinFactory(activity: AppCompatActivity) : LayoutInflater.Factory2 {

    var key: String
    private var appCompatDelegate: AppCompatDelegate
    private var layoutInflater: LayoutInflater
    private var mResources: Resources

    /**
     * activity中的view集合
     */
    private val attrViews: MutableList<AttrView> = mutableListOf()

    //兼容低版本创建 view 的逻辑（低版本是没有完整包名）
    private val CLASS_PREFIX_LIST = arrayOf("android.widget.", "android.webkit.", "android.app.")

    init {
        key = activity.javaClass.name
        appCompatDelegate = activity.delegate
        layoutInflater = activity.layoutInflater
        mResources = activity.resources
        LayoutInflaterCompat.setFactory2(activity.layoutInflater, this)
    }

    override fun onCreateView(name: String, context: Context, attrs: AttributeSet): View? {
        return null
    }

    override fun onCreateView(
        parent: View?,
        name: String,
        context: Context,
        attrs: AttributeSet
    ): View? {
        //获取theme中的值
        val obtainStyledAttributes = context.obtainStyledAttributes(attrs, R.styleable.SkinSupport)
        val isEnable = obtainStyledAttributes.getBoolean(R.styleable.SkinSupport_enableSkin, false)
        obtainStyledAttributes.recycle()
        var createView: View? = null
        //如果控件支持换肤
        if (isEnable) {
            //调用系统方法创建控件
            createView = appCompatDelegate.createView(parent, name, context, attrs)
            val attrView = AttrView(createView)
            for (i in 0 until attrs.attributeCount) {
                val attributeName = attrs.getAttributeName(i)
                //如果是支持换肤的属性
                if (isSupportAttr(attributeName)) {
                    val attributeValue = attrs.getAttributeValue(i)
                    //# 直接写死的颜色 不处理
                    //?2130903258 ?colorPrimary 这样的 解析主题，找到id，再去找资源名称和类型
                    //@2131231208 @color/red 直接就是id，根据id找到资源名称和类型
                    if (attributeValue.startsWith("?")) {
                        val attrId = attributeValue.substring(1).toInt()
                        val resIdFromTheme = getResIdFromTheme(context, attrId)
                        if (resIdFromTheme > 0) {
                            attrView.attrs.add(AttrView.AttrItem(attributeName, resIdFromTheme))
                        }
                    } else if (attributeValue.startsWith("@")) {
                        val attrId = attributeValue.substring(1).toInt()
                        attrView.attrs.add(AttrView.AttrItem(attributeName, attrId))
                    }
                }
            }
            attrViews.add(attrView)
        }
        return createView
    }

    /**
     * 解析主题，找到资源id，其实就是方案一里面的方法
     */
    private fun getResIdFromTheme(context: Context, attrId: Int): Int {
        val typedValue = TypedValue()
        val success = context.theme.resolveAttribute(attrId, typedValue, true)
        //typedValue.resourceId 可能为0
        return typedValue.resourceId
    }

    /**
     * 支持的换肤属性，如需要对所有的属性都处理，则直接返回true
     */
    private fun isSupportAttr(attrName: String): Boolean {
        return SkinAttr.contain(attrName)
    }

    fun changeSkin(context: Context) {
        //这个是在Factory2中找到的所有支持换肤的控件
        attrViews.forEach {
            if (ViewCompat.isAttachedToWindow(it.view)) {
                changAttrView(context, it)
            }
        }
    }

    fun changAttrView(context: Context, attrView: AttrView) {
        //将每一个换肤控件的属性进行应用
        attrView.attrs.forEach {
            if (attrView.view is TextView) {
                if (it.attrName == SkinAttr.textColor) {
                    //去皮肤包中寻找对应的资源
                    attrView.view.setTextColor(SkinLoader.instance.getTextColor(context, it.resId))
                } else if (it.attrName == SkinAttr.text) {
                    //去皮肤包中寻找对应的资源
                    attrView.view.text = SkinLoader.instance.getText(context, it.resId)
                }
            }
        }
    }

    fun dynamicAddSkin(v: View): AttrView {
        val attrView = AttrView(v)
        attrViews.add(attrView)
        return attrView
    }

    object SkinAttr {
        var text = "text"
        var textColor = "textColor"
        var background = "background"
        var drawable = "drawable"
        var color = "color"
        var src = "src"

        fun contain(attrName: String): Boolean {
            try {
                //通过getDeclaredFields()方法获取对象类中的所有属性（含私有）
                val fields: Array<Field> = this.javaClass.declaredFields
                for (field in fields) {
                    //设置允许通过反射访问私有变量
                    field.isAccessible = true
                    //获取字段的值
                    val value: String = field.get(this).toString()
                    if (value == attrName) {
                        return true
                    }
                }
            } catch (ex: Exception) {
                //处理异常
                return false
            }
            return false
        }
    }
}