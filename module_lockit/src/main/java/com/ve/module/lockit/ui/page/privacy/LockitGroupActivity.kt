package com.ve.module.lockit.ui.page.privacy

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.ve.lib.common.base.view.vm.BaseVmActivity
import com.ve.lib.common.vutils.LogUtil
import com.ve.module.lockit.databinding.LockitActivityClassifyGroupBinding
import com.ve.module.lockit.respository.database.entity.PrivacyFolder
import com.ve.module.lockit.respository.database.entity.PrivacyTag
import com.ve.module.lockit.respository.http.bean.ConditionVO
import com.ve.module.lockit.ui.adapter.CategoryGroupAdapter
import com.ve.module.lockit.ui.viewmodel.LockitClassifyViewModel
import java.io.Serializable

/**
 * @Author  weiyi
 * @Date 2022/5/7
 * @Description  current project lockit-android
 */
class LockitGroupActivity:
    BaseVmActivity<LockitActivityClassifyGroupBinding, LockitClassifyViewModel>() {

    companion object {

        const val FRAGMENT_TITLE_KEY: String = "this.title"
        const val FRAGMENT_ARGUMENTS_KEY = "this.args"
        const val FRAGMENT_DATA_KEY: String = "this.data"

        /**
         * person.javaClass == person::class.java == Person::class.java
         * person.javaClass != Person::class
         * className 请使用Fragment::class.java.name，不要使用 Fragment.javaClass.name
         * ContainerActivity.start(this,title,Fragment::class.java.name, bundle)
         */
        fun start(context: Context,  title: String? = null, fragmentBundle: Bundle? = null) {
            Intent(context, LockitGroupActivity::class.java).run {
                putExtra(FRAGMENT_TITLE_KEY, title)
                putExtra(FRAGMENT_ARGUMENTS_KEY,fragmentBundle)
                context.startActivity(this)
            }
        }
    }
    
    override fun attachViewBinding(): LockitActivityClassifyGroupBinding {
        return LockitActivityClassifyGroupBinding.inflate(layoutInflater)
    }

    override fun attachViewModelClass(): Class<LockitClassifyViewModel> {
        return LockitClassifyViewModel::class.java
    }

    lateinit var fragmentTitle: String
    lateinit var fragmentArguments: Bundle
    var data: Serializable?=null
    private val mGroupAdapter by lazy { CategoryGroupAdapter() }

    override fun initView(savedInstanceState: Bundle?) {
        //从bundle中取出数据
        fragmentTitle  = intent.getStringExtra(FRAGMENT_TITLE_KEY) ?: ""
        fragmentArguments = intent.getBundleExtra(FRAGMENT_ARGUMENTS_KEY) ?: Bundle()
        data = fragmentArguments.getSerializable(FRAGMENT_DATA_KEY)

        LogUtil.msg(data.toString())

        initToolbar(mBinding.extToolbar.toolbar,fragmentTitle)
        mBinding.recyclerView.setAdapter(mGroupAdapter)

    }

    override fun initWebData() {
        super.initWebData()
        mViewModel.getGroupList(ConditionVO().apply {
            if(data is PrivacyFolder)
            {
                folderId= (data as PrivacyFolder).id
            }else if(data is PrivacyTag){
                tagId= (data as PrivacyTag).id
            }
        })
    }

    override fun initObserver() {
        super.initObserver()
        mViewModel.resultGroupList.observe(this){
            val data = it.filter { it.second.isNotEmpty() }.toMutableList()
            if(data.isEmpty()){
                mBinding.multipleStatusView.showEmpty()
            }else{
                mBinding.multipleStatusView.showContent()
                mGroupAdapter.setNewInstance(true,
                    data.map { it.first }.toMutableList(),
                    data.map { it.second}.toMutableList()
                )

                val groupCount=mGroupAdapter.groupCount
                for(i in 0 until  groupCount){
                    mBinding.recyclerView.expandGroup(i)
                }
            }
            LogUtil.msg(it.toString())
        }
    }


}