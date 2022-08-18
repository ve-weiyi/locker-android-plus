package com.ve.module.lockit.ui.page.auth

import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import com.ve.lib.common.base.view.vm.BaseVmActivity
import com.ve.lib.common.ext.setOnclickNoRepeatListener
import com.ve.lib.common.utils.view.DialogUtil
import com.ve.module.lockit.common.config.LockitSpKey
import com.ve.module.lockit.databinding.LockitActivityRegisterBinding
import com.ve.module.lockit.ui.viewmodel.LockitRegisterViewModel
import com.ve.module.lockit.utils.PasswordUtils


/**
 * @Description hello word!
 * @Author  weiyi
 * @Date 2022/4/9
 */
class LockitRegisterActivity :
    BaseVmActivity<LockitActivityRegisterBinding, LockitRegisterViewModel>() {
    override fun attachViewBinding(): LockitActivityRegisterBinding {
        return LockitActivityRegisterBinding.inflate(layoutInflater)
    }

    override fun attachViewModelClass(): Class<LockitRegisterViewModel> {
        return LockitRegisterViewModel::class.java
    }

    private val mDialog by lazy {
        DialogUtil.getWaitDialog(this, "注册中")
    }

    override fun initView(savedInstanceState: Bundle?) {
        initToolbar(mBinding.extToolbar.toolbar, "注册")
        if(LockitSpKey.isDebug){
            mBinding.etUsername.editText!!.setText("791422171@qq.com")
            mBinding.etPassword.editText!!.setText("1234567")
            mBinding.etPassword2.editText!!.setText("1234567")
        }
    }

    private lateinit var username: String
    private lateinit var password: String
    private lateinit var password2: String
    private lateinit var code:String
    override fun initObserver() {
        super.initObserver()
        mViewModel.sendCodeState.observe(this){
            if(it){
                mBinding.layoutCaptcha.visibility = View.VISIBLE
                mBinding.tvCaptchaPhone.text = username
                timerCountDown(120)
                mBinding.tvSendCode.isEnabled=false
            }
        }
        mViewModel.registerState.observe(this){
            if(it.flag){
                showMsg("注册成功")
            }else{
                showMsg("注册失败!${it.message}")
            }
        }
    }
    override fun initListener() {
        super.initListener()
        mBinding.tvSendCode.setOnclickNoRepeatListener {
            if(validEmail()){
                mViewModel.sendCode(username)
            }
        }
        mBinding.btnRegister.setOnclickNoRepeatListener{
            if(validEmail()&&validPassword()){
                code=mBinding.captchaView.phoneCode
                mViewModel.register(username,password,code)
            }
        }
        mBinding.tvSignIn.setOnclickNoRepeatListener{
            finish()
        }
    }

    private fun validEmail(): Boolean {
        username=mBinding.etUsername.editText!!.text.toString()
        if(username.isEmpty()){
            mBinding.etUsername.error="邮箱账号不能为空！"
            return false
        }
        if(!PasswordUtils.checkEmail(username)){
            mBinding.etUsername.error="请输入正确的邮箱！"
            return false
        }
        return true
    }

    private fun validPassword(): Boolean {
        password=mBinding.etPassword.editText!!.text.toString()
        password2=mBinding.etPassword2.editText!!.text.toString()
        if(password!=password2){
            mBinding.etPassword2.error="请确保两次输入的密码相同！"
            return false
        }
        return true
    }

    private fun timerCountDown(millis:Long) {
        /**
         * CountDownTimer timer = new CountDownTimer(3000, 1000)中，
         * 第一个参数表示总时间，第二个参数表示间隔时间。
         * 意思就是每隔一秒会回调一次方法onTick，然后1秒之后会回调onFinish方法。
         */
        val timer: CountDownTimer = object : CountDownTimer(millis*1000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                mBinding.tvCaptchaTimer.text= "${millisUntilFinished / 1000} s"
            }

            override fun onFinish() {
                mBinding.tvSendCode.text="重新发送"
                mBinding.tvSendCode.isEnabled=true
                showMsg("时间到")
            }
        }
        //调用 CountDownTimer 对象的 start() 方法开始倒计时，也不涉及到线程处理
        timer.start()
    }


}