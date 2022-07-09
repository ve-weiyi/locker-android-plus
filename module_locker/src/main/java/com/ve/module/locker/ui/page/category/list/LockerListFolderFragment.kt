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
import com.ve.module.locker.model.db.entity.PrivacyFolder
import com.ve.module.locker.ui.adapter.ListFolderAdapter
import com.ve.module.locker.ui.page.category.details.LockerFolderDetailsFragment
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
class LockerListFolderFragment :
    BaseVmListFragment<LockerFragmentListCategoryBinding, LockerCategoryViewModel, PrivacyFolder>() {

    override fun attachViewBinding(): LockerFragmentListCategoryBinding{
        return LockerFragmentListCategoryBinding.inflate(layoutInflater)
    }

    override fun attachViewModelClass(): Class<LockerCategoryViewModel> {
        return LockerCategoryViewModel::class.java
    }

    override fun attachAdapter(): BaseQuickAdapter<PrivacyFolder, *> {
        return ListFolderAdapter()
    }

    override fun initListView() {
        mLayoutStatusView = mBinding.multipleStatusView
        mRecyclerView = mBinding.recyclerView
        mSwipeRefreshLayout = mBinding.swipeRefreshLayout
    }


    override fun initWebData() {
        mViewModel.folderList()
    }

    override fun initObserver() {
        mViewModel.folderList.observe(this) {
            showAtAdapter(it)
        }
        mViewModel.folderDeleteMsg.observe(this) {
            showMsg(it)
            getRefreshData()
        }
    }

    override fun initListener() {
        super.initListener()
        mRecyclerView!!.addOnItemTouchListener(TagSwipeItemLayout.OnSwipeItemTouchListener(activity))
        mBinding.floatingActionBtn.setOnclickNoRepeatListener  {
            addPrivacyFolder()
        }
    }

    override fun onItemChildClickEvent(datas: MutableList<PrivacyFolder>, view: View, position: Int) {
        itemChildClick(datas[position], view, position)
    }

    /**
     * Item Child Click
     * @param item TodoDataBean
     * @param view View
     * @param position Int
     */
    private fun itemChildClick(item: PrivacyFolder, view: View, position: Int) {
        when (view.id) {
            R.id.item_layout_content -> {
                val bundle = Bundle()
                bundle.putInt(LockerFolderDetailsFragment.FRAGMENT_TYPE_KEY, EditType.SEE_TAG_TYPE)
                bundle.putSerializable(LockerFolderDetailsFragment.FRAGMENT_DATA_KEY, item)
                LockerContainerActivity.start(mContext, LockerFolderDetailsFragment::class.java.name, "查看文件夹 "+item.folderName,bundle )

                showMsg("查看")
            }
            R.id.item_btn_edit -> {
                val bundle = Bundle()
                bundle.putInt(LockerFolderDetailsFragment.FRAGMENT_TYPE_KEY, EditType.EDIT_TAG_TYPE)
                bundle.putSerializable(LockerFolderDetailsFragment.FRAGMENT_DATA_KEY, item)
                LockerContainerActivity.start(mContext, LockerFolderDetailsFragment::class.java.name, "编辑文件夹 "+item.folderName, bundle)
                showMsg("编辑")
            }
            R.id.item_btn_delete -> {
                mViewModel.folderDelete(item.id.toInt())
                showMsg("删除")
            }
        }
    }

    private fun addPrivacyFolder() {
        val bundle = Bundle()
        bundle.putInt(LockerFolderDetailsFragment.FRAGMENT_TYPE_KEY, EditType.ADD_TAG_TYPE)
        LockerContainerActivity.start(mContext, LockerFolderDetailsFragment::class.java.name, "添加文件夹",bundle)
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