package com.ve.module.locker.ui.page.privacy.details

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.widget.AppCompatSpinner
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.input.input
import com.afollestad.materialdialogs.lifecycle.lifecycleOwner
import com.afollestad.materialdialogs.list.customListAdapter
import com.afollestad.materialdialogs.list.listItems
import com.ve.lib.common.base.view.vm.BaseVmFragment
import com.ve.lib.common.utils.DialogUtil
import com.ve.lib.common.view.widget.passwordGenerator.PasswordGeneratorDialog
import com.ve.lib.common.vutils.LogUtil
import com.ve.lib.common.vutils.DateTimeUtil
import com.ve.module.locker.R
import com.ve.module.locker.common.event.RefreshDataEvent
import com.ve.module.locker.databinding.LockerFragmentEditPassBinding
import com.ve.module.locker.model.db.entity.*
import com.ve.module.locker.model.db.vo.PrivacyPass
import com.ve.module.locker.ui.adapter.AppAdapter
import com.ve.module.locker.ui.adapter.FlowTagAdapter
import com.ve.module.locker.ui.viewmodel.LockerPrivacyPassViewModel
import com.ve.module.locker.utils.AndroidUtil
import com.ve.module.locker.utils.PasswordUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import org.jetbrains.anko.backgroundDrawable
import org.litepal.LitePal

/**
 * @Author  weiyi
 * @Date 2022/4/11
 * @Description  current project locker-android
 */
class LockerPassDetailsEditFragment :
    BaseVmFragment<LockerFragmentEditPassBinding, LockerPrivacyPassViewModel>() {
    override fun attachViewBinding(): LockerFragmentEditPassBinding {
        return LockerFragmentEditPassBinding.inflate(layoutInflater)
    }

    override fun attachViewModelClass(): Class<LockerPrivacyPassViewModel> {
        return LockerPrivacyPassViewModel::class.java
    }

    companion object {
        const val FRAGMENT_TYPE_KEY: String = "LockerDetailsPassEditFragment.type"
        const val FRAGMENT_DATA_KEY: String = "LockerDetailsPassEditFragment.data"
    }

    /**
     * 查看,新增,编辑 三种状态
     * 查看，不可以修改，只显示
     */
    private var mType = EditType.ADD_TAG_TYPE

    private lateinit var mPrivacyPass: PrivacyPass
    private lateinit var mPrivacyInfo: PrivacyPassInfo
    private lateinit var mPrivacyFolder: PrivacyFolder
    private lateinit var mPrivacyPassDetails: PrivacyPassDetails
    private var mPrivacyTagList: MutableList<PrivacyTag>? = null

    private lateinit var mFolderList: MutableList<PrivacyFolder>
    private lateinit var mFolderSpinner: AppCompatSpinner
    private lateinit var mFolderName: List<String>

    private lateinit var mTagList: MutableList<PrivacyTag>
    private lateinit var mTagName: List<String>
    private lateinit var mTagAdapter: FlowTagAdapter

    private lateinit var mAppAdapter: AppAdapter
    private var mCheckAppInfo: AndroidUtil.AppInfo? = null
    private var mAppInfoList: MutableList<AndroidUtil.AppInfo>? = null
    private val mAppDialog by lazy { MaterialDialog(mContext) }

    override fun initView(savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)

        mFolderSpinner = mBinding.layoutBaseInfo.spacerFolder

        mFolderList = LitePal.findAll(PrivacyFolder::class.java)
        mFolderName = mFolderList.map { it.folderName }
        mTagList = LitePal.findAll(PrivacyTag::class.java)
        mTagName = mTagList.map { it.tagName }

        arguments?.let {
            mType =
                it.getInt(LockerPassDetailsEditFragment.FRAGMENT_TYPE_KEY, EditType.ADD_TAG_TYPE)
            val data = it.getSerializable(LockerPassDetailsEditFragment.FRAGMENT_DATA_KEY)

            if (data is PrivacyPassInfo) {
                mPrivacyInfo = data
                mPrivacyFolder = data.getPrivacyFolder()
                mPrivacyPassDetails = data.getPrivacyDetails()
                mPrivacyTagList = data.getPrivacyTags()
                showAtFragment(mPrivacyInfo)
            } else {
                mPrivacyInfo = PrivacyPassInfo()
                mPrivacyFolder = LitePal.findFirst(PrivacyFolder::class.java)
                mPrivacyPassDetails = PrivacyPassDetails(account = "", password = "")
            }
        }

        mTagAdapter = FlowTagAdapter(mPrivacyTagList)
        mBinding.layoutBaseInfo.privacyTagFlowLayout.adapter = mTagAdapter

        mAppAdapter = AppAdapter()
        mCheckAppInfo =
            AndroidUtil.getAppByPackageName(mContext, mPrivacyPassDetails.appPackageName)
        LogUtil.msg(mCheckAppInfo.toString())
        mBinding.tvAppName.text = mCheckAppInfo?.name
        mBinding.ivAppIcon.backgroundDrawable = mCheckAppInfo?.icon
    }

    override fun initWebData() {
        super.initWebData()
    }


    @SuppressLint("CheckResult")
    override fun initListener() {
        super.initListener()
        mBinding.etDetailPassword.editText!!.addTextChangedListener(textWatcher)
        mBinding.layoutBaseInfo.ivAddTag.setOnClickListener {
            MaterialDialog(mContext).show {
                title(text = "添加标签")
                message(text = "请选择合适的标签")
                listItems(items = mTagName) { listItem, index, text ->
                    showMsg("Selected item $text at index $index")
                    LogUtil.msg(mTagList[index].toString())
                    mPrivacyTagList?.add(mTagList[index])
                    mTagAdapter = FlowTagAdapter(mPrivacyTagList)
                    mBinding.layoutBaseInfo.privacyTagFlowLayout.adapter = mTagAdapter
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
                            mBinding.layoutBaseInfo.privacyTagFlowLayout.adapter = mTagAdapter
                        }
                        positiveButton(text = "确定")
                        negativeButton(text = "取消")
                    }
                }
                lifecycleOwner(activity)
            }
        }

        lifecycleScope.launch {
            withContext(Dispatchers.Default) {
                suspendCancellableCoroutine {
                    if (mAppInfoList.isNullOrEmpty()){
                        mAppInfoList = AndroidUtil.getAllAppInfo(mContext)
                    }

//                    runOnUiThread {
//                        mAppAdapter.setNewInstance(mAppInfoList)
//                    }
                }
            }
        }

        mBinding.ivAppIcon.setOnClickListener {
//            mAppAdapter.setList(mAppInfoList)
            mAppAdapter.setNewInstance(mAppInfoList)
            mAppAdapter.setOnItemClickListener { adapter, view, position ->
                mCheckAppInfo = adapter.data[position] as AndroidUtil.AppInfo
                LogUtil.msg(mCheckAppInfo.toString())
                mAppDialog.message(text = "已选中 " + mCheckAppInfo!!.name)
            }

            mAppDialog.show {
                title(text = "app列表")
                message(text = "已安装的app")
                customListAdapter(mAppAdapter, LinearLayoutManager(mContext))
                positiveButton(text = "确定") {
                    mBinding.tvAppName.text = mCheckAppInfo?.name
                    mBinding.ivAppIcon.background = mCheckAppInfo?.icon
                }
                negativeButton(text = "取消") {

                }
                lifecycleOwner(activity)
            }
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
            val level = PasswordUtils.checkPasswordLevel(s.toString())
            val colorStateList = ColorStateList.valueOf(level.colorInt)

            mBinding.etDetailPassword.apply {
                setErrorIconOnClickListener {
                    if (editText!!.transformationMethod is HideReturnsTransformationMethod) {
                        editText!!.transformationMethod = PasswordTransformationMethod.getInstance()
                    } else {
                        editText!!.transformationMethod =
                            HideReturnsTransformationMethod.getInstance()
                    }
                }
                error = level.desc
                boxStrokeErrorColor = colorStateList
                setErrorTextColor(colorStateList)
                setErrorIconTintList(colorStateList)
                setBoxStrokeColorStateList(colorStateList)
            }
            LogUtil.msg("level " + level.toString())
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.locker_privacy_edit, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_privacy_check -> {
                LogUtil.msg("onOptionsItemSelected")
                mBinding.apply {
                    if (checkValid()) {
                        if (mPrivacyPassDetails.password!!.isEmpty()) {
                            val dialog = DialogUtil.getConfirmDialog(
                                mContext,
                                "未设置密码，初始化密码为12345678。",
                                onOKClickListener = { d, w ->
                                    mPrivacyPassDetails.password = "12345678"
                                    doSaveOrUpdate()
                                },
                                onCancelClickListener = { d, w ->

                                }
                            )
                            dialog.show()
                        } else {
                            doSaveOrUpdate()
                        }
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
                        mBinding.etDetailPassword.editText!!.setText(password)
                        this.dismiss()
                    }
                }.show()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun initObserver() {
        super.initObserver()
        mViewModel.addPrivacyPassResult.observe(this) {
            mEventBus?.post(RefreshDataEvent(PrivacyPassInfo::class.java.name, mPrivacyPass))
            showMsg(it)
//            activity?.finish()
        }
        mViewModel.updatePrivacyPassResult.observe(this) {
            mEventBus?.post(RefreshDataEvent(PrivacyPassInfo::class.java.name, mPrivacyPass))
            showMsg(it)
//            activity?.finish()
        }

    }

    private fun checkValid(): Boolean {
        mBinding.apply {

            val account = etDetailAccount.editText!!.text.toString()
            var password = etDetailPassword.editText!!.text.toString()
            val url = etDetailUrl.editText!!.text.toString()
            val phone = etDetailPhone.editText!!.text.toString()
            val app = ""
            val remark = etDetailRemark.editText!!.text.toString()


            var name: String = layoutBaseInfo.etPrivacyName.editText!!.text.toString()
            var desc = layoutBaseInfo.etPrivacyDesc.editText!!.text.toString()

            if (name.isEmpty()) {
                name = account + "的密码 "
            }
            if (desc.isEmpty()) {
                desc = account + "的密码 " + DateTimeUtil.date
            }
            mPrivacyPassDetails.apply {
                this.appPackageName = app
                this.account = account
                this.password = password
                this.phone = phone
                this.url = url
                this.remark = remark
            }

            mPrivacyInfo.apply {
                privacyName = name
                privacyDesc = desc
                privacyCover = "#00AA00"
                updateTime = DateTimeUtil.dateAndTime
            }

            mPrivacyPass = PrivacyPass(
                mPrivacyInfo,
                mPrivacyPassDetails,
                mPrivacyFolder,
                mPrivacyTagList
            )

            mPrivacyPassDetails.appPackageName = mCheckAppInfo!!.packageName
            return true
        }
    }

    private fun showAtFragment(privacyInfoPass: PrivacyPassInfo) {
        LogUtil.msg(mPrivacyInfo.toString())
        LogUtil.msg(mPrivacyPassDetails.toString())
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

        mBinding.layoutBaseInfo.apply {
            etPrivacyName.editText!!.setText(privacyInfoPass.privacyName)
            etPrivacyDesc.editText!!.setText(privacyInfoPass.privacyDesc)
        }

        mBinding.apply {
//            etDetailApp.editText!!.setText(mPrivacyPassDetails.app)
            etDetailAccount.editText!!.setText(mPrivacyPassDetails.account)
            etDetailPassword.editText!!.setText(mPrivacyPassDetails.password)
            etDetailUrl.editText!!.setText(mPrivacyPassDetails.url)
            etDetailPhone.editText!!.setText(mPrivacyPassDetails.phone)
            etDetailRemark.editText!!.setText(mPrivacyPassDetails.remark)
        }

    }

    private fun doSaveOrUpdate() {
        LogUtil.msg(mType.toString())
        when (mType) {
            EditType.ADD_TAG_TYPE -> {
                mViewModel.addPrivacyPass(mPrivacyPass)
            }
            EditType.EDIT_TAG_TYPE -> {
                mViewModel.updatePrivacyPass(mPrivacyPass)
            }
            else -> {
                showMsg("unknown action!!")
            }
        }
    }
}