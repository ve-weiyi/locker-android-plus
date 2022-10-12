package com.ve.module.lockit.plus.ui.page.list

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
        addItemType(HomeBeanType.CARD, R.layout.item_home_device_card)
        addItemType(HomeBeanType.GRID, R.layout.item_home_device_grid)
        addItemType(HomeBeanType.LINEAR, R.layout.item_home_device_linear)
    }

    override fun convert(holder: BaseViewHolder, item: HomeDeviceBean) {
        LogUtil.msg(item)
        when (item.itemType) {
            HomeBeanType.CARD -> {
                val mBinding = ItemHomeDeviceCardBinding.bind(holder.itemView)
                mBinding.tvDeviceName.text = item.deviceCode

                mBinding.layoutExpend.visibility = View.GONE
                mBinding.layoutVacAction.visibility = View.GONE
                mBinding.layoutBulbLight.visibility = View.GONE

                mBinding.ivSchedule.visibility = if (item.isSchedule) View.VISIBLE else View.GONE
                mBinding.ivUpdate.visibility = if (item.isUpdate) View.VISIBLE else View.GONE

                when (item.deviceCode) {
                    HomeBeanType.Code.Robot -> {
                        mBinding.layoutVacAction.visibility = View.VISIBLE
                        mBinding.ivStartSuction.setOnClickListener {

                            if (mBinding.layoutExpend.visibility == View.VISIBLE) {
                                mBinding.layoutExpend.visibility = View.GONE
                            } else {
                                mBinding.layoutExpend.visibility = View.VISIBLE
                            }

                        }
                    }
//                    HomeBeanType.Code.Wetdry -> {
//                        mBinding.tvDeviceState.visibility=View.GONE
//                    }
                    HomeBeanType.Code.Bulb -> {
                        mBinding.layoutBulbLight.visibility = View.VISIBLE
                        mBinding.ivSwitch.visibility = View.VISIBLE
                    }
                    HomeBeanType.Code.Genie -> {
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
            HomeBeanType.CARD -> {
                recyclerView.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            }
            HomeBeanType.GRID -> {
                recyclerView.layoutManager = GridLayoutManager(context, 2)
            }
            HomeBeanType.LINEAR -> {
                recyclerView.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)

            }
        }
        data.forEach { item ->
            item.itemType = type
        }
        notifyDataSetChanged()
    }

}