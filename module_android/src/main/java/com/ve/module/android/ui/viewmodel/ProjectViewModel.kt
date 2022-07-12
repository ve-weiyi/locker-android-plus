package com.ve.module.android.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import com.ve.module.android.repository.WazRepository
import com.ve.module.android.repository.bean.Article
import com.ve.module.android.repository.bean.Project
import com.ve.lib.common.base.viewmodel.BaseViewModel


class ProjectViewModel : BaseViewModel() {

    private val repository by lazy { WazRepository() }

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