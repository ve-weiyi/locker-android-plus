package com.ve.module.sunny.ui.place

import android.content.Intent
import android.view.View
import android.widget.Toast
import androidx.core.view.GravityCompat
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import com.ve.lib.common.base.view.vm.BaseVmFragment
import com.ve.lib.common.utils.log.LogUtil
import com.ve.module.sunny.SunnyActivity
import com.ve.module.sunny.databinding.FragmentPlaceBinding
import com.ve.module.sunny.ui.weather.WeatherActivity
import com.ve.module.sunny.util.SunnyConstant


class PlaceFragment : BaseVmFragment<FragmentPlaceBinding, PlaceViewModel>() {

    companion object {
        //单例模式 双重校验锁式 （Double Check)
        val instance: PlaceFragment by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            PlaceFragment()
        }
        fun newInstance()=PlaceFragment()
    }

    private lateinit var adapter: PlaceAdapter

    override fun attachViewModelClass(): Class<PlaceViewModel> {
        return PlaceViewModel::class.java
    }

    override fun attachViewBinding(): FragmentPlaceBinding {
        return FragmentPlaceBinding.inflate(layoutInflater)
    }

    /**
     * step 1.初始化liveData.observe
     */
    override fun initObserver() {

        mViewModel.placeList.observe(viewLifecycleOwner) {
            if (it.isNotEmpty()) {
                mBinding.recyclerView.visibility = View.VISIBLE
                mBinding.bgImageView.visibility = View.GONE
                adapter.setList(it)
                LogUtil.d("----placeList.observe null 111")
            } else {
                Toast.makeText(activity, "未能查询到任何地点", Toast.LENGTH_SHORT).show()
                adapter.setList(null)
                LogUtil.d("----placeList.observe null  222")
            }
        }

        mBinding.searchPlaceEdit.addTextChangedListener { editable ->
            val content = editable.toString()
            if (content.isNotEmpty()) {
                mViewModel.getPlaceList(content)
                LogUtil.d("----mViewModel.searchPlaces(content)$content")
            } else {
                mBinding.recyclerView.visibility = View.GONE
                mBinding.bgImageView.visibility = View.VISIBLE
                adapter.notifyDataSetChanged()
            }
        }
    }


    /**
     * 初始化view相关
     */
    override fun initView() {
        initPlace()
    }

    private fun initPlace() {
        //运行在mainActivity，本地已经有缓存则直接打开weather
        if (activity is SunnyActivity && mViewModel.isPlaceSaved()) {
            val place = mViewModel.getSavedPlace()
            val intent = Intent(context, WeatherActivity::class.java).apply {
                putExtra(SunnyConstant.KEY_LOCATION_LNG, place.location.lng)
                putExtra(SunnyConstant.KEY_LOCATION_LAT, place.location.lat)
                putExtra(SunnyConstant.KEY_PLACE_NAME, place.name)
            }
            startActivity(intent)
            LogUtil.d("the place have saved "+place.name)
            activity?.finish()
            //return
        }
        /** layoutManager用于指定recyclerView的布局方式**/
        val layoutManager = LinearLayoutManager(activity)
        //初始化RecycleView
        mBinding.recyclerView.layoutManager = layoutManager
        //初始化数据
        //初始化适配器
        adapter = PlaceAdapter().apply {
            //开启加载动画
            animationEnable = true
            //item点击，在fragment中完成，也可以在adapter中完成
            setOnItemClickListener() { _, _, position ->
                val place = data[position]
                if (activity is WeatherActivity) {
                    //weatherActivity中点击：关闭drawer和软键盘，刷新weather
                    (activity as WeatherActivity).mBinding.drawerLayout.closeDrawer(GravityCompat.START)
                    (activity as WeatherActivity).mViewModel.locationLng = place.location.lng
                    (activity as WeatherActivity).mViewModel.locationLat = place.location.lat
                    (activity as WeatherActivity).mViewModel.placeName = place.name
                    (activity as WeatherActivity).refreshWeather()
                } else {
                    //SunnyActivity中点击：关闭drawer，打开weatherActivity
                    val intent = Intent(activity, WeatherActivity::class.java).apply {
                        putExtra(SunnyConstant.KEY_LOCATION_LNG, place.location.lng)
                        putExtra(SunnyConstant.KEY_LOCATION_LAT, place.location.lat)
                        putExtra(SunnyConstant.KEY_PLACE_NAME, place.name)
                    }
                    LogUtil.e("search place :" + data[position].name)
                    startActivity(intent)
                }
                mViewModel.savePlace(place)
            }
        }

        mBinding.recyclerView.adapter = adapter

    }


}