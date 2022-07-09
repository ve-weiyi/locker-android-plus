package com.ve.module.locker.common.event

/**
 * @Author  weiyi
 * @Date 2022/4/13
 * @Description  current project locker-android
 * 页面数据刷新时间，设置 lazeLoad=false
 * dataClassName 已经更新对象的class name
 * data 具体的更新对象
 */
class RefreshDataEvent (var dataClassName:String,val data:Any?=null){

}