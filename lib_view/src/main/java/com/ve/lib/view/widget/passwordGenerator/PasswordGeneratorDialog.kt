package com.ve.lib.view.widget.passwordGenerator

import android.app.Dialog
import android.content.*
import android.os.Bundle
import android.view.View
import android.widget.*
import com.ve.lib.view.R

/**
 * @Author  weiyi
 * @Date 2022/4/21
 * @Description  current project lockit-android
 */
class PasswordGeneratorDialog : Dialog {
    companion object {
        const val REQUEST_CODE: Int = 404
        const val PASSWORD_GENERATED: String = "Password Generated"
    }

    private lateinit var passwordTextView: TextView
    private lateinit var seekBarLength: TextView
    private lateinit var upperCaseSwitch: Switch
    private lateinit var lowerCaseSwitch: Switch
    private lateinit var numberSwitch: Switch
    private lateinit var symbolSwitch: Switch
    private lateinit var seekBar: SeekBar
    private lateinit var generateCheck: TextView
    private lateinit var generatePasswordButton: TextView
    private lateinit var copyPassword: TextView
    private lateinit var finishActivityButton: TextView

    var checkListener: View.OnClickListener?=null

    private var length: Int = 0
    var password = ""

    constructor(context: Context) : super(context)
    constructor(context: Context, themeResId: Int) : super(context, themeResId)
    constructor(
        context: Context,
        cancelable: Boolean,
        cancelListener: DialogInterface.OnCancelListener?
    ) : super(context, cancelable, cancelListener) {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.password_generator)
        passwordTextView = findViewById(R.id.generatedPasswordTextView)
        seekBarLength = findViewById(R.id.lengthText)
        upperCaseSwitch = findViewById(R.id.capitalsSwitch)
        lowerCaseSwitch = findViewById(R.id.smallLetterSwitch)
        symbolSwitch = findViewById(R.id.symbolSwitch)
        numberSwitch = findViewById(R.id.numberSwitch)
        seekBar = findViewById(R.id.lengthSeekBar)
        generatePasswordButton = findViewById(R.id.generatePassword)
        copyPassword = findViewById(R.id.copyPassword)
        finishActivityButton = findViewById(R.id.closeActivity)
        generateCheck=findViewById(R.id.generateCheck)
        generateCheck.setOnClickListener(checkListener)

        generatePassword()

        //生成按钮
        generatePasswordButton.setOnClickListener {
            generatePassword()
        }
        //复制按钮
        copyPassword.setOnClickListener {
            var clipboardManager : ClipboardManager = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            if(clipboardManager.hasPrimaryClip()){
                var data : ClipData = ClipData.newPlainText("copied text",password)
                clipboardManager.setPrimaryClip(data)
            }
            Toast.makeText(context,"Copied to clipboard: $password", Toast.LENGTH_SHORT).show()
        }
        //关闭
        finishActivityButton.setOnClickListener {
            dismiss()
        }

        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                seekBarLength.text = "密码长度: $p1"
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {

            }

            override fun onStopTrackingTouch(p0: SeekBar?) {

            }

        })
    }

    private fun generatePassword(){
        if(!(upperCaseSwitch.isChecked || lowerCaseSwitch.isChecked || symbolSwitch.isChecked || numberSwitch.isChecked)){
            Toast.makeText(context,"Select at least one", Toast.LENGTH_SHORT).show()
        }
        else{
            password = ""
            length = seekBar.progress

            var list  = ArrayList<Int>()
            if(upperCaseSwitch.isChecked)
                list.add(0)
            if(lowerCaseSwitch.isChecked)
                list.add(1)
            if(numberSwitch.isChecked)
                list.add(2)
            if(symbolSwitch.isChecked)
                list.add(3)

            for(i in 1..length){
                var choice = list.random()
                when(choice){
                    0-> password += ('A'..'Z').random().toString()
                    1-> password += ('a'..'z').random().toString()
                    2-> password += ('0'..'9').random().toString()
                    3-> password += listOf('!','@','#','$','%','&','*','+','=','-','~').random().toString()
                }
            }
            passwordTextView.text = password
            generatePasswordButton.text = "生成"
        }
    }

    fun setPasswordCheckListener(listener: View.OnClickListener){
        checkListener=listener
    }
}