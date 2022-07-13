package com.ve.module.locker.ui.page.privacy.card

import android.os.Bundle
import android.view.View
import com.ve.lib.common.base.view.vm.BaseVmFragment
import com.ve.lib.common.ext.setOnclickNoRepeatListener
import com.ve.lib.common.utils.DialogUtil
import com.ve.lib.common.vutils.LogUtil
import com.ve.lib.common.vutils.ShareUtil
import com.ve.module.locker.R
import com.ve.module.locker.common.event.RefreshDataEvent
import com.ve.module.locker.databinding.LockerFragmentDetailsCardBinding
import com.ve.module.locker.respository.database.entity.PrivacyCard
import com.ve.module.locker.ui.page.container.LockerContainerActivity
import com.ve.module.locker.common.enums.EditTypeEnum
import com.ve.module.locker.ui.viewmodel.LockerPrivacyCardViewModel
import com.ve.module.locker.utils.StickUtils
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * @Author  weiyi
 * @Date 2022/4/11
 * @Description  current project locker-android
 */
class LockerCardDetailsFragment :
    BaseVmFragment<LockerFragmentDetailsCardBinding, LockerPrivacyCardViewModel>(),
    View.OnClickListener {

    companion object {
        const val PRIVACY_DATA_KEY = "PrivacyInfo"
    }

    override fun attachViewBinding(): LockerFragmentDetailsCardBinding {
        return LockerFragmentDetailsCardBinding.inflate(layoutInflater)
    }

    override fun attachViewModelClass(): Class<LockerPrivacyCardViewModel> {
        return LockerPrivacyCardViewModel::class.java
    }

    var mPrivacyInfo: PrivacyCard? = null

    override fun initView(savedInstanceState: Bundle?) {

        mPrivacyInfo = arguments?.getSerializable(PRIVACY_DATA_KEY) as PrivacyCard
        LogUtil.msg(mPrivacyInfo.toString())
        LogUtil.msg(mPrivacyInfo!!.getPrivacyTags().toString())
        LogUtil.msg(mPrivacyInfo!!.getPrivacyFolder().toString())

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

        mViewModel.deletePrivacyCardResult.observe(this) {
            if (it > 0) {
                showMsg("删除成功！$it")
                mEventBus?.post(RefreshDataEvent(PrivacyCard::class.java.name))
                activity?.finish()
            } else {
                showMsg("删除失败！$it")
            }
        }
    }

    override fun onClick(v: View?) {
        val account = mBinding.etDetailAccount.text.toString()
        val password = mBinding.etDetailPassword.text.toString()
        val url = mBinding.etDetailOwner.text.toString()
        when (v?.id) {
            /**
             * 编辑模式
             */
            R.id.btn_edit -> {
                val bundle = Bundle()
                bundle.putInt(LockerCardEditFragment.FRAGMENT_TYPE_KEY, EditTypeEnum.EDIT_TAG_TYPE)
                bundle.putSerializable(LockerCardEditFragment.FRAGMENT_DATA_KEY, mPrivacyInfo)
                LockerContainerActivity.start(
                    mContext,
                    LockerCardEditFragment::class.java,
                    "编辑:${mPrivacyInfo?.account}",
                    bundle
                )
            }
            R.id.btn_delete -> {
                DialogUtil.getConfirmDialog(
                    mContext,
                    "确定要删除账号:$account",
                    onOKClickListener = { d, w ->
                        mViewModel.deletePrivacyCard(mPrivacyInfo!!)
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
    private fun showPrivacyInfo(privacyInfo: PrivacyCard) {
        val folder = privacyInfo.getPrivacyFolder()
        val tags = privacyInfo.getPrivacyTags()
        val tagsName = tags.map { it.tagName }

        mBinding.apply {
            tvPrivacyName.text = privacyInfo.name
            tvPrivacyFolder.text = folder.folderName
            tvPrivacyTag.text = tagsName.toString()
            tvPrivacyUpdateTime.text = privacyInfo.updateTime
        }

        mBinding.tvAppName.text = privacyInfo.name

        mBinding.apply {
            etDetailAccount.setText(privacyInfo.account)
            etDetailPassword.setText(privacyInfo.password)
            etDetailOwner.setText(privacyInfo.owner)
            etDetailRemark.setText(privacyInfo.remark)
        }
    }



    /**
     * Refresh Data Event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onRefreshDataEvent(event: RefreshDataEvent) {
        if (PrivacyCard::class.java.name == event.dataClassName) {
            LogUtil.d("$mViewName receiver event " + event.dataClassName)
            if (event.data is PrivacyCard) {
                mPrivacyInfo = event.data
                showPrivacyInfo(mPrivacyInfo!!)
            }
            hasLoadData = false
        }
    }
}