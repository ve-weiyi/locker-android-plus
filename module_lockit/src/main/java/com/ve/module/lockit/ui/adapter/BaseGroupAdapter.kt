package com.ve.module.lockit.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.ve.lib.common.base.adapter.VBViewHolder
import java.lang.reflect.ParameterizedType


/**
 * @Author  weiyi
 * @Date 2022/5/7
 * @Description  current project lockit-android
 */
abstract class BaseGroupAdapter<GD : Any, CD : Any>() :
    BaseExpandableListAdapter() {

    private lateinit var mGroups: MutableList<GD>
    private lateinit var mChildren: MutableList<MutableList<CD>>
//    lateinit var groupViewHolder: GroupViewHolder
//    lateinit var childViewHolder: ChildViewHolder

    //返回第一级List长度
    override fun getGroupCount(): Int {
        return mGroups.size
    }

    //返回指定groupPosition的第二级List长度
    override fun getChildrenCount(groupPosition: Int): Int {
        return mChildren.size
    }

    //返回一级List里的内容
    override fun getGroup(groupPosition: Int): Any {
        return mGroups[groupPosition]
    }

    //返回二级List的内容
    override fun getChild(groupPosition: Int, childPosition: Int): Any {
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


    abstract fun getGroupResId(): Int
    abstract fun getChildrenResId(): Int

    //onBindViewHolder
    abstract fun convertGroupView(
        groupViewHolder: GroupViewHolder,
        groupPosition: Int
    )

    abstract fun convertChildrenView(
        childViewHolder: ChildViewHolder,
        groupPosition: Int,
        childPosition: Int,
        isLastChild: Boolean,
    )

    open fun onCreateGroupViewHolder(convertView: View): GroupViewHolder{
        return GroupViewHolder(convertView)
    }

    open fun onCreateChildrenViewHolder(convertView: View): ChildViewHolder{
        return ChildViewHolder(convertView)
    }

    // 返回一级父View
    override fun getGroupView(
        groupPosition: Int,
        isExpanded: Boolean,
        convertView: View?,
        parent: ViewGroup?
    ): View {
        var mConvertView = convertView
        val groupViewHolder: GroupViewHolder
        if (convertView == null) {
            mConvertView =
                LayoutInflater.from(parent?.context).inflate(getGroupResId(), parent, false)

            groupViewHolder = onCreateGroupViewHolder(mConvertView)
            mConvertView?.tag = groupViewHolder
        } else {
            groupViewHolder = mConvertView!!.tag as GroupViewHolder
        }
        convertGroupView(groupViewHolder, groupPosition)
        return mConvertView!!
    }

    override fun getChildView(
        groupPosition: Int,
        childPosition: Int,
        isLastChild: Boolean,
        convertView: View?,
        parent: ViewGroup?
    ): View {
        var mConvertView = convertView
        val childViewHolder: ChildViewHolder

        if (convertView == null) {
            mConvertView =
                LayoutInflater.from(parent?.context).inflate(getChildrenResId(), parent, false)

            childViewHolder = onCreateChildrenViewHolder(mConvertView)
            mConvertView?.tag = childViewHolder
        } else {
            childViewHolder = mConvertView!!.tag as ChildViewHolder
        }
        convertChildrenView(childViewHolder, groupPosition, childPosition, isLastChild)
        return mConvertView!!
    }


    class GroupViewHolder(var view: View) :BaseViewHolder(view)

    class ChildViewHolder(var view: View) :BaseViewHolder(view)

}