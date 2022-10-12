package com.ve.module.lockit.plus.ui.page.test

import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ve.lib.application.utils.LogUtil
import com.ve.lib.common.base.view.vm.BaseFragment
import com.ve.module.lockit.plus.databinding.FragmentHomeDeviceBinding
import com.ve.module.lockit.plus.ui.page.list.*


/**
 * @author waynie
 * @date 2022/9/30
 * @desc EufyHomeNew
 */
class HomeDeviceFragment : BaseFragment<FragmentHomeDeviceBinding>() {
    override fun attachViewBinding(): FragmentHomeDeviceBinding {
        return FragmentHomeDeviceBinding.inflate(layoutInflater)
    }

    private val mListAdapter = HomeDeviceAdapter()
    private val mRoomAdapter = HomeRoomAdapter()

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
            object :AppbarZoomBehavior.OnLayoutRecoveryListener{
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
            HomeRoomBean.swapType()
            mRoomAdapter.notifyDataSetChanged()
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
                deviceCode = HomeBeanType.Code.Robot,
                isSchedule = true,
                isUpdate = true,
            ),
            HomeDeviceBean(
                "Device",
                deviceCode = HomeBeanType.Code.Wetdry,
            ),
            HomeDeviceBean(
                "Device",
                deviceCode = HomeBeanType.Code.Bulb,
            ),
            HomeDeviceBean(
                "Device",
                deviceCode = HomeBeanType.Code.Plug,
            ),
            HomeDeviceBean(
                "Device",
                deviceCode = HomeBeanType.Code.Switch,
            ),
            HomeDeviceBean(
                "Device",
                deviceCode = HomeBeanType.Code.Genie,
            ),
        )
    }

    private fun getItemGroupRoom(): Collection<HomeRoomBean>? {
        return mutableListOf(
            HomeRoomBean(
                "Room"
            ),
            HomeRoomBean(
                "Room"
            ),
            HomeRoomBean(
                "Room"
            ),
            HomeRoomBean(
                "Room"
            ),
            HomeRoomBean(
                "Room"
            ),
            HomeRoomBean(
                "Room"
            ),
        )
    }
}