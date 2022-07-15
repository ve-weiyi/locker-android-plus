package com.ve.module.lockit.ui.page.auth

import android.content.Intent
import android.graphics.Color
import android.os.Build
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
import com.google.gson.Gson
import com.tencent.connect.common.Constants
import com.tencent.mmkv.MMKV
import com.tencent.tauth.DefaultUiListener
import com.tencent.tauth.Tencent
import com.tencent.tauth.UiError
import com.ve.lib.application.BaseApplication
import com.ve.lib.common.base.view.vm.BaseVmActivity
import com.ve.lib.common.ext.setOnclickNoRepeat
import com.ve.lib.common.ext.showToast
import com.ve.lib.common.vutils.LogUtil
import com.ve.lib.common.widget.passwordGenerator.PasswordGeneratorDialog
import com.ve.lib.common.vutils.SpUtil
import com.ve.module.lockit.LockitApplication
import com.ve.module.lockit.LockitApplication.Companion.mTencent
import com.ve.module.lockit.LockitMainActivity
import com.ve.module.lockit.R
import com.ve.module.lockit.common.config.LockitConstant
import com.ve.module.lockit.common.config.LockitSpKey
import com.ve.module.lockit.common.event.RefreshDataEvent

import com.ve.module.lockit.databinding.LockitActivityLoginBinding
import com.ve.module.lockit.respository.http.bean.LoginVO
import com.ve.module.lockit.ui.page.auth.strategy.qq.BaseUiListener
import com.ve.module.lockit.ui.page.auth.strategy.qq.QQLogin
import com.ve.module.lockit.ui.page.auth.strategy.qq.QQLoginStrategy
import com.ve.module.lockit.ui.viewmodel.LockitLoginViewModel
import org.greenrobot.eventbus.EventBus
import org.json.JSONObject

/**
 * https://www.wanandroid.com/user/login
 * @Description hello word!
 * @Author  weiyi
 * @Date 2022/3/20
 */
class LockitLoginActivity: BaseVmActivity<LockitActivityLoginBinding, LockitLoginViewModel>(){


    override fun attachViewBinding(): LockitActivityLoginBinding {
        return LockitActivityLoginBinding.inflate(layoutInflater)
    }
    override fun attachViewModelClass(): Class<LockitLoginViewModel> {
        return LockitLoginViewModel::class.java
    }
    override fun useEventBus(): Boolean = false


    private val et_username by lazy { mBinding.etUsername }
    private val et_password by lazy { mBinding.etPassword }
    private val btn_login by lazy { mBinding.btnLogin }
    private val tv_sign_up by lazy { mBinding.tvSignUp }
    /**
     * local username 从内存中读取
     */
    private val user: String by lazy {
        SpUtil.getValue( LockitSpKey.USERNAME_KEY, LockitConstant.username)
    }

    /**
     * local password 记住密码
     */
    private val pwd: String by lazy {
        SpUtil.getValue( LockitSpKey.PASSWORD_KEY, LockitConstant.password)
    }

    private val kv = MMKV.defaultMMKV()
    private lateinit var  iu: BaseUiListener
    
    override fun initView(savedInstanceState: Bundle?) {
        initToolbar(mBinding.extToolbar.toolbar, "登录", true)

        setText()
        if(LockitSpKey.isDebug){
            et_username.setText(user)
            et_password.setText(pwd)
        }
        QQLoginStrategy.init()
        QQLoginStrategy.checkLogin(this)
    }

    //这个回调改不了，只能等腾讯api更新了再改
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        //腾讯QQ回调
        QQLoginStrategy.onLoginResult(requestCode, resultCode, data)
    }
    
    override fun initObserver() {
        mViewModel.loginData.observe(this) {
            LogUtil.msg(it)
            if(it!=null) {
                loginSuccess(it)
            }else{
                loginFail()
            }
        }
    }

    override fun initListener() {
        super.initListener()
        mBinding.ibQq.setOnClickListener {
            QQLoginStrategy.doLogin(this)
        }

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
            val intent = Intent(this, LockitRegisterActivity::class.java)
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
            if(username==LockitConstant.username&&password==LockitConstant.username){
                loginSuccess(null)
            }else{
                mViewModel.login(username, password)
            }
        }
    }


    private fun loginSuccess(it: LoginVO?) {
        EventBus.getDefault().post(RefreshDataEvent(LoginVO::class.java.name,it))
        SpUtil.setValue(LockitSpKey.TOKEN_KEY, it?.token)
        SpUtil.setValue(LockitSpKey.SP_KEY_LOGIN_DATA_KEY, it)

        val username=et_username.text.toString()
        val password=et_password.text.toString()
        SpUtil.setValue(LockitSpKey.USERNAME_KEY,username)
        SpUtil.setValue(LockitSpKey.PASSWORD_KEY,password)
        startActivity(Intent(this, LockitMainActivity::class.java))
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
                    message(R.string.lockit_service_item) {
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
                    lifecycleOwner(this@LockitLoginActivity)
                }
//                LockitContainerActivity.start(mContext,lockitServiceItemFragment::class.java,"服务条款")
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