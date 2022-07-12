package com.ve.module.android.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ve.module.android.repository.WazRepository
import com.ve.module.android.repository.model.*
import com.ve.lib.common.base.viewmodel.BaseViewModel

/**
 * @Description hello word!
 * @Author  weiyi
 * @Date 2022/3/20
 */
class WanAndroidViewModel : BaseViewModel(){
    private val repository by lazy { WazRepository() }


    val _logout=MutableLiveData<Int>()
    fun userLogout(){
        launch(
            block = {
                _logout.value=repository.logout().errorCode
            }
        )
    }
    /************* 首页 *****************/
    private val _bannerList = MutableLiveData<MutableList<BannerBean>>()

    val bannerBeanList: LiveData<MutableList<BannerBean>> =_bannerList
    fun getBanner() {
        //开线程
        launch(
            block = {
                _bannerList.value = repository.getBanner()
            }
        )
    }

    val articleList = MutableLiveData<MutableList<Article>>()
    fun getArticleList(page: Int) {
        launch(
            block = {
                articleList.value = repository.getArticleList(page).datas
            }
        )
    }

    fun getTopAndHomeArticles(){
        launch(
            block = {
                val topArticles=repository.getTopArticleList()
                val lowArticle=repository.getArticleList(0).datas

                articleList.value= topArticles.apply {
                    topArticles.forEach {
                        it.top="1"
                    }
                    addAll(lowArticle)
                }
            }
        )
    }
    /**************** 收藏  *****************/
    val collectState = MutableLiveData<Boolean>()
    fun collect(id: Int) {
        launch(
            block = {
                collectState.value = 0 == repository.collect(id).code()
            }
        )
    }

    val unCollectState = MutableLiveData<Boolean>()
    fun unCollect(id: Int) {
        launch(
            block = {
                unCollectState.value = 0 == repository.unCollectByArticle(id).code()
            }
        )
    }

    /**************** 广场 *****************/

    val squareArticles = MutableLiveData<MutableList<Article>>()

    fun getSquareList(page :Int){
        //开线程
        launch(
            block = {
                squareArticles.value = repository.getSquareList(page).datas
            }
        )
    }

    /**************** 项目 *****************/
    val proList = MutableLiveData<MutableList<Project>>()

    fun getProject() {
        launch(
            block = {
                proList.value = repository.getProjectTree()
            }
        )
    }

    val childList = MutableLiveData<MutableList<Article>>()

    fun getProjectChild(page: Int, cid: Int) {
        launch(
            block = {
                childList.value = repository.getProjectList(page, cid).datas
            }
        )
    }
    /**************** 公众号 *****************/
    val wxChapters = MutableLiveData<MutableList<WxChapters>>()

    fun getWxChapters() {
        launch(
            block = {
                wxChapters.value = repository.getWXChapters()
            }
        )
    }


    val wxArticles = MutableLiveData<MutableList<Article>>()

    fun getWXArticles(id:Int,page:Int) {
        launch(
            block = {
                wxArticles.value = repository.getWXArticles(id,page).datas
            }
        )
    }
    /**************** 项目 *****************/
    private val _userInfo = MutableLiveData<UserInfoBody>()
    val userInfo: LiveData<UserInfoBody> = _userInfo
    fun getUserInfo() {
        launch(
            block = {
                _userInfo.value = repository.getUserInfo().data()

            }
        )
    }

    /**************** 收藏列表 *****************/
    val collectArticles = MutableLiveData<MutableList<Collect>>()

    fun getCollectList(page :Int){
        //开线程
        launch(
            block = {
                collectArticles.value = repository.getCollectList(page).datas
            }
        )
    }

    val shareArticleState = MutableLiveData<Any>()
    /**************** 分享文章 *****************/
    fun shareArticle(articleTitle: String, articleLink: String) {
        launch(
            block = {
                val map = mutableMapOf<String, Any>()
                map["title"] = articleTitle
                map["link"] = articleLink
                shareArticleState.value=repository.shareArticle(map).data()
            }
        )
    }
}