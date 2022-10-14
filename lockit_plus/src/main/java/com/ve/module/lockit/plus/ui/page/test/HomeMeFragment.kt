package com.ve.module.lockit.plus.ui.page.test

import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.ve.lib.common.base.view.vm.BaseVBFragment
import com.ve.module.lockit.plus.R


import com.ve.module.lockit.plus.databinding.FragmentHomeMeBinding
import com.ve.module.lockit.plus.ui.page.home.model.SimpleSettingAdapter
import com.ve.module.lockit.plus.ui.page.home.model.SimpleSettingBean
import com.ve.module.lockit.plus.ui.page.home.widget.SimpleDecoration

/**
 * @author waynie
 * @date 2022/9/30
 * @desc EufyHomeNew
 */
class HomeMeFragment: BaseVBFragment<FragmentHomeMeBinding>() {
    override fun attachViewBinding(): FragmentHomeMeBinding {
        return FragmentHomeMeBinding.inflate(layoutInflater)
    }

    private val mListAdapter= SimpleSettingAdapter()
    private val dividerItemDecoration by lazy {
        SimpleDecoration(
            mContext,
            SimpleDecoration.VERTICAL,
            16,
            com.ve.lib.application.R.drawable.simple_divider
        )
    }

    override fun initialize() {

        mListAdapter.setList(getItemGroup())

        mBinding.layoutMineMenu.recyclerView.apply {
            layoutManager = LinearLayoutManager(mContext)
            addItemDecoration(dividerItemDecoration)
            adapter = mListAdapter
        }

        mListAdapter.setOnItemClickListener { _, _, position ->
            val item = mListAdapter.data[position]

        }

    }

    private fun getItemGroup(): List<SimpleSettingBean>? {
        return mutableListOf(
            SimpleSettingBean(
                title = "Smart Integrations",
                actionKey = "0",
                startIcon = ContextCompat.getDrawable(
                    mContext,
                    R.drawable.ic_help_feedback
                ),
            ),
            SimpleSettingBean(
                title = "Widgets",
                actionKey = "1",
                startIcon = ContextCompat.getDrawable(
                    mContext,
                    R.drawable.ic_help_feedback
                ),
            ),
            SimpleSettingBean(
                title = "Widgets",
                startIcon = ContextCompat.getDrawable(
                    mContext,
                    R.drawable.ic_help_feedback
                ),
            ),
            SimpleSettingBean(
                title = "Widgets",
                startIcon = ContextCompat.getDrawable(
                    mContext,
                    R.drawable.ic_help_feedback
                ),
            ),
            SimpleSettingBean(
                title = "Widgets",
                startIcon = ContextCompat.getDrawable(
                    mContext,
                    R.drawable.ic_help_feedback
                ),
            ),
            SimpleSettingBean(
                title = "Widgets",
                startIcon = ContextCompat.getDrawable(
                    mContext,
                    R.drawable.ic_help_feedback
                ),
            ),
            SimpleSettingBean(
                title = "Widgets",
                startIcon = ContextCompat.getDrawable(
                    mContext,
                    R.drawable.ic_help_feedback
                ),
            ),
        )
    }
}