package com.ve.module.locker.ui.page.privacy.details

import android.os.Bundle
import android.text.method.PasswordTransformationMethod
import android.view.View
import com.ve.lib.common.base.view.vm.BaseVmFragment
import com.ve.lib.common.ext.setOnclickNoRepeatListener
import com.ve.lib.common.utils.DialogUtil
import com.ve.lib.common.vutils.LogUtil
import com.ve.module.locker.R
import com.ve.module.locker.common.event.RefreshDataEvent
import com.ve.module.locker.databinding.LockerFragmentSeePassBinding
import com.ve.module.locker.model.db.entity.PrivacyPassDetails
import com.ve.module.locker.model.db.entity.PrivacyPassInfo
import com.ve.module.locker.model.db.vo.PrivacyPass
import com.ve.module.locker.ui.page.container.LockerContainerActivity
import com.ve.module.locker.ui.viewmodel.LockerPrivacyPassViewModel
import com.ve.module.locker.utils.AndroidUtil
import com.ve.module.locker.utils.StickUtils
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode


/**
 * @Author  weiyi
 * @Date 2022/4/11
 * @Description  current project locker-android
 */
class LockerPassDetailsSeeFragment :
    BaseVmFragment<LockerFragmentSeePassBinding, LockerPrivacyPassViewModel>(),
    View.OnClickListener {

    companion object {
        const val PRIVACY_DATA_KEY = "DetailsPass"

    }

    override fun attachViewBinding(): LockerFragmentSeePassBinding {
        return LockerFragmentSeePassBinding.inflate(layoutInflater)
    }

    override fun attachViewModelClass(): Class<LockerPrivacyPassViewModel> {
        return LockerPrivacyPassViewModel::class.java
    }

    var mPrivacyInfoPass: PrivacyPassInfo? = null

    override fun initView(savedInstanceState: Bundle?) {

        mPrivacyInfoPass = arguments?.getSerializable(PRIVACY_DATA_KEY) as PrivacyPassInfo
        LogUtil.msg(mPrivacyInfoPass.toString())
        LogUtil.msg(mPrivacyInfoPass!!.getPrivacyTags().toString())
        LogUtil.msg(mPrivacyInfoPass!!.getPrivacyFolder().toString())
        LogUtil.msg(mPrivacyInfoPass!!.getPrivacyDetails().toString())

        if (mPrivacyInfoPass != null) {
            showPrivacyInfo(mPrivacyInfoPass!!)
            showPrivacyDetails(mPrivacyInfoPass!!.getPrivacyDetails())
        }

    }

    override fun initListener() {
        super.initListener()
        mBinding.btnEdit.setOnclickNoRepeatListener(this)
        mBinding.btnCopy.setOnclickNoRepeatListener(this)
        mBinding.btnDelete.setOnclickNoRepeatListener(this)
        mBinding.tvCopyAccount.setOnclickNoRepeatListener(this)
        mBinding.tvCopyPassword.setOnclickNoRepeatListener(this)
        mBinding.tvCopyUrl.setOnclickNoRepeatListener(this)
    }

    override fun initObserver() {
        super.initObserver()
        mViewModel.deletePrivacyPassResult.observe(this) {

            if (it > 0) {
                showMsg("删除成功！$it")
                mEventBus?.post(RefreshDataEvent(PrivacyPassInfo::class.java.name))
                activity?.finish()
            } else {
                showMsg("删除失败！$it")
            }
        }
    }

    override fun onClick(v: View?) {
        val account = mBinding.etDetailAccount.text.toString()
        val password = mBinding.etDetailPassword.text.toString()
        val url = mBinding.etDetailUrl.text.toString()
        when (v?.id) {
            R.id.btn_edit -> {
                val bundle = Bundle()
                bundle.putInt(
                    LockerPassDetailsEditFragment.FRAGMENT_TYPE_KEY,
                    EditType.EDIT_TAG_TYPE
                )
                bundle.putSerializable(
                    LockerPassDetailsEditFragment.FRAGMENT_DATA_KEY,
                    mPrivacyInfoPass
                )
                LockerContainerActivity.start(
                    mContext,
                    LockerPassDetailsEditFragment::class.java,
                    "编辑:${mPrivacyInfoPass?.privacyName}",
                    bundle
                )
            }
            R.id.btn_delete -> {
                DialogUtil.getConfirmDialog(
                    mContext,
                    "确定要删除账号:$account",
                    onOKClickListener = { d, w ->
                        mViewModel.deletePrivacyPass(mPrivacyInfoPass!!)
                    },
                    onCancelClickListener = { d, w ->
                        showMsg("取消删除")
                    }
                ).show()

            }
            R.id.btn_copy -> {

                StickUtils.copy(mContext, "账号:$account\n密码:$password")
            }
            R.id.tv_copy_account -> {
                StickUtils.copy(mContext, account)
            }
            R.id.tv_copy_password -> {
                StickUtils.copy(mContext, password)
            }
            R.id.tv_copy_url -> {
                StickUtils.copy(mContext, url)
            }
        }
    }

    private fun showPrivacyInfo(privacyInfoPass: PrivacyPassInfo) {
        val folder = privacyInfoPass.getPrivacyFolder()
        val tags = privacyInfoPass.getPrivacyTags()
        val tagsName = tags.map { it.tagName }

        mBinding.apply {
            tvPrivacyName.text = privacyInfoPass.privacyName
            tvPrivacyDesc.text = privacyInfoPass.privacyDesc
            tvPrivacyFolder.text = folder.folderName
            tvPrivacyTag.text = tagsName.toString()
            tvPrivacyCreateTime.text = privacyInfoPass.createTime
            tvPrivacyUpdateTime.text = privacyInfoPass.updateTime
        }
    }


    private fun showPrivacyDetails(privacyDetails: PrivacyPassDetails) {

        val mCheckAppInfo= AndroidUtil.getAppByPackageName(mContext,privacyDetails.appPackageName)
        mBinding.tvAppName.text = mCheckAppInfo?.name
        mBinding.ivAppIcon.background= mCheckAppInfo?.icon

        mBinding.apply {
            etDetailAccount.setText(privacyDetails.account)
            etDetailPassword.setText(privacyDetails.password)
            etDetailPassword.transformationMethod = PasswordTransformationMethod.getInstance()
            etDetailUrl.setText(privacyDetails.url)
            etDetailPhone.setText(privacyDetails.phone)
            etDetailRemark.setText(privacyDetails.remark)
        }
    }

    /**
     * Refresh Data Event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onRefreshDataEvent(event: RefreshDataEvent) {
        if (PrivacyPassInfo::class.java.name == event.dataClassName) {
            LogUtil.d("$mViewName receiver event " + event.dataClassName)
            if (event.data is PrivacyPass) {
                mPrivacyInfoPass = event.data.privacyInfo
                showPrivacyInfo(mPrivacyInfoPass!!)
                showPrivacyDetails(mPrivacyInfoPass!!.getPrivacyDetails())
            }
            hasLoadData = false
        }
    }
}