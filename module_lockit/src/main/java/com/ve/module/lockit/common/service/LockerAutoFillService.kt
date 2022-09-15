package com.ve.module.lockit.common.service

import android.app.assist.AssistStructure
import android.os.Build
import android.os.Bundle
import android.service.autofill.*
import android.text.InputType
import android.view.View
import android.view.autofill.AutofillId
import android.view.autofill.AutofillValue
import android.view.inputmethod.EditorInfo
import android.widget.RemoteViews
import com.ve.lib.common.utils.system.LogUtil
import com.ve.module.lockit.R
import com.ve.module.lockit.respository.database.entity.PrivacyPass
import com.ve.module.lockit.common.service.model.ParsedStructure
import com.ve.module.lockit.common.service.model.SimpleUserData
import com.ve.lib.common.utils.system.AndroidUtil
import org.json.JSONObject
import org.litepal.LitePal


//此填充服务以name作为主键，通过此字段来去重，也通过此字段来作为显示时的表单标题
//当应用没有name字段时，也可以设置一个隐藏的自动填充控件，来设置自己想要的主键和表单标题
//当用户保存表单时，如果确实没有name字段，服务将会根据当前时间来生成一个name主键
//此填充服务不支持同一个控件拥有多个字段，当同一控件拥有多个字段时，将以第一个字段为准
//此填充服务不支持多个控件使用同一个字段，当多个控件使用同一字段时，只有最后的控件内容会被保存
public class LockitAutoFillService : AutofillService() {
    companion object {
        const val AUTOFILL_ID_USERNAME = "usernameAutofillId"
        const val AUTOFILL_ID_PASSWORD = "passwordAutofillId"
        val HINT_TYPE_COLLECTIONS: MutableList<String?> = ArrayList()

        //模拟数据库中的数据，实际应用中，应该将这个数据保存在数据库中
        private var suggestions: MutableList<PrivacyPass> =
            LitePal.findAll(PrivacyPass::class.java)


        init {
            //模拟数据库中的数据，实际应用中，应当把表单保存到数据库中，填充时再从数据库读取
        }
    }

    override fun onFillRequest(
        request: FillRequest,
        cancellationSignal: android.os.CancellationSignal,
        callback: FillCallback
    ) {
        try {
            LogUtil.msg("1.onFillRequest")
            //获取所有自动填充节点的AutofillId和HintType
            val fillContexts = request.fillContexts
            val fillContext = fillContexts[fillContexts.size - 1]
            val structure = fillContext.structure

            LogUtil.msg("2.onFillRequest " + structure.toString())
            LogUtil.msg(suggestions.toString())

            val windowNodes: List<AssistStructure.WindowNode> = structure.run {
                (0 until windowNodeCount).map { getWindowNodeAt(it) }
            }
            val parsedStructure = ParsedStructure(null, null)
            windowNodes.forEach { windowNode: AssistStructure.WindowNode ->
                val viewNode: AssistStructure.ViewNode? = windowNode.rootViewNode
                findUsernamePasswordFillIdRecursive(viewNode, parsedStructure)
            }
            LogUtil.msg("parsedStructure=$parsedStructure")
            if (parsedStructure.usernameId == null && parsedStructure.passwordId != null) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q && parsedStructure.passwordId != fillContext.focusedId) {
                    parsedStructure.usernameId = fillContext.focusedId
                }
            }
            if (!parsedStructure.isValid()) {
                callback.onSuccess(null)
                return
            }

            /**
             * 需要填写的app id
             */
            val appId = structure.activityComponent.packageName
            val appName = AndroidUtil.getAppName(this, appId)
            LogUtil.msg("appId $appId appName $appName")
            //每条建议记录对应填充服务中的一个Dataset对象
            //每个Dataset代表了一套数据，包含name，password，phone等所有保存的字段
            //我们用Map来记录Dataset的数据，从而可以方便得将其存储到数据库或内存中
            val fillResponseBuilder = FillResponse.Builder()
            val clientState = Bundle()
            suggestions.forEach { it ->
                val name = it.appPackageName
                val username = it.account
                val password = it.password
                if (username.isNotEmpty() || password.isNotEmpty()) {
                    val autofillItem = Dataset.Builder()
                    val usernamePresentation = createRemoteView(name, name, username)
                    parsedStructure.usernameId?.let {
                        clientState.putParcelable(AUTOFILL_ID_USERNAME, it)
                        autofillItem.setValue(
                            it,
                            AutofillValue.forText(username),
                            usernamePresentation
                        )
                    }
                    parsedStructure.passwordId?.let {
                        clientState.putParcelable(AUTOFILL_ID_PASSWORD, it)
                        autofillItem.setValue(
                            it,
                            AutofillValue.forText(password),
                            usernamePresentation
                        )
                    }
                    fillResponseBuilder.addDataset(autofillItem.build())
                }
            }
            // 设置保存信息
            when {
                parsedStructure.usernameId != null && parsedStructure.passwordId != null -> {
                    fillResponseBuilder.setSaveInfo(
                        SaveInfo.Builder(
                            SaveInfo.SAVE_DATA_TYPE_USERNAME or SaveInfo.SAVE_DATA_TYPE_PASSWORD,
                            arrayOf(parsedStructure.usernameId, parsedStructure.passwordId)
                        ).build()
                    )
                }
                parsedStructure.usernameId != null -> {
                    fillResponseBuilder.setSaveInfo(
                        SaveInfo.Builder(
                            SaveInfo.SAVE_DATA_TYPE_USERNAME,
                            arrayOf(parsedStructure.usernameId)
                        ).build()
                    )
                }
                parsedStructure.passwordId != null -> {
                    fillResponseBuilder.setSaveInfo(
                        SaveInfo.Builder(
                            SaveInfo.SAVE_DATA_TYPE_PASSWORD,
                            arrayOf(parsedStructure.passwordId)
                        ).build()
                    )
                }
            }
            //成功
            callback.onSuccess(fillResponseBuilder.build())
        } catch (t: Throwable) {
            callback.onFailure(t.message)
        }
    }

    private fun createRemoteView(appId: String, name: String, username: String): RemoteViews {
        return RemoteViews(packageName, R.layout.lockit_item_autofill).apply {
            AndroidUtil.getAppIcon(applicationContext, appId)?.let { it ->
                setImageViewBitmap(R.id.iv_app_icon, it)
            }
            setTextViewText(R.id.tv_name, name.ifEmpty { "Unknown" })
            setTextViewText(R.id.tv_username, username)
        }
    }

    override fun onSaveRequest(request: SaveRequest, callback: SaveCallback) {
        val fillContexts = request.fillContexts
        val fillContext = fillContexts[fillContexts.size - 1]
        val structure = fillContext.structure

        val usernameAutofillId =
            request.clientState?.getParcelable<AutofillId>(AUTOFILL_ID_USERNAME)
        val passwordAutofillId =
            request.clientState?.getParcelable<AutofillId>(AUTOFILL_ID_PASSWORD)

        val windowNodes: List<AssistStructure.WindowNode> = structure.run {
            (0 until windowNodeCount).map { getWindowNodeAt(it) }
        }

        val userData = SimpleUserData(null, null)

        windowNodes.forEach { windowNode: AssistStructure.WindowNode ->
            val viewNode: AssistStructure.ViewNode? = windowNode.rootViewNode
            obtainUsernamePasswordRecursive(
                viewNode,
                userData,
                usernameAutofillId,
                passwordAutofillId
            )
        }

        val packageName = structure.activityComponent.packageName
        val name = AndroidUtil.getAppName(this, packageName) ?: packageName

        val jsonObj = JSONObject()
        jsonObj.put("name", name)
        jsonObj.put("username", userData.username)
        jsonObj.put("password", userData.password)
        jsonObj.put("appName", name)
        jsonObj.put("appId", packageName)

        callback.onSuccess()
    }

    private fun findUsernamePasswordFillIdRecursive(
        viewNode: AssistStructure.ViewNode?,
        parsedStructure: ParsedStructure
    ) {
        if (isUsernameNode(viewNode)) {
            viewNode?.autofillId?.let {
                parsedStructure.usernameId = it
            }
            return
        } else if (isPasswordNode(viewNode)) {
            viewNode?.autofillId?.let {
                parsedStructure.passwordId = it
            }
            return
        }

        val children: List<AssistStructure.ViewNode>? = viewNode?.run {
            (0 until childCount).map { getChildAt(it) }
        }

        children?.forEach { childNode: AssistStructure.ViewNode ->
            findUsernamePasswordFillIdRecursive(childNode, parsedStructure)
        }
    }

    private fun obtainUsernamePasswordRecursive(
        viewNode: AssistStructure.ViewNode?,
        userData: SimpleUserData,
        usernameAutofillId: AutofillId?,
        passwordAutofillId: AutofillId?
    ) {
        if (viewNode?.autofillId != null && viewNode.autofillId == usernameAutofillId) {
            viewNode.text?.let {
                userData.username = it.toString()
            }
            return
        } else if (viewNode?.autofillId != null && viewNode.autofillId == passwordAutofillId) {
            viewNode.text?.let {
                userData.password = it.toString()
            }
            return
        }

        val children: List<AssistStructure.ViewNode>? = viewNode?.run {
            (0 until childCount).map { getChildAt(it) }
        }

        children?.forEach { childNode: AssistStructure.ViewNode ->
            obtainUsernamePasswordRecursive(
                childNode,
                userData,
                usernameAutofillId,
                passwordAutofillId
            )
        }
    }

    private fun isUsernameNode(viewNode: AssistStructure.ViewNode?): Boolean {
        viewNode ?: return false
        val autofillHints = viewNode.autofillHints
        if (autofillHints != null) {
            return autofillHints.contains(View.AUTOFILL_HINT_USERNAME) || autofillHints.contains(
                View.AUTOFILL_HINT_EMAIL_ADDRESS
            )
                    || autofillHints.contains(View.AUTOFILL_HINT_PHONE)
        }
        val inputType = viewNode.inputType
        return inputType == InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
                || inputType == InputType.TYPE_CLASS_PHONE
    }

    private fun isPasswordNode(viewNode: AssistStructure.ViewNode?): Boolean {
        viewNode ?: return false
        val autofillHints = viewNode.autofillHints
        if (autofillHints != null) {
            return autofillHints.contains(View.AUTOFILL_HINT_PASSWORD)
        }
        val inputType = viewNode.inputType
        return isPasswordInputType(inputType) || isVisiblePasswordInputType(inputType)
    }

    private fun isPasswordInputType(inputType: Int): Boolean {
        val variation = inputType and (EditorInfo.TYPE_MASK_CLASS or EditorInfo.TYPE_MASK_VARIATION)
        return (variation
                == EditorInfo.TYPE_CLASS_TEXT or EditorInfo.TYPE_TEXT_VARIATION_PASSWORD) || (variation
                == EditorInfo.TYPE_CLASS_TEXT or EditorInfo.TYPE_TEXT_VARIATION_WEB_PASSWORD) || (variation
                == EditorInfo.TYPE_CLASS_NUMBER or EditorInfo.TYPE_NUMBER_VARIATION_PASSWORD)
    }

    private fun isVisiblePasswordInputType(inputType: Int): Boolean {
        val variation = inputType and (EditorInfo.TYPE_MASK_CLASS or EditorInfo.TYPE_MASK_VARIATION)
        return (variation
                == EditorInfo.TYPE_CLASS_TEXT or EditorInfo.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD)
    }
}