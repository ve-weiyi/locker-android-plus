package com.ve.module.locker.ui.page.privacy.card

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.InputType
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.widget.AppCompatSpinner
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.input.input
import com.afollestad.materialdialogs.lifecycle.lifecycleOwner
import com.afollestad.materialdialogs.list.listItems
import com.ve.lib.common.base.view.vm.BaseVmFragment
import com.ve.lib.common.view.widget.passwordGenerator.PasswordGeneratorDialog
import com.ve.lib.common.vutils.TimeUtil
import com.ve.lib.common.vutils.LogUtil
import com.ve.module.locker.R
import com.ve.module.locker.common.event.RefreshDataEvent
import com.ve.module.locker.databinding.LockerFragmentEditCardBinding
import com.ve.module.locker.respository.database.entity.*
import com.ve.module.locker.ui.adapter.FlowTagAdapter
import com.ve.module.locker.ui.page.privacy.EditType
import com.ve.module.locker.ui.viewmodel.LockerPrivacyCardViewModel
import org.litepal.LitePal

/**
 * @Author  weiyi
 * @Date 2022/4/11
 * @Description  current project locker-android
 */
class LockerCardEditFragment :
    BaseVmFragment<LockerFragmentEditCardBinding, LockerPrivacyCardViewModel>() {
    override fun attachViewBinding(): LockerFragmentEditCardBinding {
        return LockerFragmentEditCardBinding.inflate(layoutInflater)
    }

    override fun attachViewModelClass(): Class<LockerPrivacyCardViewModel> {
        return LockerPrivacyCardViewModel::class.java
    }

    companion object {
        const val FRAGMENT_TYPE_KEY: String = "LockerDetailsCardEditFragment.type"
        const val FRAGMENT_DATA_KEY: String = "LockerDetailsCardEditFragment.data"
    }

    /**
     * 查看,新增,编辑 三种状态
     * 查看，不可以修改，只显示
     */
    private var mType = EditType.ADD_TAG_TYPE
    
    private lateinit var mPrivacyCard: PrivacyCard
    private lateinit var mPrivacyFolder: PrivacyFolder
    private var mPrivacyTagList: MutableList<PrivacyTag>? = null

    private lateinit var mFolderList: MutableList<PrivacyFolder>
    private lateinit var mFolderSpinner: AppCompatSpinner
    private lateinit var mFolderName: List<String>

    private lateinit var mTagList: MutableList<PrivacyTag>
    private lateinit var mTagName: List<String>
    private lateinit var mTagAdapter: FlowTagAdapter

    override fun initView(savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)

        mFolderSpinner = mBinding.spacerFolder

        mFolderList = LitePal.findAll(PrivacyFolder::class.java)
        mFolderName = mFolderList.map { it.folderName }
        mTagList = LitePal.findAll(PrivacyTag::class.java)
        mTagName = mTagList.map { it.tagName }

        //拿数据
        arguments?.let {
            mType =
                it.getInt(FRAGMENT_TYPE_KEY, EditType.ADD_TAG_TYPE)
            val data = it.getSerializable(FRAGMENT_DATA_KEY)

            if (mType== EditType.EDIT_TAG_TYPE && data is PrivacyCard) {
                mPrivacyCard = data
                mPrivacyFolder = data.getPrivacyFolder()
                mPrivacyTagList = data.getPrivacyTags()
                showAtFragment(mPrivacyCard)
            } else {
                mPrivacyCard = PrivacyCard()
                mPrivacyFolder = LitePal.findFirst(PrivacyFolder::class.java)
                mPrivacyTagList = mutableListOf()
            }
        }

        mTagAdapter = FlowTagAdapter(mPrivacyTagList)
        mBinding.privacyTagFlowLayout.adapter = mTagAdapter

    }
    
    @SuppressLint("CheckResult")
    override fun initListener() {
        super.initListener()
        mBinding.btnSave.setOnClickListener {
            if (contract()) {
                doSaveOrUpdate()
            }
        }
        mBinding.ivAddTag.setOnClickListener {
            MaterialDialog(mContext).show {
                title(text = "添加标签")
                message(text = "请选择合适的标签")
                listItems(items = mTagName) { listItem, index, text ->
                    showMsg("Selected item $text at index $index")
                    LogUtil.msg(mTagList[index].toString())
                    mPrivacyTagList?.add(mTagList[index])
                    mTagAdapter = FlowTagAdapter(mPrivacyTagList)
                    mBinding.privacyTagFlowLayout.adapter = mTagAdapter
                    dismiss()
                }

                positiveButton(text = "确定") {

                }

                negativeButton(text = "取消") {

                }
                neutralButton(text = "自定义") {
                    MaterialDialog(mContext).show {
                        title(text = "输入标签名")
                        input(
                            hint = "标签名",
                            inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_CAP_WORDS
                        ) { dialog, text ->
                            showMsg("Input: $text")
                            mPrivacyTagList?.add(PrivacyTag(tagName = text.toString()))
                            mTagAdapter = FlowTagAdapter(mPrivacyTagList)
                            mBinding.privacyTagFlowLayout.adapter = mTagAdapter
                        }
                        positiveButton(text = "确定")
                        negativeButton(text = "取消")
                    }
                }
                lifecycleOwner(activity)
            }
        }
    }


    override fun initObserver() {
        super.initObserver()
        mViewModel.addPrivacyCardResult.observe(this) {
            if (it) {
                showMsg("保存成功！$it")
                mEventBus?.post(RefreshDataEvent(PrivacyCard::class.java.name))
                activity?.finish()
            } else {
                showMsg("保存失败！$it")
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.locker_privacy_edit, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_privacy_check -> {
                mBinding.apply {
                    if (contract()) {
                        doSaveOrUpdate()
                    }
                    return true
                }
            }
            R.id.action_privacy_help -> {
                MaterialDialog(mContext).show {
                    title(text = "帮助")
                    message(R.string.locker_edit_help) {
                        html { showMsg("Clicked link: $it") }
                    }
                }
            }
            R.id.action_privacy_code -> {
                PasswordGeneratorDialog(mContext).apply {
                    setPasswordCheckListener {
                        mBinding.etDetailPassword.setText(password)
                        this.dismiss()
                    }
                }.show()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showAtFragment(privacy: PrivacyCard) {
        LogUtil.msg(mPrivacyCard.toString())
        LogUtil.msg(mPrivacyFolder.toString())
        LogUtil.msg(mPrivacyTagList.toString())

        val mAdapter = ArrayAdapter<String>(mContext, android.R.layout.simple_spinner_dropdown_item)
        mAdapter.addAll(mFolderName)
        mFolderSpinner.apply {
            adapter = mAdapter
            onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    mPrivacyFolder = mFolderList[position]
                    LogUtil.msg(mPrivacyFolder.toString())
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {

                }
            }
        }
        mFolderSpinner.setSelection(mFolderName.indexOf(mPrivacyFolder.folderName))
        

        mBinding.apply {
            etDetailAccount.setText(mPrivacyCard.account)
            etDetailPassword.setText(mPrivacyCard.password)
            etDetailOwner.setText(mPrivacyCard.owner)
            etDetailRemark.setText(mPrivacyCard.remark)
        }
    }

    private fun contract(): Boolean {
        mBinding.apply {

            val account = etDetailAccount.text.toString()
            var password = etDetailPassword.text.toString()
            var owner = etDetailOwner.text.toString()
            val remark = etDetailRemark.text.toString()
            val institution = tvAppName.text.toString()

            if(account.isNullOrEmpty()){
                showMsg("用户名不能为空")
                return false
            }

            if(password.isNullOrEmpty()){
                showMsg("密码不能为空")
                return false
            }

            if(owner.isNullOrEmpty()){
                owner="unknown"
            }

            mPrivacyCard.apply {
                this.name= institution
                this.account = account
                this.password = password
                this.owner = owner
                this.remark = remark
                updateTime = TimeUtil.dateAndTime
            }

            return true
        }
    }

    private fun doSaveOrUpdate() {
        LogUtil.msg(mType.toString())
        when (mType) {
            EditType.ADD_TAG_TYPE -> {
                mViewModel.addPrivacyCard(mPrivacyCard,mPrivacyFolder,mPrivacyTagList)
            }
            EditType.EDIT_TAG_TYPE -> {
                mViewModel.addPrivacyCard(mPrivacyCard,mPrivacyFolder,mPrivacyTagList)
            }
            else -> {
                showMsg("unknown action!!")
            }
        }
    }
}