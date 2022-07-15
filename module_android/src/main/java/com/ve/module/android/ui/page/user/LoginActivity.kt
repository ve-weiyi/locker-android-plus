package com.ve.module.android.ui.page.user

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
import androidx.appcompat.widget.Toolbar
import com.ve.module.android.R
import com.ve.module.android.databinding.ActivityLoginBinding
import com.ve.lib.common.event.LoginEvent
import com.ve.module.android.repository.bean.LoginData
import com.ve.module.android.ui.viewmodel.LoginViewModel
import com.ve.lib.common.base.view.vm.BaseVmActivity
import com.ve.lib.common.ext.setOnclickNoRepeat
import com.ve.module.android.config.Constant
import com.ve.lib.common.vutils.DialogUtil
import com.ve.lib.common.utils.PreferenceUtil
import org.greenrobot.eventbus.EventBus

/**
 * https://www.wanandroid.com/user/login
 * @Description hello word!
 * @Author  weiyi
 * @Date 2022/3/20
 */
class LoginActivity: BaseVmActivity<ActivityLoginBinding, LoginViewModel>(){

    lateinit var mToolbar: Toolbar
    lateinit var mTitle: String
    override fun initView(savedInstanceState: Bundle?) {
        mToolbar=mBinding.extToolbar.toolbar
        mTitle=getString(R.string.login)
        initToolbar(mToolbar,mTitle)
        setText()
        et_username.setText(Constant.username)
        et_password.setText(Constant.password)
    }

    override fun attachViewBinding(): ActivityLoginBinding {
        return ActivityLoginBinding.inflate(layoutInflater)
    }
    override fun attachViewModelClass(): Class<LoginViewModel> {
        return LoginViewModel::class.java
    }

    override fun useEventBus(): Boolean = false

    override fun enableNetworkTip(): Boolean = false
    /**
     * local username 从内存中读取
     */
    private var user: String by PreferenceUtil(Constant.USERNAME_KEY, "")
    /**
     * local password
     */
    private var pwd: String by PreferenceUtil(Constant.PASSWORD_KEY, "")
    /**
     * token
     */
    private var token: String by PreferenceUtil(Constant.TOKEN_KEY, "")


    private val et_username by lazy { mBinding.etUsername }
    private val et_password by lazy { mBinding.etPassword }
    private val toolbar by lazy { mBinding.extToolbar.toolbar }
    private val btn_login by lazy { mBinding.btnLogin }
    private val tv_sign_up by lazy { mBinding.tvSignUp }

    private val mDialog by lazy {
        DialogUtil.getWaitDialog(this, getString(R.string.login_ing))
    }


    override fun initObserver() {
        mViewModel.loginState.observe(this) {
            showLoading()
            if (it) {
                showMsg("登录成功")
            } else {
                showMsg("登录失败")
            }
        }

        mViewModel.loginData.observe(this) {
            if(it!=null) {
                loginSuccess(it)
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

        tv_sign_up.setOnclickNoRepeat {
            val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
            startActivity(intent)
            finish()
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        }
    }
    /**
     * Login
     */
    private fun login() {
        if (validate()) {
            mViewModel.login(et_username.text.toString(), et_password.text.toString())
        }
    }


    private fun loginSuccess(data: LoginData) {
        showMsg(getString(R.string.login_success))
        isLogin = true
        user = data.username
        pwd = data.password
        token = data.token

        EventBus.getDefault().post(LoginEvent(true))
        //startActivity(Intent(this, MainActivity::class.java))
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
            et_username.error = getString(R.string.username_not_empty)
            valid = false
        }
        if (password.isEmpty()) {
            et_password.error = getString(R.string.password_not_empty)
            valid = false
        }
        return valid
    }

    private fun setText() {
        val spanBuilder = SpannableStringBuilder("同意")
        val color = mThemeColor

        var span = SpannableString("服务协议")
        span.setSpan(object : ClickableSpan() {
            override fun onClick(widget: View) {
                showMsg("服务协议")
            }
        }, 0, span.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

        span.setSpan(ForegroundColorSpan(color), 0, span.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        spanBuilder.append(span)

        spanBuilder.append("与")

        span = SpannableString("隐私政策")
        span.setSpan(object : ClickableSpan() {
            override fun onClick(widget: View) {
                showMsg("隐私政策")
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