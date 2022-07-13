package com.ve.module.lockit.common.event

/**
 * @Author  weiyi
 * @Date 2022/4/13
 * @Description  current project lockit-android
 * 页面数据刷新时间，设置 lazeLoad=false
 * dataClassName 已经更新对象的class name
 * data 具体的更新对象
 */
class RefreshDataEvent (var dataClassName:String,val data:Any?=null){

}