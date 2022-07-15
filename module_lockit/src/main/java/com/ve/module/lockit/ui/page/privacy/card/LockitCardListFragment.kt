package com.ve.module.lockit.ui.page.privacy.card

import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import com.chad.library.adapter.base.BaseQuickAdapter
import com.ve.lib.common.base.view.list.BaseVmListFragment
import com.ve.lib.common.ext.setOnclickNoRepeatListener
import com.ve.lib.common.vutils.DialogUtil
import com.ve.lib.common.vutils.LogUtil
import com.ve.module.lockit.R
import com.ve.module.lockit.common.event.RefreshDataEvent
import com.ve.module.lockit.databinding.LockitFragmentListPrivacyBinding
import com.ve.module.lockit.respository.database.entity.PrivacyFolder
import com.ve.module.lockit.respository.database.entity.PrivacyCard
import com.ve.module.lockit.ui.adapter.PrivacyInfoCardAdapter
import com.ve.module.lockit.ui.page.container.LockitContainerActivity
import com.ve.module.lockit.ui.viewmodel.LockitPrivacyCardViewModel
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.litepal.LitePal

/**
 * @Description hello word!
 * @Author  weiyi
 * @Date 2022/4/8
 */
class LockitCardListFragment :
    BaseVmListFragment<LockitFragmentListPrivacyBinding, LockitPrivacyCardViewModel, PrivacyCard>() {

    override fun attachViewBinding(): LockitFragmentListPrivacyBinding {
        return LockitFragmentListPrivacyBinding.inflate(layoutInflater)
    }


    override fun attachAdapter(): BaseQuickAdapter<PrivacyCard, *> {
        return PrivacyInfoCardAdapter()
    }

    val mAdapter by lazy { mListAdapter as PrivacyInfoCardAdapter }

    lateinit var mFolderList: MutableList<PrivacyFolder>

    override fun attachViewModelClass(): Class<LockitPrivacyCardViewModel> {
        return LockitPrivacyCardViewModel::class.java
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
        mViewModel.getPrivacyCardList()
    }

    override fun initListener() {
        super.initListener()
        mBinding.floatingActionBtnAdd.setOnclickNoRepeatListener {
            LockitContainerActivity.start(
                mContext,
                LockitCardEditFragment::class.java,
                "添加密码"
            )
        }
        mBinding.tvSearchText.setOnClickListener {
            LockitContainerActivity.start(
                mContext, LockitCardSearchFragment::class.java,null,null)
        }
    }

    override fun initObserver() {
        super.initObserver()
        mViewModel.getPrivacyCardListResult.observe(this) {
            showPrivacyList(it)
        }
        mViewModel.deletePrivacyCardListResult.observe(this) {
            showMsg("$it 条数据删除成功！")
            showMenuMore(false)
            mViewModel.getPrivacyCardList()
        }
        mViewModel.movePrivacyCardListResult.observe(this) {
            showMsg("$it 条数据移动成功！")
            showMenuMore(false)
            mViewModel.getPrivacyCardList()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
        if (!isShowMore) {
            inflater.inflate(R.menu.lockit_menu_swap, menu)
        } else {
            inflater.inflate(R.menu.lockit_menu_edit, menu)
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

    private fun deleteList(list: MutableList<PrivacyCard>) {
        if (listValid(list)) {
            DialogUtil.getConfirmDialog(
                mContext,
                "您确定要删除 ${list.size} 条数据吗？这项操作无法恢复。",
                onOKClickListener = { _, _ ->
                    mViewModel.deletePrivacyCardList(list)
                },
                onCancelClickListener = { _, _ ->
                    showMsg("取消")
                },
            ).show()
        }
    }

    private fun moveList(list: MutableList<PrivacyCard>) {
        if (listValid(list)) {
            val folderNameList = mFolderList.map { it.folderName }
            DialogUtil.getSelectDialog(
                mContext,
                folderNameList.toTypedArray(),
                onClickListener = { dialog, which ->
                    mViewModel.movePrivacyCardList(
                        mAdapter.getSelectData(), folder = mFolderList[which]
                    )
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

    private fun listValid(list: MutableList<PrivacyCard>): Boolean {
        LogUtil.msg(list.toString())
        if (list.size == 0) {
            showMsg("至少选择一条数据哦！")
            showMenuMore(false)
            return false
        }
        return true
    }

    private fun showPrivacyList(privacyList: MutableList<PrivacyCard>) {
        showAtAdapter(true, sortAtInstitution(privacyList))
    }

    /**
     * Refresh Data Event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onRefreshDataEvent(event: RefreshDataEvent) {
        if (PrivacyCard::class.java.name == event.dataClassName) {
            LogUtil.d("$mViewName receiver event " + event.dataClassName)
            hasLoadData = false
        }
    }

    /**
     * 按名称分组
     */
    private fun sortAtInstitution(privacyList: MutableList<PrivacyCard>): MutableList<PrivacyCard> {
        //按url排序
        privacyList.sortWith(compareBy { it.name })
        //新的列表
        val list = mutableListOf<PrivacyCard>()
        var bHeader = true
        privacyList.forEach { privacyCard ->
            bHeader = true
            //检查已有的header，如果已经有，直接添加
            for (i in list.indices) {
                if (privacyCard.name == list[i].headerName) {
                    bHeader = false
                    break
                }
            }
            //如果是新的header，则添加。header 和 context 只能显示一个
            if (bHeader) {
                list.add(PrivacyCard(headerName = privacyCard.name))
            }
            //显示context
            list.add(privacyCard)
        }
        return list
    }
}