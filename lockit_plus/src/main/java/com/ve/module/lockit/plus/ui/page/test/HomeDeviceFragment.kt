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


/**
 * @author waynie
 * @date 2022/9/30
 * @desc EufyHomeNew
 */
class HomeDeviceFragment: BaseFragment<FragmentHomeDeviceBinding>() {
    override fun attachViewBinding(): FragmentHomeDeviceBinding {
        return FragmentHomeDeviceBinding.inflate(layoutInflater)
    }

    private val mListAdapter = SimpleSettingAdapter()
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
        initAppBarLayout()
    }


    private fun initRecyclerView(){


        mBinding.layoutDeviceList.recyclerView.apply {
            layoutManager = LinearLayoutManager(mContext)
            addItemDecoration(dividerItemDecoration)
            adapter = mListAdapter
        }

        mBinding.layoutDeviceList.recyclerView2.apply {
            layoutManager = LinearLayoutManager(mContext)
            addItemDecoration(dividerItemDecoration)
            adapter = mListAdapter
        }
    }



    var imageOriginHeight=0

    private fun initAppBarLayout() {
        val appBarLayout: AppBarLayout = mBinding.appbar
        val imageView: ImageView = mBinding.image
        // 把锚点放到左上角
        imageView.setPivotX(0F)
        imageView.setPivotY(0F)
        //显示的调用invalidate
        imageView.invalidate()
        imageView.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                LogUtil.d( "onClick: ")
            }
        })
        appBarLayout.addOnOffsetChangedListener(object : AppBarLayout.OnOffsetChangedListener {
            override fun onOffsetChanged(appBarLayout: AppBarLayout?, verticalOffset: Int) {
                // 第一次滑动的时候记录图片的原始高度
                if (imageOriginHeight === 0) {
                    imageOriginHeight = imageView.getMeasuredHeight()
                }
                // 根据滑动的距离缩放图片
                val newHeight: Float = imageOriginHeight.toFloat() + verticalOffset
                val scale: Float = newHeight / imageOriginHeight
                ViewCompat.setScaleY(imageView, scale)
                ViewCompat.setScaleX(imageView, scale)
            }
        })
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