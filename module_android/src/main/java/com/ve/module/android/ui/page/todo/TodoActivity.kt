package com.ve.module.android.ui.page.todo

import android.os.Build
import android.view.*
import android.widget.PopupWindow
import androidx.appcompat.widget.Toolbar
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationBarView
import com.ve.module.android.R
import com.ve.module.android.databinding.ActivityTodoBinding
import com.ve.module.android.repository.bean.TodoTypeBean
import com.ve.module.android.ui.adapter.TodoPopupAdapter
import com.ve.module.android.ui.viewmodel.TodoViewModel
import com.ve.lib.common.base.view.vm.BaseVmActivity
import com.ve.module.android.config.Constant
import com.ve.module.android.event.TodoEvent
import com.ve.module.android.event.TodoTypeEvent
import com.ve.lib.common.utils.ui.DisplayManager
import org.greenrobot.eventbus.EventBus

/**
 * @Description hello word!
 * @Author  weiyi
 * @Date 2022/3/21
 */
class TodoActivity : BaseVmActivity<ActivityTodoBinding, TodoViewModel>(){

    override fun attachViewBinding(): ActivityTodoBinding {
        return ActivityTodoBinding.inflate(layoutInflater)
    }

    private lateinit var mToolbar: Toolbar
    private var mType = 0
    private var mTodoFragment: TodoFragment? = null
    private lateinit var datas: MutableList<TodoTypeBean>

    private fun getTypeData(): MutableList<TodoTypeBean> {
        val list = mutableListOf<TodoTypeBean>()
        list.add(TodoTypeBean(0, "只用这一个", true))
        list.add(TodoTypeBean(1, "工作", false))
        list.add(TodoTypeBean(2, "学习", false))
        list.add(TodoTypeBean(3, "生活", false))
        return list
    }

    override fun initView() {
        datas=getTypeData()
        mToolbar=mBinding.extToolbar.toolbar
        initToolbar(mBinding.extToolbar.toolbar, "TODO", true)

        val transaction = supportFragmentManager.beginTransaction()
        if (mTodoFragment == null) {
            mTodoFragment = TodoFragment.getInstance(mType)
            transaction.add(R.id.container, mTodoFragment!!, "todo")
        } else {
            transaction.show(mTodoFragment!!)
        }
        transaction.commit()
    }

    override fun attachViewModelClass(): Class<TodoViewModel> {
       return TodoViewModel::class.java
    }

    override fun initListener() {
        super.initListener()
        mBinding.bottomNavigation.apply {
            labelVisibilityMode = NavigationBarView.LABEL_VISIBILITY_LABELED
            setOnItemSelectedListener (onNavigationItemSelectedListener)
        }
        mBinding.floatingActionBtn.apply {
            setOnClickListener {
                EventBus.getDefault().post(TodoEvent(Constant.TODO_ADD, mType))
            }
        }
    }
    /**
     * NavigationItemSelect监听
     */
    private val onNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        return@OnNavigationItemSelectedListener when (item.itemId) {
            R.id.action_notodo -> {
                EventBus.getDefault().post(TodoEvent(Constant.TODO_NO, mType))
                true
            }
            R.id.action_completed -> {
                EventBus.getDefault().post(TodoEvent(Constant.TODO_DONE, mType))
                true
            }
            else -> {
                false
            }
        }
    }

    /**
     * PopupWindow
     */
    private var mSwitchPopupWindow: PopupWindow? = null
    /**
     * 初始化 PopupWindow
     */
    private fun initPopupWindow(dataList: List<TodoTypeBean>) {
        val recyclerView =
            layoutInflater.inflate(R.layout.layout_popup_todo, null) as androidx.recyclerview.widget.RecyclerView
        val adapter = TodoPopupAdapter()
        adapter.setList(dataList)
        adapter.setOnItemClickListener { adapter, view, position ->
            mSwitchPopupWindow?.dismiss()
            val itemData = adapter.data[position] as TodoTypeBean
            mType = itemData.type
            mToolbar.title = itemData.name
            adapter.data.forEachIndexed { index, any ->
                val item = any as TodoTypeBean
                item.isSelected = index == position
            }
            adapter.notifyDataSetChanged()
            mBinding.bottomNavigation.selectedItemId = R.id.action_notodo
            EventBus.getDefault().post(TodoTypeEvent(mType))
        }
        recyclerView.apply {
            layoutManager = androidx.recyclerview.widget.LinearLayoutManager(this@TodoActivity)
            this.adapter = adapter
        }
        mSwitchPopupWindow = PopupWindow(recyclerView)
        mSwitchPopupWindow?.apply {
            width = ViewGroup.LayoutParams.WRAP_CONTENT
            height = ViewGroup.LayoutParams.WRAP_CONTENT
            isOutsideTouchable = true
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                elevation = DisplayManager.dip2px(10F).toFloat()
            }
            // setBackgroundDrawable(ColorDrawable(mThemeColor))
            setOnDismissListener {
                dismiss()
            }
            setTouchInterceptor { v, event ->
                if (event.action == MotionEvent.ACTION_OUTSIDE) {
                    dismiss()
                    true
                }
                false
            }
        }
    }
    /**
     * 展示 PopupWindow
     */
    private fun showPopupWindow(dataList: MutableList<TodoTypeBean>) {
        if (mSwitchPopupWindow == null) initPopupWindow(dataList)
        if (mSwitchPopupWindow?.isShowing == true) mSwitchPopupWindow?.dismiss()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            mSwitchPopupWindow?.showAsDropDown(mToolbar, -DisplayManager.dip2px(5F), 0, Gravity.END)
        } else {
            mSwitchPopupWindow?.showAtLocation(mToolbar, Gravity.BOTTOM, -DisplayManager.dip2px(5F), 0)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menu?.clear()
        menuInflater.inflate(R.menu.menu_todo, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_todo_type -> {
                showPopupWindow(datas)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }


}