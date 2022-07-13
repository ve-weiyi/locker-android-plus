package com.ve.module.lockit.ui.page.privacy.pass

import android.os.Bundle
import android.view.View
import com.ve.lib.common.base.view.vm.BaseVmFragment
import com.ve.lib.common.ext.setOnclickNoRepeatListener
import com.ve.lib.common.utils.DialogUtil
import com.ve.lib.common.vutils.LogUtil
import com.ve.lib.common.vutils.ShareUtil
import com.ve.module.lockit.R
import com.ve.module.lockit.common.event.RefreshDataEvent
import com.ve.module.lockit.databinding.LockitFragmentDetailsPassBinding
import com.ve.module.lockit.respository.database.entity.PrivacyPass
import com.ve.module.lockit.ui.page.container.LockitContainerActivity
import com.ve.module.lockit.common.enums.EditTypeEnum
import com.ve.module.lockit.ui.viewmodel.LockitPrivacyPassViewModel
import com.ve.module.lockit.utils.AndroidUtil
import com.ve.module.lockit.utils.StickUtils
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * @Author  weiyi
 * @Date 2022/4/11
 * @Description  current project lockit-android
 */
class LockitPassDetailsFragment :
    BaseVmFragment<LockitFragmentDetailsPassBinding, LockitPrivacyPassViewModel>(),
    View.OnClickListener {

    companion object {
        const val PRIVACY_DATA_KEY = "PrivacyInfo"
    }

    override fun attachViewBinding(): LockitFragmentDetailsPassBinding {
        return LockitFragmentDetailsPassBinding.inflate(layoutInflater)
    }

    override fun attachViewModelClass(): Class<LockitPrivacyPassViewModel> {
        return LockitPrivacyPassViewModel::class.java
    }

    var mPrivacyInfo: PrivacyPass? = null

    override fun initView(savedInstanceState: Bundle?) {

        mPrivacyInfo = arguments?.getSerializable(PRIVACY_DATA_KEY) as PrivacyPass
//        LogUtil.msg(mPrivacyInfo.toString())
//        LogUtil.msg(mPrivacyInfo!!.getPrivacyTags().toString())
//        LogUtil.msg(mPrivacyInfo!!.getPrivacyFolder().toString())

        if (mPrivacyInfo != null) {
            showPrivacyInfo(mPrivacyInfo!!)
        }

    }

    override fun initListener() {
        super.initListener()
        mBinding.btnEdit.setOnclickNoRepeatListener(this)
        mBinding.btnCopy.setOnclickNoRepeatListener(this)
        mBinding.btnDelete.setOnclickNoRepeatListener(this)
        mBinding.ivBrowser.setOnclickNoRepeatListener(this)
    }

    override fun initObserver() {
        super.initObserver()

        mViewModel.deletePrivacyPassResult.observe(this) {
            if (it > 0) {
                showMsg("删除成功！$it")
                EventBus.getDefault().post(RefreshDataEvent(PrivacyPass::class.java.name))
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
            /**
             * 编辑模式
             */
            R.id.btn_edit -> {
                val bundle = Bundle()
                bundle.putInt(LockitPassEditFragment.FRAGMENT_TYPE_KEY, EditTypeEnum.EDIT_TAG_TYPE)
                bundle.putSerializable(LockitPassEditFragment.FRAGMENT_DATA_KEY, mPrivacyInfo)
                LockitContainerActivity.start(
                    mContext,
                    LockitPassEditFragment::class.java,
                    "编辑:${mPrivacyInfo?.account}",
                    bundle
                )
            }
            R.id.btn_delete -> {
                DialogUtil.getConfirmDialog(
                    mContext,
                    "确定要删除账号:$account",
                    onOKClickListener = { d, w ->
                        mViewModel.deletePrivacyPass(mPrivacyInfo!!)
                    },
                    onCancelClickListener = { d, w ->
                        showMsg("取消删除")
                    }
                ).show()

            }
            R.id.btn_copy -> {
                StickUtils.copy(mContext, "账号:$account\n密码:$password")
            }
            R.id.iv_browser -> {
                ShareUtil.goUrl(mContext,url)
            }
        }
    }

    /**
     * 显示隐私信息
     */
    private fun showPrivacyInfo(privacyInfo: PrivacyPass) {
        val folder = privacyInfo.getPrivacyFolder()
        val tags = privacyInfo.getPrivacyTags()
        val tagsName = tags.map { it.tagName }

        mBinding.apply {
            tvPrivacyName.text = privacyInfo.name
            tvPrivacyFolder.text = folder.folderName
            tvPrivacyTag.text = tagsName.toString()
            tvPrivacyUpdateTime.text = privacyInfo.updateTime
        }

        val mCheckAppInfo= AndroidUtil.getAppInfo(mContext,privacyInfo.appPackageName)
        mBinding.tvAppName.text = mCheckAppInfo?.name
        mBinding.ivAppIcon.background= mCheckAppInfo?.icon

        mBinding.apply {
            etDetailAccount.setText(privacyInfo.account)
            etDetailPassword.setText(privacyInfo.password)
//            etDetailPassword.transformationMethod = PasswordTransformationMethod.getInstance()
            etDetailUrl.setText(privacyInfo.url)
            etDetailRemark.setText(privacyInfo.remark)
        }
    }



    /**
     * Refresh Data Event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onRefreshDataEvent(event: RefreshDataEvent) {
        if (PrivacyPass::class.java.name == event.dataClassName) {
            LogUtil.d("$mViewName receiver event " + event.dataClassName)
            if (event.data is PrivacyPass) {
                mPrivacyInfo = event.data
                showPrivacyInfo(mPrivacyInfo!!)
            }
            hasLoadData = false
        }
    }
}