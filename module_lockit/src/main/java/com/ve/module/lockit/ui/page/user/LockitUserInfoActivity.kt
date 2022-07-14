package com.ve.module.lockit.ui.page.user

import android.os.Bundle
import com.ve.lib.common.base.view.vm.BaseActivity
import com.ve.lib.common.utils.ImageLoader
import com.ve.lib.common.vutils.LogUtil
import com.ve.lib.common.vutils.SpUtil
import com.ve.module.lockit.common.config.LockitSpKey
import com.ve.module.lockit.databinding.LockitActivityUserinfoBinding
import com.ve.module.lockit.respository.http.bean.LoginVO

/**
 * @Author  weiyi
 * @Date 2022/5/13
 * @Description  current project lockit-android
 */
class LockitUserInfoActivity:BaseActivity<LockitActivityUserinfoBinding>() {
    override fun attachViewBinding(): LockitActivityUserinfoBinding {
        return LockitActivityUserinfoBinding.inflate(layoutInflater)
    }

    override fun initialize(saveInstanceState: Bundle?) {
        initToolbar(mBinding.extToolbar.toolbar,"账号资料")
        val userinfo= SpUtil.getValue(LockitSpKey.SP_KEY_LOGIN_DATA_KEY, LoginVO::class.java)
        LogUtil.msg(userinfo)
        if(!userinfo.token.isEmpty()){
            showUserInfo(userinfo)
        }else{
            showMsg("您还未登录哦")
        }
    }

    private fun showUserInfo(userinfo: LoginVO) {
        val userDetail=userinfo.userInfoDTO
        mBinding.apply {
            ImageLoader.loadView(mContext,userDetail.avatar,ivAvatar)
            tvUsername.text=userDetail.username
            tvNickname.text=userDetail.nickname
//            tvRole.text=userDetail.roleList.toString()
            tvEmail.text=userDetail.email
            tvIntro.text=userDetail.intro
            tvWebsite.text=userDetail.webSite

            tvUid.text=userDetail.id.toString()
            tvSystem.text="Android 12"
            tvIpAddress.text=userDetail.ipAddress
            tvIpSource.text=userDetail.ipSource
            tvLastTime.text=userDetail.lastLoginTime
        }
    }
}