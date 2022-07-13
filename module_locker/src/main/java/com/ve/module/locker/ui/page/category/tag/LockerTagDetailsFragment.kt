package com.ve.module.locker.ui.page.category.tag

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.*
import com.ve.lib.common.base.view.vm.BaseVmFragment
import com.ve.lib.common.utils.ImageLoader
import com.ve.lib.common.ext.formatCurrentDate
import com.ve.lib.common.ext.setOnclickNoRepeatListener
import com.ve.lib.common.vutils.LogUtil
import com.ve.module.locker.common.event.RefreshDataEvent
import com.ve.module.locker.databinding.LockerFragmentDetailsTagBinding
import com.ve.module.locker.respository.database.entity.PrivacyTag
import com.ve.module.locker.common.enums.EditTypeEnum
import com.ve.module.locker.ui.viewmodel.LockerClassifyViewModel
import java.util.*

/**
 * @Author  weiyi
 * @Date 2022/4/12
 * @Description  current project locker-android
 */
class LockerTagDetailsFragment :
    BaseVmFragment<LockerFragmentDetailsTagBinding, LockerClassifyViewModel>() {

    companion object {

        const val FRAGMENT_TYPE_KEY: String = "LockerDetailsTagFragment.type"
        const val FRAGMENT_DATA_KEY: String = "LockerDetailsTagFragment.key"
    }

    override fun attachViewBinding(): LockerFragmentDetailsTagBinding {
        return LockerFragmentDetailsTagBinding.inflate(layoutInflater)
    }

    override fun attachViewModelClass(): Class<LockerClassifyViewModel> {
        return LockerClassifyViewModel::class.java
    }


    /**
     * 查看,新增,编辑 三种状态
     * 查看，不可以修改，只显示
     */
    private var mType = EditTypeEnum.SEE_TAG_TYPE
    private lateinit var mData: PrivacyTag

    private lateinit var tv_date: TextView
    private lateinit var et_id: EditText
    private lateinit var et_title: EditText
    private lateinit var et_content: EditText

    private lateinit var rb0: RadioButton
    private lateinit var rb1: RadioButton
    private lateinit var ll_date: LinearLayout
    private lateinit var btn_save: Button
    private lateinit var iv_arrow_right: ImageView
    private lateinit var ll_priority: LinearLayout
    /**
     * Date
     */
    private var mCurrentDate = formatCurrentDate()

    override fun initView(savedInstanceState: Bundle?) {

        tv_date = mBinding.tvDate
        et_id = mBinding.etId
        et_title = mBinding.etTitle
        et_content = mBinding.etContent
        rb0 = mBinding.rb0
        rb1 = mBinding.rb1
        ll_date = mBinding.llDate
        btn_save = mBinding.btnSave
        iv_arrow_right = mBinding.ivArrowRight
        ll_priority = mBinding.llPriority


        arguments?.let {
            mType = it.getInt(FRAGMENT_TYPE_KEY, EditTypeEnum.SEE_TAG_TYPE)
            val data = it.getSerializable(FRAGMENT_DATA_KEY)

            if (data is PrivacyTag) {
                mData = data
                showAtFragment(mData)
            }

            initTypeView(mType)
        }
    }


    private fun showAtFragment(privacyTag: PrivacyTag) {
        LogUtil.msg(privacyTag.toString())
        mBinding.etId.setText(privacyTag.id.toString())
        mBinding.etTitle.setText(privacyTag.tagName)
        mBinding.etContent.setText(privacyTag.tagDesc)
        ImageLoader.loadView(mContext, privacyTag.tagCover, mBinding.ivCover)
    }

    private fun initTypeView(type: Int) {
        mBinding.etId.isEnabled = false
        when (mType) {
            EditTypeEnum.SEE_TAG_TYPE -> {
                mBinding.etTitle.isEnabled = false
                mBinding.etContent.isEnabled = false
                mBinding.btnSave.visibility = View.GONE
                mBinding.ivArrowRight.visibility = View.GONE
            }
            EditTypeEnum.EDIT_TAG_TYPE -> {

            }
            EditTypeEnum.ADD_TAG_TYPE -> {
                mBinding.tvDate.text = mCurrentDate
            }
            else -> {

            }
        }
    }

    override fun initObserver() {
        super.initObserver()
        mViewModel.tagAddMsg.observe(this){
            showMsg(it)
            mEventBus?.post(RefreshDataEvent(LockerTagListFragment::class.java.name))
            activity?.finish()
        }

        mViewModel.tagDeleteMsg.observe(this){
            showMsg(it)
            mEventBus?.post(RefreshDataEvent(LockerTagListFragment::class.java.name))
            activity?.finish()
        }
    }

    override fun initListener() {
        super.initListener()
        mBinding.btnSave.setOnclickNoRepeatListener  {
            val tagName: String = et_title.text.toString()
            val tagCover: String = "#FF0000"
            val tagDesc = et_content.text.toString()

            when (mType) {
                EditTypeEnum.SEE_TAG_TYPE -> {

                }
                EditTypeEnum.EDIT_TAG_TYPE -> {
                    val tagId: Long = mData.id
                    val privacyTag = PrivacyTag(tagId, tagName, tagCover, tagDesc)
                    mViewModel.tagUpdate(privacyTag)
                }
                EditTypeEnum.ADD_TAG_TYPE -> {
                    val privacyTag = PrivacyTag(-1, tagName, tagCover, tagDesc)
                    mViewModel.saveTag(privacyTag)
                }
                else -> {

                }
            }
        }

        mBinding.llDate.setOnclickNoRepeatListener {
            var now = Calendar.getInstance()
            val dpd = DatePickerDialog(
                requireActivity(), { view, year, month, dayOfMonth ->
                    mCurrentDate = "$year-${month + 1}-$dayOfMonth"
                    tv_date.text = mCurrentDate
                },
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)
            )
            dpd.show()
        }
    }

}