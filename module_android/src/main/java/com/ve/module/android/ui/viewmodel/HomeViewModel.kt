package com.ve.module.android.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ve.module.android.repository.WazRepository
import com.ve.module.android.repository.model.Article
import com.ve.module.android.repository.model.BannerBean
import com.ve.module.android.repository.model.Project
import com.ve.lib.common.base.viewmodel.BaseViewModel

class HomeViewModel : BaseViewModel() {

    private val repository by lazy { WazRepository() }

    private val _bannerList = MutableLiveData<MutableList<BannerBean>>()
    val bannerBeanList: LiveData<MutableList<BannerBean>> =_bannerList

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
}