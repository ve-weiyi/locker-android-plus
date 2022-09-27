package com.ve.module.lockit.plus.widget


import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.TextView
import com.ve.lib.application.utils.LogUtil
import com.ve.lib.common.utils.ui.DisplayUtil
import com.ve.module.lockit.plus.R


/**
 * @author waynie
 * @date 2022/9/20
 * @desc lockit-android
 */
class PrivacyDialog(context: Context) : Dialog(context, R.style.CommonEufyDialogStyle) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        applyCompat()
    }

    private fun applyCompat() {
        window!!.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
    }

    class Builder(private val mContext: Context) {
        private var mGDPRDialog: PrivacyDialog? = null
        private var mLeftBtnOnClickListener: View.OnClickListener? = null
        private var mRightBtnOnClickListener: View.OnClickListener? = null
        fun setLeftBtnOnClickListener(onClickListener: View.OnClickListener?): Builder {
            mLeftBtnOnClickListener = onClickListener
            return this
        }

        fun setRightBtnOnClickListener(onClickListener: View.OnClickListener?): Builder {
            mRightBtnOnClickListener = onClickListener
            return this
        }

        fun builder(): PrivacyDialog {
            val view: View =
                LayoutInflater.from(mContext).inflate(R.layout.privacy_dialog, null)
            val dialog = PrivacyDialog(mContext)
            dialog.setContentView(view)
            dialog.setCanceledOnTouchOutside(false)
            dialog.setCancelable(false)

            val dialogWindow = dialog.window
            val lp = dialogWindow!!.attributes
            lp.width = (DisplayUtil.getScreenWidth()* 0.9).toInt()
            lp.height = (DisplayUtil.getScreenHeight()* 0.8).toInt()
            dialogWindow.attributes = lp

            val content = view.findViewById<View>(R.id.dialog_describe_tv) as TextView
            content.text = initSpannableString(content, mContext)
            view.findViewById<View>(R.id.dialog_btn_left).setOnClickListener {
                dialog.cancel()
                if (mLeftBtnOnClickListener != null) {
                    mLeftBtnOnClickListener!!.onClick(view)
                }
            }
            view.findViewById<View>(R.id.dialog_btn_right).setOnClickListener {
                dialog.cancel()
                if (mRightBtnOnClickListener != null) {
                    mRightBtnOnClickListener!!.onClick(view)
                }
            }
            return dialog
        }

        fun show(): PrivacyDialog? {
            if (mGDPRDialog == null) {
                mGDPRDialog = builder()
            }

            if (!mGDPRDialog!!.isShowing) {
                mGDPRDialog?.show()
            }
            return mGDPRDialog
        }
    }

    companion object {
        private val TAG = PrivacyDialog::class.java.simpleName
        fun initSpannableString(
            textView: TextView,
            context: Context,
        ): SpannableString {
            val src = textView.text.toString()
            try {
                val termsService = "Terms of Service"
                val privacyPolicy = "Privacy Policy"
                val spannableInfo = SpannableString(src)
                val termsServiceIndex = src.indexOf(termsService)
                val privacyPolicyIndex = src.indexOf(privacyPolicy)
                if (termsServiceIndex > 0) {
                    spannableInfo.setSpan(
                        ForegroundColorSpan(Color.CYAN),
                        termsServiceIndex,
                        termsServiceIndex + termsService.length,
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                }
                if (privacyPolicyIndex > 0) {
                    spannableInfo.setSpan(
                        ForegroundColorSpan(Color.CYAN),
                        privacyPolicyIndex,
                        privacyPolicyIndex + privacyPolicy.length,
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                }
                textView.text = spannableInfo
                textView.movementMethod = LinkMovementMethod.getInstance()
                return spannableInfo
            } catch (e: Exception) {
                LogUtil.msg(TAG, e.message)
            }
            return SpannableString(src)
        }

    }
}