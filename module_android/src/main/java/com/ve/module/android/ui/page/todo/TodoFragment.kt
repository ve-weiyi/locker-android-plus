package com.ve.module.android.ui.page.todo

import android.os.Bundle
import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.ve.module.android.R
import com.ve.module.android.config.Constant
import com.ve.module.android.databinding.FragmentTodoBinding
import com.ve.module.android.repository.bean.TodoDataBean
import com.ve.module.android.repository.bean.TodoResponseBody
import com.ve.module.android.ui.adapter.TodoAdapter
import com.ve.module.android.ui.page.activity.CommonActivity
import com.ve.module.android.ui.viewmodel.TodoViewModel
import com.ve.lib.view.widget.layout.SwipeItemLayout
import com.ve.lib.application.BaseApplication
import com.ve.lib.common.base.view.list.BaseVmListFragment
import com.ve.module.android.event.RefreshTodoEvent
import com.ve.module.android.event.TodoEvent
import com.ve.module.android.event.TodoTypeEvent
import com.ve.lib.common.network.util.NetWorkUtil
import com.ve.lib.common.utils.view.DialogUtil
import com.ve.lib.common.utils.system.LogUtil
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * @Description hello word!
 * @Author  weiyi
 * @Date 2022/3/21
 */
class TodoFragment : BaseVmListFragment<FragmentTodoBinding, TodoViewModel, TodoDataBean>() {

    companion object {
        fun getInstance(type: Int): TodoFragment {
            val fragment = TodoFragment()
            val bundle = Bundle()
            bundle.putInt(Constant.TODO_TYPE, type)
            fragment.arguments = bundle
            return fragment
        }
    }

    private var mType: Int = 0

    /**
     * 是否是已完成 false->待办 true->已完成
     */
    private var bDone: Boolean = false

    override fun attachViewBinding(): FragmentTodoBinding {
        return FragmentTodoBinding.inflate(layoutInflater)
    }

    override fun attachViewModelClass(): Class<TodoViewModel> {
        return TodoViewModel::class.java
    }

    override fun attachAdapter(): BaseQuickAdapter<TodoDataBean, *> {
        return TodoAdapter()
    }

    override fun initListView() {
        //设置了该属性才能修改activity的toolbar
        setHasOptionsMenu(true)
        mLayoutStatusView = mBinding.fragmentRefreshLayout.multipleStatusView
        mRecyclerView = mBinding.fragmentRefreshLayout.recyclerView
        mSwipeRefreshLayout = mBinding.fragmentRefreshLayout.swipeRefreshLayout

        mType = arguments?.getInt(Constant.TODO_TYPE) ?: 0

        mRecyclerView!!.addOnItemTouchListener(SwipeItemLayout.OnSwipeItemTouchListener(activity))
    }



    override fun initObserver() {
        super.initObserver()
        mViewModel.doneTodoList.observe(this) {
            showTodoList(it)
        }

        mViewModel.noTodoList.observe(this) {
            showTodoList(it)
        }
    }

    override fun initWebData() {
        mLayoutStatusView?.showLoading()
        mCurrentPage = 0
        if (bDone) {
            mViewModel.getDoneList(1, mType)
        } else {
            mViewModel.getNoTodoList(1, mType)
        }
    }

    override fun getMoreData() {
        if (bDone) {
            mViewModel.getDoneList(mCurrentPage + 1, mType)
        } else {
            mViewModel.getNoTodoList(mCurrentPage + 1, mType)
        }
    }

    private fun showTodoList(todoResponseBody: TodoResponseBody) {
        hideLoading()
        val list = mutableListOf<TodoDataBean>()
        var bHeader = true
        todoResponseBody.datas.forEach { todoBean ->
            bHeader = true
            for (i in list.indices) {
                if (todoBean.dateStr == list[i].headerName) {
                    bHeader = false
                    break
                }
            }
            if (bHeader) {
                list.add(TodoDataBean(headerName = todoBean.dateStr))
            }
            list.add(TodoDataBean(todoBean = todoBean))
        }

        showAtAdapter(mCurrentPage == 0, list)
        //mListAdapter.setNewOrAddData(mCurrentPage == 0, list)
    }

    override fun onItemChildClickEvent(datas: MutableList<TodoDataBean>, view: View, position: Int) {
        itemChildClick(datas[position], view, position)
    }

    /**
     * Item Child Click
     * @param item TodoDataBean
     * @param view View
     * @param position Int
     */
    private fun itemChildClick(item: TodoDataBean, view: View, position: Int) {
        val data = item.todoBean ?: return
        when (view.id) {
            R.id.btn_delete -> {
                if (!NetWorkUtil.isNetworkAvailable(BaseApplication.mContext)) {
                    showMsg(resources.getString(R.string.no_network))
                    return
                }
                activity?.let {
                    DialogUtil.getConfirmDialog(
                        it,
                        resources.getString(R.string.confirm_delete),
                    ) { _, _ ->
                        mViewModel.deleteTodoById(data.id)
                        mListAdapter.removeAt(position)
                    }.show()
                }
            }
            R.id.btn_done -> {
                if (!NetWorkUtil.isNetworkAvailable(BaseApplication.mContext)) {
                    showMsg(resources.getString(R.string.no_network))
                    return
                }
                if (bDone) {
                    mViewModel.updateTodoById(data.id, 0)
                } else {
                    mViewModel.updateTodoById(data.id, 1)
                }
                mListAdapter.removeAt(position)
            }
            R.id.item_todo_content -> {
                if (bDone) {
                    //查看todo
                    val bundle = Bundle()
                    bundle.putInt(Constant.TODO_TYPE, mType)
                    bundle.putString(Constant.TYPE_KEY, Constant.Type.SEE_TODO_TYPE_KEY)
                    bundle.putSerializable(Constant.TODO_BEAN, data)
                    CommonActivity.start(
                        requireContext(),
                        getString(R.string.see),
                        AddTodoFragment::class.java.name,
                        bundle
                    )
                } else {
                    //编辑todo
                    val bundle = Bundle()
                    bundle.putInt(Constant.TODO_TYPE, mType)
                    bundle.putString(Constant.TYPE_KEY, Constant.Type.EDIT_TODO_TYPE_KEY)
                    bundle.putSerializable(Constant.TODO_BEAN, data)
                    CommonActivity.start(
                        requireContext(),
                        getString(R.string.edit),
                        AddTodoFragment::class.java.name,
                        bundle
                    )
                }
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun doTodoTypeEvent(event: TodoTypeEvent) {
        LogUtil.e("get todo event")
        mType = event.type
        bDone = false
        getRefreshData()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun doTodoEvent(event: TodoEvent) {
        LogUtil.e("get todo event")
        if (mType == event.curIndex) {
            when (event.type) {
                Constant.TODO_ADD -> {
                    val bundle = Bundle()
                    bundle.putInt(Constant.TODO_TYPE, mType)
                    CommonActivity.start(
                        requireContext(),
                        getString(R.string.add),
                        AddTodoFragment::class.java.name,
                    )

//                    Intent(activity, CommonActivity::class.java).run {
//                        putExtra(Constant.TYPE_KEY, Constant.Type.ADD_TODO_TYPE_KEY)
//                        putExtra(Constant.TODO_TYPE, mType)
//                        startActivity(this)
//                    }
                }
                Constant.TODO_NO -> {
                    bDone = false
                    getRefreshData()
                }
                Constant.TODO_DONE -> {
                    bDone = true
                    getRefreshData()
                }
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun doRefresh(event: RefreshTodoEvent) {
        LogUtil.e("get todo event")
        if (event.isRefresh) {
            if (mType == event.type) {
                getRefreshData()
            }
        }
    }

}