package com.ve.module.locker.ui.page.privacy.list

import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import com.chad.library.adapter.base.BaseQuickAdapter
import com.ve.lib.common.base.view.list.BaseVmListFragment
import com.ve.lib.common.ext.setOnclickNoRepeatListener
import com.ve.lib.common.utils.DialogUtil
import com.ve.lib.common.vutils.LogUtil
import com.ve.module.locker.R
import com.ve.module.locker.common.event.RefreshDataEvent
import com.ve.module.locker.databinding.LockerFragmentListPrivacyBinding
import com.ve.module.locker.model.db.entity.PrivacyFolder
import com.ve.module.locker.model.db.entity.PrivacyPassInfo
import com.ve.module.locker.ui.adapter.PrivacyInfoPassAdapter
import com.ve.module.locker.ui.page.container.LockerContainerActivity
import com.ve.module.locker.ui.page.privacy.details.LockerPassDetailsEditFragment
import com.ve.module.locker.ui.page.search.LockerListPassSearchFragment
import com.ve.module.locker.ui.viewmodel.LockerPrivacyPassViewModel
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.litepal.LitePal

/**
 * @Description hello word!
 * @Author  weiyi
 * @Date 2022/4/8
 */
class LockerListPassFragment :
    BaseVmListFragment<LockerFragmentListPrivacyBinding, LockerPrivacyPassViewModel, PrivacyPassInfo>(){

    override fun attachViewBinding(): LockerFragmentListPrivacyBinding {
        return LockerFragmentListPrivacyBinding.inflate(layoutInflater)
    }


    override fun attachAdapter(): BaseQuickAdapter<PrivacyPassInfo, *> {
        return PrivacyInfoPassAdapter()
    }

    val mAdapter by lazy { mListAdapter as PrivacyInfoPassAdapter }
    lateinit var mFolderList: MutableList<PrivacyFolder>

    override fun attachViewModelClass(): Class<LockerPrivacyPassViewModel> {
        return LockerPrivacyPassViewModel::class.java
    }


    override fun initListView() {
        mLayoutStatusView = mBinding.fragmentRefreshLayout.multipleStatusView
        mRecyclerView = mBinding.fragmentRefreshLayout.recyclerView
        mSwipeRefreshLayout = mBinding.fragmentRefreshLayout.swipeRefreshLayout
        setHasOptionsMenu(true)
        mFolderList = LitePal.findAll(PrivacyFolder::class.java)
    }

    override fun initWebData() {
        super.initWebData()
        mViewModel.getPrivacyPassList()
    }

    override fun initListener() {
        super.initListener()
        mBinding.floatingActionBtn1.setOnclickNoRepeatListener {

            LockerContainerActivity.start(
                mContext,
                LockerPassDetailsEditFragment::class.java.name,
                "添加账号"
            )
        }
        mBinding.tvSearchText.setOnClickListener{
            LockerContainerActivity.start(mContext,
                LockerListPassSearchFragment::class.java,null,null)
        }
    }
    override fun initObserver() {
        super.initObserver()
        mViewModel.privacyPassInfoList.observe(this) {
            LogUtil.msg("$mViewName ------ $it")
            showPrivacyList(it)
        }
        mViewModel.deletePrivacyPassListResult.observe(this) {
            showMsg("$it 条数据删除成功！")
            showMenuMore(false)
            mViewModel.getPrivacyPassList()
        }
        mViewModel.movePrivacyPassResult.observe(this) {
            showMsg("$it 条数据移动成功！")
            showMenuMore(false)
            mViewModel.getPrivacyPassList()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
        if (!isShowMore) {
            inflater.inflate(R.menu.locker_menu_swap, menu)
        } else {
            inflater.inflate(R.menu.locker_menu_edit, menu)
        }
        super.onCreateOptionsMenu(menu, inflater)
    }

    var isShowMore = false
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_swap -> {
                showMenuMore(true)
                return true
            }
            R.id.action_cancel -> {
                showMenuMore(false)
                return true
            }
            R.id.action_move -> {
                val list = mAdapter.getSelectData()
                moveList(list)
            }
            R.id.action_all -> {
                mAdapter.changeAllState()
                mAdapter.notifyDataSetChanged()
                LogUtil.msg(mAdapter.getSelectData().toString())
            }
            R.id.action_delete -> {
                val list = mAdapter.getSelectData()
                deleteList(list)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun deleteList(list: MutableList<PrivacyPassInfo>) {
        if (listValid(list)) {
            DialogUtil.getConfirmDialog(
                mContext,
                "您确定要删除 ${list.size} 条数据吗？这项操作无法恢复。",
                onOKClickListener = { _, _ ->
                    mViewModel.deletePrivacyPassList(list)
                },
                onCancelClickListener = { _, _ ->

                },
            ).show()
        }
    }

    private fun moveList(list: MutableList<PrivacyPassInfo>) {
        if (listValid(list)) {
            val folderNameList = mFolderList.map { it.folderName }
            DialogUtil.getSelectDialog(
                mContext,
                folderNameList.toTypedArray(),
                onClickListener = { dialog, which ->
                    mViewModel.movePrivacyPass(
                        mAdapter.getSelectData(), folder = mFolderList[which])
                }
            ).show()
        }
    }

    private fun showMenuMore(isShow: Boolean) {
        isShowMore = isShow
        mAdapter.isCheckMode = isShow
        mAdapter.notifyDataSetChanged()
        activity?.invalidateOptionsMenu()
    }

    private fun listValid(list: MutableList<PrivacyPassInfo>): Boolean {
        LogUtil.msg(list.toString())
        if (list.size == 0) {
            showMsg("至少选择一条数据哦！")
            showMenuMore(false)
            return false
        }
        return true
    }



    private fun showPrivacyList(privacyList: MutableList<PrivacyPassInfo>) {
        val list = mutableListOf<PrivacyPassInfo>()
        var bHeader = true

        privacyList.forEach { privacyPassInfo ->
            bHeader = true
            //检查已有的header
            for (i in list.indices) {
                if (privacyPassInfo.createTime.startsWith(list[i].headerName)) {
                    bHeader = false
                    break
                }
            }
            //如果是新的header，则添加。header 和 context 只能显示一个
            if (bHeader) {
                list.add(PrivacyPassInfo(headerName = privacyPassInfo.createTime.substring(0, 10)))
            }
            //显示context
            list.add(privacyPassInfo)
        }
        showAtAdapter(true, list)
    }

    /**
     * Refresh Data Event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onRefreshDataEvent(event: RefreshDataEvent) {
        if (PrivacyPassInfo::class.java.name == event.dataClassName) {
            LogUtil.d("$mViewName receiver event " + event.dataClassName)
            hasLoadData = false
        }
    }
}