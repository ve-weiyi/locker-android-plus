package com.ve.module.lockit.plus.ui.page.test

import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ve.lib.application.utils.LogUtil
import com.ve.lib.common.base.view.vm.BaseVBFragment
import com.ve.module.lockit.plus.databinding.FragmentHomeDeviceBinding
import com.ve.module.lockit.plus.ui.page.home.brhavior.AppbarZoomBehavior
import com.ve.module.lockit.plus.ui.page.home.model.*


/**
 * @author waynie
 * @date 2022/9/30
 * @desc EufyHomeNew
 */
class HomeDeviceFragment : BaseVBFragment<FragmentHomeDeviceBinding>() {
    override fun attachViewBinding(): FragmentHomeDeviceBinding {
        return FragmentHomeDeviceBinding.inflate(layoutInflater)
    }

    private val mListAdapter = HomeDeviceAdapter()
    private val mRoomAdapter = HomeBulbAdapter()

    override fun initialize() {

        mListAdapter.setList(getItemGroup())
        mRoomAdapter.setList(getItemGroupRoom())

        mListAdapter.setOnItemClickListener { _, _, position ->
            val item = mListAdapter.data[position]

        }

        initRecyclerView()
        mBinding.appbarRefresh.apply {
            setProgressViewOffset(true, 50, 220)
            setColorSchemeResources(
                android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light
            )
        }

        val behavior=((mBinding.appbar.layoutParams) as CoordinatorLayout.LayoutParams ).behavior as AppbarZoomBehavior
        behavior.setRecoveryListener(
            object : AppbarZoomBehavior.OnLayoutRecoveryListener{
                override fun onRecovery() {
                    mBinding.progressBar.apply {
                        visibility= View.VISIBLE
                        postDelayed({
                            visibility=View.GONE
                            LogUtil.msg("refresh page at : " + javaClass.simpleName)
                        }, 1500)
                    }
//                    mBinding.appbarRefresh.apply {
//                        isRefreshing=true
//                        postDelayed({
//                            isRefreshing=false
//                            LogUtil.msg("refresh page at : " + javaClass.simpleName)
//                        }, 1500)
//                    }
                }
            }
        )
    }


    private fun initRecyclerView() {


        mBinding.layoutDeviceList.recyclerView.apply {
            layoutManager = LinearLayoutManager(mContext,RecyclerView.HORIZONTAL,false)
//            addItemDecoration(dividerItemDecoration)
            adapter = mRoomAdapter
        }

        mBinding.layoutDeviceList.ivGroupSwap.setOnClickListener {
            mRoomAdapter.swapLayoutType()
        }

        mBinding.layoutDeviceList.recyclerView2.apply {
            layoutManager = LinearLayoutManager(mContext)
//            addItemDecoration(dividerItemDecoration)
            adapter = mListAdapter
        }


        mBinding.layoutDeviceList.ivDeviceSwap.setOnClickListener {
            mListAdapter.swapLayoutType()
        }
    }


    private fun getItemGroup(): Collection<HomeDeviceBean>? {
        return mutableListOf(
            HomeDeviceBean(
                name = "Device",
                deviceCode = HomeAdapterType.Code.Robot,
                isSchedule = true,
                isUpdate = true,
            ),
            HomeDeviceBean(
                "Device",
                deviceCode = HomeAdapterType.Code.Wetdry,
            ),
            HomeDeviceBean(
                "Device",
                deviceCode = HomeAdapterType.Code.Bulb,
            ),
            HomeDeviceBean(
                "Device",
                deviceCode = HomeAdapterType.Code.Plug,
            ),
            HomeDeviceBean(
                "Device",
                deviceCode = HomeAdapterType.Code.Switch,
            ),
            HomeDeviceBean(
                "Device",
                deviceCode = HomeAdapterType.Code.Genie,
            ),
        )
    }

    private fun getItemGroupRoom(): Collection<HomeBulbBean>? {
        return mutableListOf(
            HomeBulbBean(
                "Room"
            ),
            HomeBulbBean(
                "Room"
            ),
            HomeBulbBean(
                "Room"
            ),
            HomeBulbBean(
                "Room"
            ),
            HomeBulbBean(
                "Room"
            ),
            HomeBulbBean(
                "Room"
            ),
        )
    }
}