package com.ve.module.lockit.ui.page.category.tag

import android.os.Bundle
import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.ve.lib.common.base.view.list.BaseVmListFragment
import com.ve.lib.common.ext.setOnclickNoRepeatListener
import com.ve.lib.common.utils.log.LogUtil
import com.ve.module.lockit.R
import com.ve.module.lockit.common.event.RefreshDataEvent
import com.ve.module.lockit.databinding.LockitFragmentListCategoryBinding
import com.ve.module.lockit.respository.database.entity.PrivacyTag
import com.ve.module.lockit.ui.adapter.TagListAdapter
import com.ve.module.lockit.ui.page.container.LockitContainerActivity
import com.ve.module.lockit.common.enums.EditTypeEnum
import com.ve.module.lockit.ui.viewmodel.LockitClassifyViewModel
import com.ve.lib.common.widget.layout.SwipeItemLayout
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * @Description hello word!
 * @Author  weiyi
 * @Date 2022/4/8
 */
class LockitTagListFragment :
    BaseVmListFragment<LockitFragmentListCategoryBinding , LockitClassifyViewModel, PrivacyTag>() {

    override fun attachViewBinding(): LockitFragmentListCategoryBinding {
        return LockitFragmentListCategoryBinding.inflate(layoutInflater)
    }

    override fun attachViewModelClass(): Class<LockitClassifyViewModel> {
        return LockitClassifyViewModel::class.java
    }

    override fun attachAdapter(): BaseQuickAdapter<PrivacyTag, *> {
        return TagListAdapter()
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
        mRecyclerView!!.addOnItemTouchListener(SwipeItemLayout.OnSwipeItemTouchListener(activity))
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
                bundle.putInt(LockitTagDetailsFragment.FRAGMENT_TYPE_KEY, EditTypeEnum.SEE_TAG_TYPE)
                bundle.putSerializable(LockitTagDetailsFragment.FRAGMENT_DATA_KEY, item)
                LockitContainerActivity.start(mContext, LockitTagDetailsFragment::class.java.name, "查看标签 "+item.tagName,bundle )

                showMsg("查看")
            }
            R.id.item_btn_edit -> {
                val bundle = Bundle()
                bundle.putInt(LockitTagDetailsFragment.FRAGMENT_TYPE_KEY, EditTypeEnum.EDIT_TAG_TYPE)
                bundle.putSerializable(LockitTagDetailsFragment.FRAGMENT_DATA_KEY, item)
                LockitContainerActivity.start(mContext, LockitTagDetailsFragment::class.java.name, "编辑标签 "+item.tagName, bundle)
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
        bundle.putInt(LockitTagDetailsFragment.FRAGMENT_TYPE_KEY, EditTypeEnum.ADD_TAG_TYPE)
        LockitContainerActivity.start(mContext, LockitTagDetailsFragment::class.java.name, "添加标签",bundle)
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