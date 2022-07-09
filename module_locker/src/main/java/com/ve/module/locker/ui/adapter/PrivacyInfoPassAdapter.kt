package com.ve.module.locker.ui.adapter

import android.os.Bundle
import android.view.View
import android.widget.CheckBox
import android.widget.TextView
import com.chad.library.adapter.base.BaseSectionQuickAdapter
import com.chad.library.adapter.base.module.DraggableModule
import com.chad.library.adapter.base.module.LoadMoreModule
import com.chad.library.adapter.base.module.UpFetchModule
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.ve.lib.common.ext.spanText
import com.ve.lib.common.vutils.LogUtil
import com.ve.lib.common.vutils.ToastUtil
import com.ve.module.locker.R
import com.ve.module.locker.model.db.entity.PrivacyPassInfo
import com.ve.module.locker.ui.page.container.LockerContainerActivity
import com.ve.module.locker.ui.page.privacy.details.LockerPassDetailsSeeFragment
import com.ve.module.locker.utils.AndroidUtil
import com.ve.module.locker.utils.StickUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * @Description hello word!
 * @Author  weiyi
 * @Date 2022/4/10
 */
class PrivacyInfoPassAdapter :
    BaseSectionQuickAdapter<PrivacyPassInfo, BaseViewHolder>(
        com.ve.lib.common.R.layout.item_sticky_header, R.layout.locker_item_privacy_pass
    ), LoadMoreModule, DraggableModule, UpFetchModule {

//    init {
//        addChildClickViewIds(R.id.check_button)
//    }

    var isCheckMode = false
    private var isAllCheck = false
    private var mSelectList = mutableListOf<PrivacyPassInfo>()
    private var keywords = ""

    override fun convert(holder: BaseViewHolder, item: PrivacyPassInfo) {
        if (keywords.isNotEmpty()) {
            LogUtil.msg("keywords= $keywords")
            holder.getView<TextView>(R.id.tv_privacy_info_name).apply {
                spanText(keywords, text.toString())
            }
            holder.getView<TextView>(R.id.tv_privacy_info_desc).apply {
                spanText(keywords, text.toString())
            }
        }

        /**
         * Dispatchers.Main：Android 主线程。用于调用 suspend 函数，UI 框架操作，及更新 LiveData 对象。
         * Dispatchers.IO：非主线程。用于磁盘操作（例如，Room 操作），及网络 I/O 请求（例如，调用服务器 API）。
         * Dispatchers.Default：非主线程，用于 CPU 密集型操作。例如，list 排序，及 JSON 解析。
         */
        CoroutineScope(Dispatchers.IO).launch {
            val app=AndroidUtil.getAppByPackageName(context,item.getPrivacyDetails().appPackageName)
            withContext(Dispatchers.Main) {
                holder.setImageDrawable(R.id.iv_app_icon,app?.icon)
            }
        }

        val account=item.getPrivacyDetails().account
        holder.setText(R.id.tv_privacy_info_account, account)
        holder.setText(R.id.tv_privacy_info_name, item.privacyName)
        holder.setText(R.id.tv_privacy_info_desc, item.privacyDesc)
        holder.setText(R.id.tv_privacy_info_create_time, item.createTime)
        holder.setText(R.id.tv_privacy_info_update_time, item.updateTime)


        val checkBox = holder.getView<CheckBox>(R.id.check_button)
        checkBox.setOnClickListener {
            checkBox.apply {
//                isChecked=!isChecked
                if (isChecked) {
                    //选择
                    if (!mSelectList.contains(item)) {
                        LogUtil.msg("add 11 +$item}")
                        mSelectList.add(item)
                    }
                } else {
                    //取消选择
                    if (mSelectList.contains(item)) {
                        LogUtil.msg("remove 1 +${item}")
                        mSelectList.remove(item)
                    }
                }
            }
        }

        if (isCheckMode) {
            checkBox.visibility = View.VISIBLE
            checkBox.isChecked = isAllCheck
        } else {
            checkBox.visibility = View.GONE
        }
    }

    fun setKeywords(key: String) {
        LogUtil.msg("keywords= $keywords")
        keywords = key
    }

    fun changeAllState() {
        isAllCheck = !isAllCheck
        if (isAllCheck) {
            data.forEach { pass ->
                if (!pass.isHeader && !mSelectList.contains(pass))
                    mSelectList.add(pass)
            }
        } else {
            mSelectList.removeAll(mSelectList)
        }
    }


    override fun convertHeader(helper: BaseViewHolder, item: PrivacyPassInfo) {
        LogUtil.msg("head " + item.headerName)
        helper.setText(com.ve.lib.common.R.id.tv_header, item.headerName)
        helper.setEnabled(com.ve.lib.common.R.id.tv_header, false)
    }

    fun getSelectData(): MutableList<PrivacyPassInfo> {
        return mSelectList
    }


    init {
        setOnItemLongClickListener { adapter, view, position ->
            ToastUtil.showCenter("已复制密码")
            StickUtils.copy(context, data[position].getPrivacyDetails().password)
            true
        }
    }

    override fun setOnItemClick(v: View, position: Int) {
        super.setOnItemChildClick(v, position)
        if (data[position].isHeader)
            return
        if (!isCheckMode) {
            val privacyInfo = data[position]
            val bundle = Bundle()
            bundle.putSerializable(LockerPassDetailsSeeFragment.PRIVACY_DATA_KEY, privacyInfo)
//            Intent(context, LockerLoginActivity::class.java).run {
//                context.startActivity(this)
//            }
//            Intent(context, LockerContainerActivity::class.java).run {
//                putExtra(LockerContainerActivity.FRAGMENT_TITLE_KEY, "查看密码：" + privacyInfo.privacyName)
//                putExtra(LockerContainerActivity.FRAGMENT_CLASS_NAME_KEY, LockerPassDetailsSeeFragment::class.java.name)
//                putExtra(LockerContainerActivity.FRAGMENT_ARGUMENTS_KEY,bundle)
//                //或者
//                context.startActivity(this)
//            }
            LockerContainerActivity.start(
                context,
                LockerPassDetailsSeeFragment::class.java.name,
                "查看密码：" + privacyInfo.privacyName,
                bundle
            )
        } else {
            v.findViewById<CheckBox>(R.id.check_button).apply {
                isChecked = !isChecked
                if (isChecked) {
                    //选择
                    if (!mSelectList.contains(data[position])) {
                        LogUtil.msg("add +${data[position]}")
                        mSelectList.add(data[position])
                    }
                } else {
                    //取消选择
                    if (mSelectList.contains(data[position])) {
                        LogUtil.msg("remove +${data[position]}")
                        mSelectList.remove(data[position])
                    }
                }
            }
        }
    }

}