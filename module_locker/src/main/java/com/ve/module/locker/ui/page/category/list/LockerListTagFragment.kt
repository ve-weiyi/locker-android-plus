package com.ve.module.locker.ui.page.category.list

import android.os.Bundle
import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.ve.lib.common.base.view.list.BaseVmListFragment
import com.ve.lib.common.ext.setOnclickNoRepeatListener
import com.ve.lib.common.vutils.LogUtil
import com.ve.module.locker.R
import com.ve.module.locker.common.event.RefreshDataEvent
import com.ve.module.locker.databinding.LockerFragmentListCategoryBinding
import com.ve.module.locker.model.db.entity.PrivacyTag
import com.ve.module.locker.ui.adapter.ListTagAdapter
import com.ve.module.locker.ui.page.category.details.LockerTagDetailsFragment
import com.ve.module.locker.ui.page.container.LockerContainerActivity
import com.ve.module.locker.ui.page.privacy.details.EditType
import com.ve.module.locker.ui.viewmodel.LockerCategoryViewModel
import com.ve.module.locker.ui.view.TagSwipeItemLayout
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * @Description hello word!
 * @Author  weiyi
 * @Date 2022/4/8
 */
class LockerListTagFragment :
    BaseVmListFragment<LockerFragmentListCategoryBinding , LockerCategoryViewModel, PrivacyTag>() {

    override fun attachViewBinding(): LockerFragmentListCategoryBinding {
        return LockerFragmentListCategoryBinding.inflate(layoutInflater)
    }

    override fun attachViewModelClass(): Class<LockerCategoryViewModel> {
        return LockerCategoryViewModel::class.java
    }

    override fun attachAdapter(): BaseQuickAdapter<PrivacyTag, *> {
        return ListTagAdapter()
    }

    override fun initListView() {
        mLayoutStatusView = mBinding.multipleStatusView
        mRecyclerView = mBinding.recyclerView
        mSwipeRefreshLayout = mBinding.swipeRefreshLayout
    }

    override fun initWebData() {
        mViewModel.getTagList()
    }

    override fun getMoreData() {
        mCurrentPage=0
    }

    override fun initObserver() {
        mViewModel.tagList.observe(this) {
            showAtAdapter(it)
        }
        mViewModel.tagDeleteMsg.observe(this) {
            showMsg(it)
            getRefreshData()
        }
    }

    override fun initListener() {
        super.initListener()
        mRecyclerView!!.addOnItemTouchListener(TagSwipeItemLayout.OnSwipeItemTouchListener(activity))
        mBinding.floatingActionBtn.setOnclickNoRepeatListener  {
            addPrivacyTag()
        }
    }

    override fun onItemChildClickEvent(datas: MutableList<PrivacyTag>, view: View, position: Int) {
        itemChildClick(datas[position], view, position)
    }

    /**
     * Item Child Click
     * @param item TodoDataBean
     * @param view View
     * @param position Int
     */
    private fun itemChildClick(item: PrivacyTag, view: View, position: Int) {
        when (view.id) {
            R.id.item_layout_content -> {
                val bundle = Bundle()
                bundle.putInt(LockerTagDetailsFragment.FRAGMENT_TYPE_KEY, EditType.SEE_TAG_TYPE)
                bundle.putSerializable(LockerTagDetailsFragment.FRAGMENT_DATA_KEY, item)
                LockerContainerActivity.start(mContext, LockerTagDetailsFragment::class.java.name, "查看标签 "+item.tagName,bundle )

                showMsg("查看")
            }
            R.id.item_btn_edit -> {
                val bundle = Bundle()
                bundle.putInt(LockerTagDetailsFragment.FRAGMENT_TYPE_KEY, EditType.EDIT_TAG_TYPE)
                bundle.putSerializable(LockerTagDetailsFragment.FRAGMENT_DATA_KEY, item)
                LockerContainerActivity.start(mContext, LockerTagDetailsFragment::class.java.name, "编辑标签 "+item.tagName, bundle)
                showMsg("编辑")
            }
            R.id.item_btn_delete -> {
                mViewModel.tagDelete(item.id.toInt())
                showMsg("删除")
            }
        }
    }

    private fun addPrivacyTag() {
        val bundle = Bundle()
        bundle.putInt(LockerTagDetailsFragment.FRAGMENT_TYPE_KEY, EditType.ADD_TAG_TYPE)
        LockerContainerActivity.start(mContext, LockerTagDetailsFragment::class.java.name, "添加标签",bundle)
        showMsg("添加")
    }

    /**
     * Refresh Data Event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onRefreshDataEvent(event: RefreshDataEvent) {
        if(this::class.java.name==event.dataClassName){
            LogUtil.d("$mViewName receiver event "+event.dataClassName)
            hasLoadData=false
        }
    }

}