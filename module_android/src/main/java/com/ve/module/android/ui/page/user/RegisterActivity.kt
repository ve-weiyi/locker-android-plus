package com.ve.module.android.ui.page.user

import android.content.Intent
import android.view.View
import androidx.appcompat.widget.Toolbar
import com.ve.module.android.R
import com.ve.module.android.databinding.ActivityRegisterBinding
import com.ve.lib.common.event.LoginEvent
import com.ve.module.android.repository.bean.LoginData
import com.ve.module.android.ui.viewmodel.RegisterViewModel
import com.ve.lib.common.base.view.vm.BaseVmActivity
import com.ve.module.android.config.Constant
import com.ve.lib.common.utils.view.DialogUtil
import com.ve.lib.common.utils.system.PreferenceUtil
import org.greenrobot.eventbus.EventBus

/**
 * https://www.wanandroid.com/user/register
 * @Description hello word!
 * @Author  weiyi
 * @Date 2022/3/20
 */
class RegisterActivity : BaseVmActivity<ActivityRegisterBinding, RegisterViewModel>() {


    lateinit var mToolbar: Toolbar
    lateinit var mTitle: String
    override fun initView() {
        mToolbar=mBinding.extToolbar.toolbar
        mTitle=getString(R.string.register)
        initToolbar(mToolbar,mTitle)
    }
    override fun attachViewBinding(): ActivityRegisterBinding {
        return ActivityRegisterBinding.inflate(layoutInflater)
    }

    override fun attachViewModelClass(): Class<RegisterViewModel> {
        return RegisterViewModel::class.java
    }

    /**
     * local username
     */
    private var user: String by PreferenceUtil(Constant.USERNAME_KEY, "")

    /**
     * local password
     */
    private var pwd: String by PreferenceUtil(Constant.PASSWORD_KEY, "")
    private val mDialog by lazy {
        DialogUtil.getWaitDialog(this, getString(R.string.register_ing))
    }

    override fun showLoading() {
        mDialog.show()
    }

    override fun hideLoading() {
        mDialog.dismiss()
    }
    override fun useEventBus(): Boolean = false

    override fun enableNetworkTip(): Boolean = false

    fun registerSuccess(data: LoginData) {
        showMsg(getString(R.string.register_success))
        user = data.username
        pwd = data.password

        EventBus.getDefault().post(LoginEvent(true))
        finish()
    }

    fun registerFail() {

    }

    override fun initListener() {
        super.initListener()
        mBinding.btnRegister.setOnClickListener(onClickListener)
        mBinding.tvSignIn.setOnClickListener(onClickListener)
    }

    /**
     * OnClickListener
     */
    private val onClickListener = View.OnClickListener { view ->
        when (view.id) {
            R.id.btn_register -> {
                register()
            }
            R.id.tv_sign_in -> {
                Intent(this@RegisterActivity, LoginActivity::class.java).apply {
                    startActivity(this)
                }
                finish()
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
            }
        }
    }

    /**
     * Register
     */
    private fun register() {
        if (validate()) {
            val username: String =mBinding.etUsername.text.toString()
            val password: String = mBinding.etPassword.text.toString()
            val password2: String = mBinding.etPassword2.text.toString()
            mViewModel.register(username,
                password,
                password2)
        }
    }

    /**
     * check data
     */
    private fun validate(): Boolean {
        var valid = true
        val username: String =mBinding.etUsername.text.toString()
        val password: String = mBinding.etPassword.text.toString()
        val password2: String = mBinding.etPassword2.text.toString()
        if (username.isEmpty()) {
            mBinding.etUsername.error = getString(R.string.username_not_empty)
            valid = false
        }
        if (password.isEmpty()) {
            mBinding.etPassword.error = getString(R.string.password_not_empty)
            valid = false
        }
        if (password2.isEmpty()) {
            mBinding.etPassword2.error = getString(R.string.confirm_password_not_empty)
            valid = false
        }
        if (password != password2) {
            mBinding.etPassword2.error = getString(R.string.password_cannot_match)
            valid = false
        }
        return valid
    }
}