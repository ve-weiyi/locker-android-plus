package com.ve.module.locker.ui.page.privacy.friends

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.datetime.datePicker
import com.afollestad.materialdialogs.lifecycle.lifecycleOwner
import com.ve.lib.common.base.view.vm.BaseVmFragment
import com.ve.lib.common.vutils.LogUtil
import com.ve.lib.common.vutils.TimeUtil
import com.ve.module.locker.R
import com.ve.module.locker.common.event.RefreshDataEvent
import com.ve.module.locker.databinding.LockerFragmentEditFriendsBinding
import com.ve.module.locker.respository.database.entity.PrivacyFriend
import com.ve.module.locker.ui.page.privacy.EditType
import com.ve.module.locker.ui.viewmodel.LockerPrivacyFriendsViewModel

/**
 * @Author  weiyi
 * @Date 2022/5/6
 * @Description  current project locker-android
 */
class LockerFriendsEditFragment:
    BaseVmFragment<LockerFragmentEditFriendsBinding, LockerPrivacyFriendsViewModel>() {
    override fun attachViewBinding(): LockerFragmentEditFriendsBinding {
        return LockerFragmentEditFriendsBinding.inflate(layoutInflater)
    }

    override fun attachViewModelClass(): Class<LockerPrivacyFriendsViewModel> {
        return LockerPrivacyFriendsViewModel::class.java
    }

    companion object {
        const val FRAGMENT_TYPE_KEY: String = "LockerDetailsEditFragment.type"
        const val FRAGMENT_DATA_KEY: String = "LockerDetailsEditFragment.data"
    }

    /**
     * 查看,新增,编辑 三种状态
     * 查看，不可以修改，只显示
     */
    private var mType = EditType.ADD_TAG_TYPE
    private lateinit var mPrivacyInfo: PrivacyFriend


    override fun initView(savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        arguments?.let {
            mType = it.getInt(FRAGMENT_TYPE_KEY, EditType.ADD_TAG_TYPE)
            val data = it.getSerializable(FRAGMENT_DATA_KEY)
            LogUtil.msg(mType.toString())
            LogUtil.msg(data.toString())

            if (data is PrivacyFriend) {
                mPrivacyInfo=data
                showAtFragment(mPrivacyInfo)
            } else {
                mPrivacyInfo= PrivacyFriend()
            }
        }
    }

    override fun initListener() {
        super.initListener()
        mBinding.tvFriendsBirthday.setOnClickListener {
            MaterialDialog(mContext).show {
                datePicker { dialog, datetime ->
                    mBinding.tvFriendsBirthday.text= TimeUtil.formatDate(datetime.time)
                }
                lifecycleOwner(activity)
            }
        }
    }

    override fun initObserver() {
        super.initObserver()
        mViewModel.saveOrUpdateResult.observe(this){
            if(it){
                showMsg("保存成功")
                mEventBus?.post(RefreshDataEvent(PrivacyFriend::class.java.name, mPrivacyInfo))
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
                if(checkValid()){
                    mViewModel.saveOrUpdatePrivacyFriends(mPrivacyInfo)
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

            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun checkValid(): Boolean {
        mBinding.apply {
            mPrivacyInfo.name=etDetailName.editText!!.text.toString()
            mPrivacyInfo.nickname=etDetailNickname.editText!!.text.toString()
            mPrivacyInfo.phone=etDetailPhone.editText!!.text.toString()
            mPrivacyInfo.email=etDetailEmail.editText!!.text.toString()
            mPrivacyInfo.qq=etDetailQq.editText!!.text.toString()
            mPrivacyInfo.wechat= etDetailsWechat.editText!!.text.toString()
            mPrivacyInfo.address=etDetailAddress.editText!!.text.toString()
            mPrivacyInfo.department= etDetailDepartment.editText!!.text.toString()
            mPrivacyInfo.remark=etDetailRemark.editText!!.text.toString()

            if(rbFemale.isChecked){
                mPrivacyInfo.sex=0
            }else{
                mPrivacyInfo.sex=1
            }
        }
        mPrivacyInfo.birthday=mBinding.tvFriendsBirthday.text.toString()

        return true
    }
    private fun showAtFragment(privacyInfo: PrivacyFriend) {
        LogUtil.msg(privacyInfo.toString())
        mBinding.apply {
            etDetailName.editText!!.setText(privacyInfo.name)
            etDetailNickname.editText!!.setText(privacyInfo.nickname)
            etDetailPhone.editText!!.setText(privacyInfo.phone)
            etDetailEmail.editText!!.setText(privacyInfo.email)
            etDetailQq.editText!!.setText(privacyInfo.qq)
            etDetailsWechat.editText!!.setText(privacyInfo.wechat)
            etDetailAddress.editText!!.setText(privacyInfo.address)
            etDetailDepartment.editText!!.setText(privacyInfo.department)
            etDetailRemark.editText!!.setText(privacyInfo.remark)

            rbFemale.isChecked = mPrivacyInfo.sex==0
        }
        mBinding.tvFriendsBirthday.text=privacyInfo.birthday
    }

}