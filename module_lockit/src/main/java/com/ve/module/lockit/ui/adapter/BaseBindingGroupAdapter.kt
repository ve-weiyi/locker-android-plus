package com.ve.module.lockit.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import androidx.viewbinding.ViewBinding
import com.ve.lib.common.base.adapter.ViewBindingHolder
import com.ve.lib.common.exception.BizException
import com.ve.lib.common.utils.system.LogUtil
import org.jetbrains.anko.layoutInflater
import java.lang.reflect.ParameterizedType


/**
 * @Author  weiyi
 * @Date 2022/5/7
 * @Description  current project lockit-android
 */
abstract class BaseBindingGroupAdapter<GD : Any, CD : Any, GVB : ViewBinding, CVB : ViewBinding>() :
    BaseExpandableListAdapter() {

    var mGroups: MutableList<GD> = mutableListOf()
    var mChildren: MutableList<MutableList<CD>> = mutableListOf()
//    lateinit var groupViewHolder: GroupViewHolder
//    lateinit var childViewHolder: ChildViewHolder

    fun setNewInstance(
        isNewData: Boolean,
        groups: MutableList<GD>,
        children: MutableList<MutableList<CD>>
    ) {
        if (groups.size < children.size) {
            throw BizException("分组错误")
        }

        if (isNewData) {
            mGroups = groups
            mChildren = children
            notifyDataSetChanged()
        } else {
            mGroups.addAll(groups)
            mChildren.addAll(children)
            notifyDataSetChanged()
        }
    }

    //返回第一级List长度
    override fun getGroupCount(): Int {
        return mGroups.size
    }

    //返回指定groupPosition的第二级List长度
    override fun getChildrenCount(groupPosition: Int): Int {
        return mChildren[groupPosition].size
    }

    //返回一级List里的内容
    override fun getGroup(groupPosition: Int): GD {
        return mGroups[groupPosition]
    }

    //返回二级List的内容
    override fun getChild(groupPosition: Int, childPosition: Int): CD {
        LogUtil.msg("----->$groupPosition------>$childPosition")
        return mChildren[groupPosition][childPosition];
    }

    //返回一级View的id 保证id唯一
    override fun getGroupId(groupPosition: Int): Long {
        return groupPosition.toLong()
    }

    //返回二级View的id 保证id唯一
    override fun getChildId(groupPosition: Int, childPosition: Int): Long {
        return (groupPosition shl 8 + childPosition).toLong()
    }

    //基础数据进行更改时子ID和组ID是否稳定
    override fun hasStableIds(): Boolean {
        return true
    }

    //指定位置的子项是否可选
    override fun isChildSelectable(groupPosition: Int, childPosition: Int): Boolean {
        return true
    }


    //onBindViewHolder
    abstract fun convertGroupView(
        groupViewHolder: GroupViewHolder<GVB>,
        groupPosition: Int
    )

    abstract fun convertChildrenView(
        childViewHolder: ChildViewHolder<CVB>,
        groupPosition: Int,
        childPosition: Int,
        isLastChild: Boolean,
    )

    private val gvbClass: Class<GVB> by lazy {
        (javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[2] as Class<GVB>
    }

    private val cvbClass: Class<CVB> by lazy {
        (javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[3] as Class<CVB>
    }

    open fun onCreateGroupViewHolder(context: Context): GroupViewHolder<GVB> {
        //这里为了使用简洁性，使用反射来实例ViewBinding  actualTypeArguments[1]是泛型第二个参数VB
        val inflate = gvbClass.getDeclaredMethod("inflate", LayoutInflater::class.java)
        val mBinding = inflate.invoke(null, context.layoutInflater) as GVB
        return GroupViewHolder(mBinding, mBinding.root)
    }

    open fun onCreateChildrenViewHolder(context: Context): ChildViewHolder<CVB> {
        //这里为了使用简洁性，使用反射来实例ViewBinding  actualTypeArguments[1]是泛型第二个参数VB
        val inflate = cvbClass.getDeclaredMethod("inflate", LayoutInflater::class.java)
        val mBinding = inflate.invoke(null, context.layoutInflater) as CVB
        return ChildViewHolder(mBinding, mBinding.root)
    }

//    open fun onCreateChildrenViewHolder(convertView: View): ChildViewHolder<CVB> {
//        //这里为了使用简洁性，使用反射来实例ViewBinding  actualTypeArguments[1]是泛型第二个参数VB
//        val inflate = cvbClass.getDeclaredMethod(
//            "inflate",
//            LayoutInflater::class.java,
//            ViewGroup::class.java,
//            Boolean::class.java
//        )
//        val mBinding = inflate.invoke(
//            null,
//            LayoutInflater.from(convertView.context),
//            convertView,
//            false
//        ) as CVB
//        return ChildViewHolder(mBinding, mBinding.root)
//    }

    // 返回一级父View
    override fun getGroupView(
        groupPosition: Int,
        isExpanded: Boolean,
        convertView: View?,
        parent: ViewGroup?
    ): View {
        var mConvertView = convertView
        val groupViewHolder: GroupViewHolder<GVB>
        if (convertView == null) {
            groupViewHolder = onCreateGroupViewHolder(parent!!.context)
            mConvertView = groupViewHolder.view
            mConvertView.tag = groupViewHolder
        } else {
            groupViewHolder = mConvertView!!.tag as GroupViewHolder<GVB>
        }

        convertGroupView(groupViewHolder, groupPosition)
        return mConvertView
    }

    override fun getChildView(
        groupPosition: Int,
        childPosition: Int,
        isLastChild: Boolean,
        convertView: View?,
        parent: ViewGroup?
    ): View {
        var mConvertView = convertView
        val childViewHolder: ChildViewHolder<CVB>

        if (convertView == null) {
            //这里为了使用简洁性，使用反射来实例ViewBinding  actualTypeArguments[1]是泛型第二个参数VB
            childViewHolder = onCreateChildrenViewHolder(parent!!.context)
            mConvertView = childViewHolder.view
            mConvertView.tag = childViewHolder
        } else {
            childViewHolder = mConvertView!!.tag as ChildViewHolder<CVB>
        }
        convertChildrenView(childViewHolder, groupPosition, childPosition, isLastChild)
        return mConvertView
    }


    class GroupViewHolder<GVB : ViewBinding>(var mBinding: GVB, var view: View) :
        ViewBindingHolder<GVB>(mBinding, view)

    class ChildViewHolder<CVB : ViewBinding>(var mBinding: CVB, var view: View) :
        ViewBindingHolder<CVB>(mBinding, view)

}