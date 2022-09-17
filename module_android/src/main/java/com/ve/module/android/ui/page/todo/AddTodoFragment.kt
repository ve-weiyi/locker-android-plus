package com.ve.module.android.ui.page.todo


import android.app.DatePickerDialog
import android.os.Bundle
import android.view.View
import com.ve.module.android.R
import com.ve.module.android.config.Constant
import com.ve.module.android.databinding.FragmentAddTodoBinding
import com.ve.module.android.repository.bean.TodoBean
import com.ve.module.android.ui.viewmodel.TodoViewModel
import com.ve.lib.common.base.view.vm.BaseVmFragment
import com.ve.module.android.event.RefreshTodoEvent
import com.ve.lib.common.utils.data.TimeUtil
import com.ve.lib.common.utils.system.LogUtil
import org.greenrobot.eventbus.EventBus
import java.util.*


/**
 * Created by chenxz on 2018/8/11.
 */
class AddTodoFragment : BaseVmFragment<FragmentAddTodoBinding, TodoViewModel>(){
    companion object {
        fun getInstance(bundle: Bundle): AddTodoFragment {
            val fragment = AddTodoFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    /**
     * Date
     */
    private var mCurrentDate = TimeUtil.formatDate(Date())

    /**
     * 类型
     */
    private var mType: Int = 0
    private var mTodoBean: TodoBean? = null

    /**
     * 新增，编辑，查看 三种状态
     */
    private var mTypeKey = ""

    /**
     * id
     */
    private var mId: Int? = 0

    /**
     * 优先级  重要（1），一般（0）
     */
    private var mPriority = 0

    override fun loadWebData() {

    }

    override fun initObserver() {
        mViewModel.addTodoCode.observe(this){
           // showMsg(it.toString())
            showAddTodo(true)
        }

        mViewModel.updateTodoCode.observe(this){
           // showMsg(it.toString())
            showUpdateTodo(true)
        }
    }
     fun showAddTodo(success: Boolean) {
        if (success) {
            showMsg(getString(R.string.save_success))
            EventBus.getDefault().post(RefreshTodoEvent(true, mType))
            activity?.finish()
        }
    }

     fun showUpdateTodo(success: Boolean) {
        if (success) {
            showMsg(getString(R.string.save_success))
            EventBus.getDefault().post(RefreshTodoEvent(true, mType))
            activity?.finish()
        }
    }
    override fun initView() {
        mType = arguments?.getInt(Constant.TODO_TYPE) ?: 0
        mTypeKey = arguments?.getString(Constant.TYPE_KEY) ?: Constant.Type.ADD_TODO_TYPE_KEY
        var bundle: Bundle? = arguments //从bundle中取出数据
        LogUtil.e(mType.toString())
        LogUtil.e(mTypeKey.toString())
        LogUtil.e(bundle.toString())

        val tv_date=mBinding.tvDate
        val et_content=mBinding.etContent
        val et_title=mBinding.etTitle
        val rb0=mBinding.rb0
        val rb1=mBinding.rb1
        val ll_date=mBinding.llDate
        val btn_save=mBinding.btnSave
        val iv_arrow_right=mBinding.ivArrowRight
        val ll_priority=mBinding.llPriority

        when (mTypeKey) {
            Constant.Type.ADD_TODO_TYPE_KEY -> {
                tv_date.text = TimeUtil.formatDate(Date())
            }
            Constant.Type.EDIT_TODO_TYPE_KEY -> {
                mTodoBean = arguments?.getSerializable(Constant.TODO_BEAN) as TodoBean ?: null
                LogUtil.e(mTodoBean.toString())
                et_title.setText(mTodoBean?.title)
                et_content.setText(mTodoBean?.content)
                tv_date.text = mTodoBean?.dateStr
                mPriority = mTodoBean?.priority ?: 0
                if (mTodoBean?.priority == 0) {
                    rb0.isChecked = true
                    rb1.isChecked = false
                } else if (mTodoBean?.priority == 1) {
                    rb0.isChecked = false
                    rb1.isChecked = true
                }
            }
            Constant.Type.SEE_TODO_TYPE_KEY -> {
                mTodoBean = arguments?.getSerializable(Constant.TODO_BEAN) as TodoBean ?: null
                et_title.setText(mTodoBean?.title)
                et_content.setText(mTodoBean?.content)
                tv_date.text = mTodoBean?.dateStr
                et_title.isEnabled = false
                et_content.isEnabled = false
                ll_date.isEnabled = false
                btn_save.visibility = View.GONE
                iv_arrow_right.visibility = View.GONE

                ll_priority.isEnabled = false
                if (mTodoBean?.priority == 0) {
                    rb0.isChecked = true
                    rb1.isChecked = false
                    rb1.visibility = View.GONE
                } else if (mTodoBean?.priority == 1) {
                    rb0.isChecked = false
                    rb1.isChecked = true
                    rb0.visibility = View.GONE
                } else {
                    ll_priority.visibility = View.GONE
                }
            }

        }


        ll_date.setOnClickListener {
            var now = ""
            if (mTypeKey == Constant.Type.EDIT_TODO_TYPE_KEY) {
                mTodoBean?.dateStr?.let {
                    now = TimeUtil.formatDate(it)
                }
            }
            val dpd = DatePickerDialog(
                requireActivity(), { view, year, month, dayOfMonth ->
                    mCurrentDate = "$year-${month + 1}-$dayOfMonth"
                    tv_date.text = mCurrentDate
                },
                1,
                2,
                3
            )
            dpd.show()
        }

        btn_save.setOnClickListener {
            val type = mType
            val title = et_title.text.toString()
            val content = et_content.text.toString()
            val date = tv_date.text.toString()
            val priority = mPriority.toString()

            val map = mutableMapOf<String, Any>()
            map["type"] = type
            map["title"] = title
            map["content"] = content
            map["date"] = date
            map["priority"] = priority

            when (mTypeKey) {
                Constant.Type.ADD_TODO_TYPE_KEY -> {
                    mViewModel.addTodo(map)
                }
                Constant.Type.EDIT_TODO_TYPE_KEY -> {
                    mViewModel.updateTodo(mTodoBean?.id ?: 0,map)
                }
            }
        }

        val rg_priority=mBinding.rgPriority
        rg_priority.setOnCheckedChangeListener { group, checkedId ->
            if (checkedId == R.id.rb0) {
                mPriority = 0
                rb0.isChecked = true
                rb1.isChecked = false
            } else if (checkedId == R.id.rb1) {
                mPriority = 1
                rb0.isChecked = false
                rb1.isChecked = true
            }
        }
    }

    override fun initListener() {

    }

    override fun attachViewModelClass(): Class<TodoViewModel> {
       return TodoViewModel::class.java
    }

    override fun attachViewBinding(): FragmentAddTodoBinding {
        return FragmentAddTodoBinding.inflate(layoutInflater)
    }

}