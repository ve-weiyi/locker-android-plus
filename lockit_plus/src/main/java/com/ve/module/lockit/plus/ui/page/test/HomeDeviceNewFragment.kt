package com.ve.module.lockit.plus.ui.page.test

import androidx.recyclerview.widget.LinearLayoutManager
import com.ve.lib.common.base.view.vm.BaseVBFragment
import com.ve.module.lockit.plus.databinding.FragmentHomeDeviceNewBinding
import com.ve.module.lockit.plus.ui.page.model.HomeDeviceAdapter
import com.ve.module.lockit.plus.ui.page.model.HomeDeviceBean


/**
 * @author waynie
 * @date 2022/9/30
 * @desc EufyHomeNew
 */
class HomeDeviceNewFragment: BaseVBFragment<FragmentHomeDeviceNewBinding>() {
    override fun attachViewBinding(): FragmentHomeDeviceNewBinding {
        return FragmentHomeDeviceNewBinding.inflate(layoutInflater)
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

//        mBinding.btnStart.setOnClickListener {
//            mBinding.swipeRefreshLayout.isRefreshing=true
//        }
//        mBinding.btnEnd.setOnClickListener {
//            mBinding.swipeRefreshLayout.isRefreshing=false
//        }

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