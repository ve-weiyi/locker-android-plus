package com.ve.module.lockit.ui.page.category.folder

import android.app.DatePickerDialog
import android.view.View
import android.widget.*
import com.ve.lib.common.base.view.vm.BaseVmFragment
import com.ve.lib.common.utils.data.ImageLoader
import com.ve.lib.common.ext.setOnclickNoRepeatListener
import com.ve.lib.common.utils.data.TimeUtil
import com.ve.lib.application.utils.LogUtil
import com.ve.module.lockit.common.event.RefreshDataEvent
import com.ve.module.lockit.databinding.LockitFragmentDetailsFolderBinding
import com.ve.module.lockit.respository.database.entity.PrivacyFolder
import com.ve.module.lockit.common.enums.EditTypeEnum
import com.ve.module.lockit.ui.viewmodel.LockitClassifyViewModel
import org.greenrobot.eventbus.EventBus
import java.util.*

/**
 * @Author  weiyi
 * @Date 2022/4/12
 * @Description  current project lockit-android
 */
class LockitFolderDetailsFragment :
    BaseVmFragment<LockitFragmentDetailsFolderBinding, LockitClassifyViewModel>() {

    companion object {

        const val FRAGMENT_TYPE_KEY: String = "lockitDetailsFolderFragment.type"
        const val FRAGMENT_DATA_KEY: String = "lockitDetailsFolderFragment.key"
    }

    override fun attachViewBinding(): LockitFragmentDetailsFolderBinding {
        return LockitFragmentDetailsFolderBinding.inflate(layoutInflater)
    }

    override fun attachViewModelClass(): Class<LockitClassifyViewModel> {
        return LockitClassifyViewModel::class.java
    }


    /**
     * 查看,新增,编辑 三种状态
     * 查看，不可以修改，只显示
     */
    private var mType = EditTypeEnum.SEE_TAG_TYPE
    private lateinit var mData: PrivacyFolder

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
    private var mCurrentDate = TimeUtil.formatDate(Date())

    override fun initView() {

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

            if (data is PrivacyFolder) {
                mData = data
                showAtFragment(mData)
            }

            initTypeView(mType)
        }
    }


    private fun showAtFragment(privacyFolder: PrivacyFolder) {
        LogUtil.msg(privacyFolder.toString())
        mBinding.etId.setText(privacyFolder.id.toString())
        mBinding.etTitle.setText(privacyFolder.folderName)
        mBinding.etContent.setText(privacyFolder.folderDesc)
        ImageLoader.loadView(mContext, privacyFolder.folderCover, mBinding.ivCover)
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
        mViewModel.folderAddMsg.observe(this){
            showMsg(it)
            EventBus.getDefault().post(RefreshDataEvent(LockitFolderListFragment::class.java.name))
            activity?.finish()
        }

        mViewModel.folderUpdateMsg.observe(this){
            showMsg(it)
            EventBus.getDefault().post(RefreshDataEvent(LockitFolderListFragment::class.java.name))
            activity?.finish()
        }
    }

    override fun initListener() {
        super.initListener()
        mBinding.btnSave.setOnclickNoRepeatListener  {
            val folderName: String = et_title.text.toString()
            val folderCover: String = "#FF0000"
            val folderDesc = et_content.text.toString()

            when (mType) {
                EditTypeEnum.SEE_TAG_TYPE -> {

                }
                EditTypeEnum.EDIT_TAG_TYPE -> {
                    val folderId: Long = mData.id
                    val privacyFolder = PrivacyFolder(folderId, folderName, folderCover, folderDesc)
                    mViewModel.folderUpdate(privacyFolder)
                }
                EditTypeEnum.ADD_TAG_TYPE -> {
                    val privacyFolder = PrivacyFolder(-1, folderName, folderCover, folderDesc)
                    mViewModel.folderAdd(privacyFolder)
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