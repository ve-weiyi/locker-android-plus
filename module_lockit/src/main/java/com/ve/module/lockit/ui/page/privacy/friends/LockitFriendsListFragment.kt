package com.ve.module.lockit.ui.page.privacy.friends

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.LinearLayout
import com.chad.library.adapter.base.BaseQuickAdapter
import com.ve.lib.common.base.view.list.BaseVmListFragment
import com.ve.lib.common.ext.setOnclickNoRepeatListener
import com.ve.lib.common.utils.view.DialogUtil
import com.ve.lib.common.utils.system.LogUtil
import com.ve.module.lockit.R
import com.ve.module.lockit.common.event.RefreshDataEvent
import com.ve.module.lockit.databinding.LockitFragmentListPrivacyBinding
import com.ve.module.lockit.respository.database.entity.PrivacyFolder
import com.ve.module.lockit.respository.database.entity.PrivacyFriend
import com.ve.module.lockit.ui.adapter.PrivacyInfoFriendsAdapter
import com.ve.module.lockit.ui.page.container.LockitContainerActivity
import com.ve.lib.view.widget.layout.SwipeItemLayout
import com.ve.module.lockit.ui.viewmodel.LockitPrivacyFriendsViewModel
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.litepal.LitePal

/**
 * @Description hello word!
 * @Author  weiyi
 * @Date 2022/4/8
 */
class LockitFriendsListFragment :
    BaseVmListFragment<LockitFragmentListPrivacyBinding, LockitPrivacyFriendsViewModel, PrivacyFriend>(){

    override fun attachViewBinding(): LockitFragmentListPrivacyBinding {
        return LockitFragmentListPrivacyBinding.inflate(layoutInflater)
    }


    override fun attachAdapter(): BaseQuickAdapter<PrivacyFriend, *> {
        return PrivacyInfoFriendsAdapter()
    }

    val mAdapter by lazy { mListAdapter as PrivacyInfoFriendsAdapter }
    lateinit var mFolderList: MutableList<PrivacyFolder>

    override fun attachViewModelClass(): Class<LockitPrivacyFriendsViewModel> {
        return LockitPrivacyFriendsViewModel::class.java
    }


    override fun initListView() {
        mLayoutStatusView = mBinding.fragmentRefreshLayout.multipleStatusView
        mRecyclerView = mBinding.fragmentRefreshLayout.recyclerView
        mSwipeRefreshLayout = mBinding.fragmentRefreshLayout.swipeRefreshLayout
        setHasOptionsMenu(true)
        mFolderList = LitePal.findAll(PrivacyFolder::class.java)
    }

    override fun loadWebData() {
        super.loadWebData()
        mViewModel.getPrivacyFriendsList()
    }

    override fun initListener() {
        super.initListener()
        mBinding.tvSearchText.addTextChangedListener(textWatcher)

        mRecyclerView!!.addOnItemTouchListener(SwipeItemLayout.OnSwipeItemTouchListener(activity))
        mBinding.floatingActionBtnAdd.setOnclickNoRepeatListener {
            LockitContainerActivity.start(
                mContext,
                LockitFriendsEditFragment::class.java.name,
                "添加好友信息"
            )
        }
    }

    override fun initObserver() {
        super.initObserver()
        mViewModel.privacyFriendsInfoList.observe(this){
            showAtAdapter(true,it)
        }
        mViewModel.deletePrivacyFriendsResult.observe(this){
            showMsg("删除成功")
            mViewModel.getPrivacyFriendsList()
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

    private fun deleteList(list: MutableList<PrivacyFriend>) {
        if (listValid(list)) {
            DialogUtil.getConfirmDialog(
                mContext,
                "您确定要删除 ${list.size} 条数据吗？这项操作无法恢复。",
                onOKClickListener = { _, _ ->
                    mViewModel.deletePrivacyFriends(list)
                },
                onCancelClickListener = { _, _ ->
                    showMsg("取消")
                },
            ).show()
        }
    }

    private fun moveList(list: MutableList<PrivacyFriend>) {
        if (listValid(list)) {
            val folderNameList = mFolderList.map { it.folderName }
            DialogUtil.getSelectDialog(
                mContext,
                folderNameList.toTypedArray(),
                onClickListener = { dialog, which ->

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

    private fun listValid(list: MutableList<PrivacyFriend>): Boolean {
        LogUtil.msg(list.toString())
        if (list.size == 0) {
            showMsg("至少选择一条数据哦！")
            showMenuMore(false)
            return false
        }
        return true
    }


    override fun onItemChildClickEvent(
        datas: MutableList<PrivacyFriend>,
        view: View,
        position: Int
    ) {
        val privacyFriends=datas[position]
        when (view.id) {
            R.id.item_layout_content -> {
                val layout=view.findViewById<LinearLayout>(R.id.layout_details)
                layout.apply {
                    if(visibility==View.GONE){
                        visibility=View.VISIBLE
                    }else{
                        visibility=View.GONE
                    }
                }
            }
            R.id.item_btn_edit -> {
                val privacyInfo=datas[position]
                val bundle = Bundle()
                bundle.putSerializable(LockitFriendsEditFragment.FRAGMENT_DATA_KEY, privacyInfo)
                LockitContainerActivity.start(
                    mContext,
                    LockitFriendsEditFragment::class.java.name,
                    "编辑好友：" + privacyInfo.name,
                    bundle
                )
                LogUtil.msg("编辑")
            }
            R.id.item_btn_delete -> {
                DialogUtil.getConfirmDialog(
                    mContext,
                    "您确定要删除这条数据吗？这项操作无法恢复。",
                    onOKClickListener = { _, _ ->
                        mViewModel.deletePrivacyFriends(privacyFriends)
                    },
                    onCancelClickListener = { _, _ ->

                    },
                ).show()
            }
        }
    }

    /**
     * Refresh Data Event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onRefreshDataEvent(event: RefreshDataEvent) {
        if (PrivacyFriend::class.java.name == event.dataClassName) {
            LogUtil.d("$mViewName receiver event " + event.dataClassName)
            hasLoadData = false
        }
    }

    private val textWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            LogUtil.msg("before " + s.toString())
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            LogUtil.msg("on " + s.toString())
        }

        override fun afterTextChanged(s: Editable?) {
            LogUtil.msg("after " + s.toString())
            mViewModel.getPrivacyFriendsList(
                keyWords = s.toString()
            )
        }
    }
}