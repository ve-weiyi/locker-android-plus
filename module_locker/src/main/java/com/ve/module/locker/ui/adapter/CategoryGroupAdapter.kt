package com.ve.module.locker.ui.adapter

import com.ve.lib.common.utils.ImageLoader
import com.ve.module.locker.databinding.LockerItemCategoryChildrenBinding
import com.ve.module.locker.databinding.LockerItemCategoryGroupBinding
import com.ve.module.locker.model.db.vo.PrivacySimpleInfo

/**
 * @Author  weiyi
 * @Date 2022/5/7
 * @Description  current project locker-android
 */
class CategoryGroupAdapter:BaseBindingGroupAdapter
<String,PrivacySimpleInfo, LockerItemCategoryGroupBinding, LockerItemCategoryChildrenBinding>() {

    override fun convertGroupView(
        groupViewHolder: GroupViewHolder<LockerItemCategoryGroupBinding>,
        groupPosition: Int
    ) {
        val item=getGroup(groupPosition)
        groupViewHolder.apply {
            mBinding.tvHeader.text=item
        }
    }

    override fun convertChildrenView(
        childViewHolder: ChildViewHolder<LockerItemCategoryChildrenBinding>,
        groupPosition: Int,
        childPosition: Int,
        isLastChild: Boolean
    ) {
        val item=getChild(groupPosition,childPosition)
        childViewHolder.apply {
            mBinding.tvPrivacyInfoName.text=item.privacyName
            mBinding.tvPrivacyInfoDesc.text=item.privacyDesc
            ImageLoader.load(childViewHolder.view.context,item.privacyCover,mBinding.ivAppIcon)
            mBinding.tvPrivacyInfoCreateTime.text=item.createTime
            mBinding.tvPrivacyInfoUpdateTime.text=item.updateTime
        }
    }


}