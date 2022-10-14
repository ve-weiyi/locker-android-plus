package com.ve.module.lockit.plus.ui.page.home.model

import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.ve.lib.application.utils.LogUtil
import com.ve.module.lockit.plus.R
import com.ve.module.lockit.plus.databinding.ItemHomeDeviceCardBinding

/**
 * @author waynie
 * @date 2022/10/10
 * @desc lockit-android
 */
class HomeDeviceAdapter : BaseMultiItemQuickAdapter<HomeDeviceBean, BaseViewHolder>() {


    init {
        addItemType(HomeAdapterType.Device.CARD, R.layout.item_home_device_card)
        addItemType(HomeAdapterType.Device.GRID, R.layout.item_home_device_grid)
        addItemType(HomeAdapterType.Device.LINEAR, R.layout.item_home_device_linear)
    }

    override fun convert(holder: BaseViewHolder, item: HomeDeviceBean) {
        LogUtil.msg(item)
        when (item.itemType) {
            HomeAdapterType.Device.CARD -> {
                val mBinding = ItemHomeDeviceCardBinding.bind(holder.itemView)
                mBinding.tvDeviceName.text = item.deviceCode

                mBinding.layoutExpend.visibility = View.GONE
                mBinding.layoutVacAction.visibility = View.GONE
                mBinding.layoutBulbLight.visibility = View.GONE

                mBinding.ivSchedule.visibility = if (item.isSchedule) View.VISIBLE else View.GONE
                mBinding.ivUpdate.visibility = if (item.isUpdate) View.VISIBLE else View.GONE

                when (item.deviceCode) {
                    HomeAdapterType.Code.Robot -> {
                        mBinding.layoutVacAction.visibility = View.VISIBLE
                        mBinding.ivStartSuction.setOnClickListener {

                            if (mBinding.layoutExpend.visibility == View.VISIBLE) {
                                mBinding.layoutExpend.visibility = View.GONE
                            } else {
                                mBinding.layoutExpend.visibility = View.VISIBLE
                            }

                        }
                    }
//                    HomeAdapterType.Code.Wetdry -> {
//                        mBinding.tvDeviceState.visibility=View.GONE
//                    }
                    HomeAdapterType.Code.Bulb -> {
                        mBinding.layoutBulbLight.visibility = View.VISIBLE
                        mBinding.ivSwitch.visibility = View.VISIBLE
                    }
                    HomeAdapterType.Code.Genie -> {
                        mBinding.tvDeviceState.visibility = View.GONE
                    }
                    else -> {
                        mBinding.ivSwitch.visibility = View.VISIBLE
                    }
                }


            }
        }

    }

    var count = 0

    fun swapLayoutType() {
        swapLayoutType(++count % 3)
    }


    fun swapLayoutType(type: Int) {
        when (type) {
            HomeAdapterType.Device.CARD -> {
                recyclerView.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            }
            HomeAdapterType.Device.GRID -> {
                recyclerView.layoutManager = GridLayoutManager(context, 2)
            }
            HomeAdapterType.Device.LINEAR -> {
                recyclerView.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)

            }
        }
        data.forEach { item ->
            item.itemType = type
        }
        notifyDataSetChanged()
    }

}