package com.ve.module.android.ui.page.fragment

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import com.ve.module.android.R
import com.ve.module.android.databinding.FragmentShareArticleBinding
import com.ve.module.android.ui.viewmodel.WanAndroidViewModel
import com.ve.lib.common.base.view.vm.BaseVmFragment
import com.ve.lib.common.event.RefreshShareEvent
import com.ve.lib.common.vutils.DialogUtil
import org.greenrobot.eventbus.EventBus

/**
 * @Description hello word!
 * @Author  weiyi
 * @Date 2022/3/22
 */
class ShareArticleFragment: BaseVmFragment<FragmentShareArticleBinding, WanAndroidViewModel>() {
    override fun attachViewBinding(): FragmentShareArticleBinding {
        return FragmentShareArticleBinding.inflate(layoutInflater)
    }

    override fun attachViewModelClass(): Class<WanAndroidViewModel> {
        return WanAndroidViewModel::class.java
    }

    override fun initView(savedInstanceState: Bundle?) {
        // 在fragment中使用 onCreateOptionsMenu 时需要在 onCrateView 中添加此方法，否则不会调用
        setHasOptionsMenu(true)
    }

    private val mDialog by lazy {
        DialogUtil.getWaitDialog(requireActivity(), getString(R.string.submit_ing))
    }

    private lateinit var articleTitle: String
    private lateinit var articleLink: String

    override fun initObserver() {
        super.initObserver()
        mViewModel.shareArticleState.observe(this){
            showShareArticle(true)
        }
    }

    private fun showShareArticle(success: Boolean) {
        if (success) {
            showMsg(getString(R.string.share_success))
            EventBus.getDefault().post(RefreshShareEvent(true))
            activity?.finish()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_share_article, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_share_article -> {
                articleTitle=mBinding.etArticleTitle.text.toString().trim()
                articleLink=mBinding.etArticleLink.text.toString().trim()
                if (articleTitle.isEmpty()) {
                    showMsg("文章标题不能为空")
                    return true
                }
                if (articleLink.isEmpty()) {
                    showMsg("文章链接不能为空")
                    return true
                }
                mViewModel.shareArticle(articleTitle,articleLink)
            }
        }
        return super.onOptionsItemSelected(item)
    }


    override fun showLoading() {
        mDialog.setCancelable(false)
        mDialog.setCanceledOnTouchOutside(false)
        mDialog.show()
    }

    override fun hideLoading() {
        mDialog.dismiss()
    }
}