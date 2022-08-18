package com.ve.lib.common.lifecycle

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Context.LOCATION_SERVICE
import android.content.Intent
import android.content.pm.PackageManager
import android.location.*
import android.provider.Settings
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.permissionx.guolindev.PermissionX
import com.ve.lib.application.BaseApplication.Companion.context
import com.ve.lib.common.utils.view.DialogUtil
import com.ve.lib.common.utils.log.LogUtil
import java.util.*


/**
 * @Description hello word!
 * 只需要获取一次数据，则 init(context).getLocation()
 * 持续监听  init(context).observer(listener)-->destroy
 * @Author weiyi
 * @Date 2022/4/7
 */
class LocationLifecycle private constructor():LifecycleObserver{

    /**
     * Android原生方式获取经纬度两种定位方式：GPS定位和Wifi定位
     *
     * GPS定位相比Wifi定位更精准且可在无网络情况下使用，但在室内基本暴毙无法使用。
     * WiFi定位没有室内外限制，也不需要开启GPS但需要联网。但测试发现WiFi定位时onLocationChanged函数（用于监听经纬度变化）触发间隔无法小于30s。
     *
     * 作者：木木玩Android
     * 链接：https://www.jianshu.com/p/fb89ab396bf8
     * 来源：简书
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     */
    companion object{
        val instance by lazy { LocationLifecycle() }


        fun isPermissionEnable(context: Context): Boolean {
            return (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED)
        }

        fun permissionCheck(activity: FragmentActivity) {
            //添加权限检查
            if (!isPermissionEnable(activity)) {
                LogUtil.e("TAG", "没有权限")
                openLocationPermission(activity)
            }

            /**
             * 打开位置服务
             */
            if (!isLocationProviderEnabled(activity)) {

                DialogUtil.getConfirmDialog(activity, "这项功能需要您打开GPS定位才能正常使用",
                    onOKClickListener = { dialog, which ->
                        openLocationServer(activity)

                    },
                    onCancelClickListener = { dialog, which ->

                    }
                ).show()
            }
        }

        /**
         * 获取权限（如果没有开启权限，会弹出对话框，询问是否开启权限）
         */
        private fun openLocationPermission(activity: FragmentActivity){
            //请求权限
            PermissionX.init(activity)
                .permissions(
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
                .onExplainRequestReason { scope, deniedList ->
                    val message = "PermissionX需要您同意以下权限才能正常使用"
                    scope.showRequestReasonDialog(deniedList, message, "Allow", "Deny")
                }
                .request { allGranted, grantedList, deniedList ->
                    if (allGranted) {
                        Toast.makeText(activity, "所有申请的权限都已通过", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(activity, "您拒绝了如下权限：$deniedList", Toast.LENGTH_SHORT).show()
                    }
                }
        }

        /**
         * 判断是否开启了GPS或网络定位开关
         */
        fun isLocationProviderEnabled(context: Context): Boolean {
            var result = false
            val locationManager =
                context.getSystemService(LOCATION_SERVICE) as LocationManager
                    ?: return false
            if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
            ) {
                result = true
            }
            return result
        }

        /**
         * 跳转到设置界面，引导用户开启定位服务
         */
        private fun openLocationServer(activity: FragmentActivity) {
            val i = Intent()
            i.action = Settings.ACTION_LOCATION_SOURCE_SETTINGS
            activity.startActivity(i)
        }
    }

    /**
     * (1).LocationManager可以用来获取当前的位置，追踪设备的移动路线，或设定敏感区域，在进入或离开敏感区域时设备会发出特定警报 。
     */
    private lateinit var mLocationManager: LocationManager

    /**
     * (2).LocationProviders则是提供定位功能的组件集合，集合中的每种组件以不同的技术提供设备的当前位置，区别在于定位的精度、速度和成本等方面 。
     */
    private lateinit var mLocationProvider: String


    private var mLocationListener: LocationListener = object : LocationListener {
        // Provider被enable时触发此函数，比如GPS被打开
        override fun onProviderEnabled(provider: String) {}

        // Provider被disable时触发此函数，比如GPS被关闭
        override fun onProviderDisabled(provider: String) {}

        //当坐标改变时触发此函数，如果Provider传进相同的坐标，它就不会被触发
        override fun onLocationChanged(location: Location) {
            LogUtil.e("TAG", "监视地理位置变化-经纬度：" + location.longitude + "   " + location.latitude)
        }
    }

    @SuppressLint("MissingPermission")
    fun setLocationListener(listener: LocationListener?=null){

        //1.获取位置管理器
        mLocationManager = context.getSystemService(LOCATION_SERVICE) as LocationManager ?: return
        //2.获取位置提供器，GPS或是NetWork
        mLocationProvider = getProvider(mLocationManager!!)
        //3.获取上次的位置，一般第一次运行，此值为null
        var location: Location?=mLocationManager!!.getLastKnownLocation(mLocationProvider!!)

        listener?.let {
            mLocationListener=it
        }
        mLocationManager!!.requestLocationUpdates(
            mLocationProvider!!,
            60*1000L,
            1f,
            mLocationListener
        )
    }


    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun onCreate(){
        setLocationListener()
        LogUtil.msg("onCreate")
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onResume(){
        LogUtil.msg("onResume")
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy() {
        mLocationManager.removeUpdates(mLocationListener)
    }

    /**
     * 2.获取位置提供器，GPS或是NetWork
     */
    private fun getProvider(locationManager: LocationManager): String {
        val providers = locationManager!!.getProviders(true)
        var locationProvider: String="gps"
        if (providers.contains(LocationManager.GPS_PROVIDER)) {
            //如果是GPS
            locationProvider = LocationManager.GPS_PROVIDER
            LogUtil.msg("TAG", "定位方式GPS")
        } else if (providers.contains(LocationManager.NETWORK_PROVIDER)) {
            //如果是Network
            locationProvider = LocationManager.NETWORK_PROVIDER
            LogUtil.msg("TAG", "定位方式Network")
        } else {
            Toast.makeText(context, "没有可用的位置提供器", Toast.LENGTH_SHORT).show()
        }
        return locationProvider
    }

    @SuppressLint("MissingPermission")
    fun getLastLocation(context: Context): Location? {
        //3.获取上次的位置，一般第一次运行，此值为null
        var location: Location?=mLocationManager.getLastKnownLocation(mLocationProvider)

        if (location != null) {
            LogUtil.e("TAG", "获取上次的位置-经纬度：" + location.longitude + "   " + location.latitude)
        }else{
            //监视地理位置变化，第二个和第三个参数分别为更新的最短时间minTime和最短距离minDistace
            mLocationManager.requestLocationUpdates(mLocationProvider, 3000, 1F, mLocationListener);
        }
        return location
    }

    //获取地址信息:城市、街道等信息
    fun getAddress(location: Location?): List<Address>? {
        var result: List<Address>? = null
        try {
            if (location != null) {
                val gc = Geocoder(context, Locale.getDefault())
                result = gc.getFromLocation(location.latitude, location.longitude, 1)
                LogUtil.e("TAG", "获取地址信息：$result")
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return result
    }


}