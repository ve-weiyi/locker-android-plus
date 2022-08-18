package com.ve.module.lockit.ui.adapter

import com.ve.lib.common.utils.file.ImageLoader
import com.ve.module.lockit.databinding.LockitItemCategoryChildrenBinding
import com.ve.module.lockit.databinding.LockitItemCategoryGroupBinding
import com.ve.module.lockit.respository.database.vo.PrivacySimpleInfo

/**
 * @Author  weiyi
 * @Date 2022/5/7
 * @Description  current project lockit-android
 */
class CategoryGroupAdapter:BaseBindingGroupAdapter
<String,PrivacySimpleInfo, LockitItemCategoryGroupBinding, LockitItemCategoryChildrenBinding>() {

    override fun convertGroupView(
        groupViewHolder: GroupViewHolder<LockitItemCategoryGroupBinding>,
        groupPosition: Int
    ) {
        val item=getGroup(groupPosition)
        groupViewHolder.apply {
            mBinding.tvHeader.text=item
        }
    }

    override fun convertChildrenView(
        childViewHolder: ChildViewHolder<LockitItemCategoryChildrenBinding>,
        groupPosition: Int,
        childPosition: Int,
        isLastChild: Boolean
    ) {
        val item=getChild(groupPosition,childPosition)
        childViewHolder.apply {
            mBinding.tvPrivacyName.text=item.privacyName
            mBinding.tvPrivacyInfoDesc.text=item.privacyDesc
            ImageLoader.load(childViewHolder.view.context,item.privacyCover,mBinding.ivAppIcon)
            mBinding.tvPrivacyInfoUpdateTime.text=item.updateTime
        }
    }


}