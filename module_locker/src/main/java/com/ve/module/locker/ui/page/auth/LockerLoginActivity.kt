package com.ve.module.locker.ui.page.auth

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.view.View
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.lifecycle.lifecycleOwner
import com.ve.lib.common.base.view.vm.BaseVmActivity
import com.ve.lib.common.ext.setOnclickNoRepeat
import com.ve.lib.common.utils.DialogUtil
import com.ve.lib.common.utils.PreferenceUtil
import com.ve.lib.common.view.widget.passwordGenerator.PasswordGeneratorDialog
import com.ve.lib.common.vutils.LogUtil
import com.ve.lib.common.vutils.SpUtil
import com.ve.module.locker.LockerMainActivity
import com.ve.module.locker.R
import com.ve.module.locker.common.config.LockerConstant
import com.ve.module.locker.common.config.LockerLifecycle
import com.ve.module.locker.common.config.SettingConstant

import com.ve.module.locker.databinding.LockerActivityLoginBinding
import com.ve.module.locker.ui.viewmodel.LockerLoginViewModel

/**
 * https://www.wanandroid.com/user/login
 * @Description hello word!
 * @Author  weiyi
 * @Date 2022/3/20
 */
class LockerLoginActivity: BaseVmActivity<LockerActivityLoginBinding, LockerLoginViewModel>(){

    override fun initView(savedInstanceState: Bundle?) {
        initToolbar(mBinding.extToolbar.toolbar, "登录", true)

        setText()
        if(SettingConstant.isDebug){
            et_username.setText(LockerConstant.username)
            et_password.setText(LockerConstant.password)
        }
    }

    override fun attachViewBinding(): LockerActivityLoginBinding {
        return LockerActivityLoginBinding.inflate(layoutInflater)
    }
    override fun attachViewModelClass(): Class<LockerLoginViewModel> {
        return LockerLoginViewModel::class.java
    }
    override fun useEventBus(): Boolean = false

    /**
     * local username 从内存中读取
     */
    private var user: String by PreferenceUtil(LockerConstant.USERNAME_KEY, "")
    /**
     * local password
     */
    private var pwd: String by PreferenceUtil(LockerConstant.PASSWORD_KEY, "")
    /**
     * token
     */
    private var token: String by PreferenceUtil(LockerConstant.TOKEN_KEY, "")


    private val et_username by lazy { mBinding.etUsername }
    private val et_password by lazy { mBinding.etPassword }
    private val btn_login by lazy { mBinding.btnLogin }
    private val tv_sign_up by lazy { mBinding.tvSignUp }

    private val mDialog by lazy {
        DialogUtil.getWaitDialog(this, "正在登录...")
    }

    override fun showLoading() {
        // mDialog.show()
    }

    override fun hideLoading() {
        // mDialog.dismiss()
    }

    override fun initObserver() {
        mViewModel.loginState.observe(this) {
            LockerLifecycle.loginState.value=it
            if (it) {
                showMsg("登录成功")
            } else {
                showMsg("登录失败")
            }
        }

        mViewModel.loginData.observe(this) {
            LockerLifecycle.loginData.value=it
            SpUtil.setValue(LockerConstant.TOKEN_KEY,it.accessToken)
            LogUtil.msg(mViewName+"\n--${it.accessToken}\n--"+ SpUtil.getValue(
                LockerConstant.TOKEN_KEY,"---")!!)
            if(it!=null) {
                loginSuccess()
            }else{
                loginFail()
            }
        }
    }

    override fun initListener() {
        super.initListener()
        btn_login.apply {
            setOnclickNoRepeat {
                if (!mBinding.cbServiceAgreement.isChecked) {
                    showMsg("同意服务协议与隐私政策后才能登录")
                    return@setOnclickNoRepeat
                }
                login()
            }
            setBackgroundColor(mThemeColor)
        }

        tv_sign_up.setOnClickListener{
            val intent = Intent(this, LockerRegisterActivity::class.java)
            startActivity(intent)
//            finish()
//            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        }
    }
    /**
     * Login
     */
    private fun login() {
        if (validate()) {
            val username=et_username.text.toString()
            val password=et_password.text.toString()
            //本地登录，无需验证网络
            if(username==LockerConstant.username&&password==LockerConstant.username){
                loginSuccess()
            }else{
                mViewModel.login(username, password)
            }
        }
    }


    private fun loginSuccess() {
        startActivity(Intent(this, LockerMainActivity::class.java))
        finish()
    }
    private fun loginFail() {

    }

    /**
     * Check UserName and PassWord
     */
    private fun validate(): Boolean {
        var valid = true
        val username: String = et_username.text.toString()
        val password: String = et_password.text.toString()

        if (username.isEmpty()) {
            et_username.error = "用户名不能为空"
            valid = false
        }
        if (password.isEmpty()) {
            et_password.error = "密码不能为空"
            valid = false
        }
        return valid
    }

    private fun setText() {
        val spanBuilder = SpannableStringBuilder("同意")
        val color = mThemeColor

        var span = SpannableString("服务条款")
        span.setSpan(object : ClickableSpan() {
            override fun onClick(widget: View) {
                MaterialDialog(mContext).show {
                    title(text="服务条款")
                    message(R.string.locker_service_item) {
                        html { showMsg("Clicked link: $it") }
//                        lineSpacing(1.4f)
                    }
                    positiveButton(text = "同意"){
                        // Do something
                    }
                    negativeButton(text = "取消"){

                    }
                    neutralButton(text = "确定"){

                    }
                    lifecycleOwner(this@LockerLoginActivity)
                }
//                LockerContainerActivity.start(mContext,LockerServiceItemFragment::class.java,"服务条款")
            }
        }, 0, span.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

        span.setSpan(ForegroundColorSpan(color), 0, span.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        spanBuilder.append(span)

        spanBuilder.append("与")

        span = SpannableString("隐私政策")
        span.setSpan(object : ClickableSpan() {
            override fun onClick(widget: View) {
                showMsg("隐私政策")

                PasswordGeneratorDialog(mContext).show()
            }
        }, 0, span.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        span.setSpan(ForegroundColorSpan(color), 0, span.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        spanBuilder.append(span)

        mBinding.tvServiceAgreement.movementMethod = LinkMovementMethod.getInstance()
        mBinding.tvServiceAgreement.text = spanBuilder
        //设置高亮颜色透明，因为点击会变色
        mBinding.tvServiceAgreement.highlightColor = Color.parseColor("#00000000")
        //ContextCompat.getColor(applicationContext, R.color.transparent)
    }
}