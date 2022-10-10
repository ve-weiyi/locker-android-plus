package com.ve.module.lockit.plus.ui.page.test

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.appbar.AppBarLayout
import com.ve.lib.application.utils.LogUtil
import com.ve.lib.common.base.view.vm.BaseFragment
import com.ve.module.lockit.plus.R
import com.ve.module.lockit.plus.databinding.FragmentHomeDeviceBinding
import com.ve.module.lockit.plus.ui.page.list.HomeDeviceAdapter
import com.ve.module.lockit.plus.ui.page.list.HomeDeviceBean


/**
 * @author waynie
 * @date 2022/9/30
 * @desc EufyHomeNew
 */
class HomeDeviceFragment: BaseFragment<FragmentHomeDeviceBinding>() {
    override fun attachViewBinding(): FragmentHomeDeviceBinding {
        return FragmentHomeDeviceBinding.inflate(layoutInflater)
    }

    private val mListAdapter = HomeDeviceAdapter()

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

        mListAdapter.setOnItemClickListener { _, _, position ->
            val item = mListAdapter.data[position]

        }

        initRecyclerView()
    }


    private fun initRecyclerView(){


        mBinding.layoutDeviceList.recyclerView.apply {
            layoutManager = LinearLayoutManager(mContext)
//            addItemDecoration(dividerItemDecoration)
            adapter = mListAdapter
        }

        mBinding.layoutDeviceList.recyclerView2.apply {
//            layoutManager = LinearLayoutManager(mContext)
//            addItemDecoration(dividerItemDecoration)
            adapter = mListAdapter
        }
    }



    private fun getItemGroup(): Collection<HomeDeviceBean>? {
        return mutableListOf(
            HomeDeviceBean(
                "Device"
            ),
            HomeDeviceBean(
                "Device"
            ),
            HomeDeviceBean(
                "Device"
            ),
            HomeDeviceBean(
                "Device"
            ),
            HomeDeviceBean(
                "Device"
            ),
            HomeDeviceBean(
                "Device"
            ),
            HomeDeviceBean(
                "Device"
            ),
        )
    }
}