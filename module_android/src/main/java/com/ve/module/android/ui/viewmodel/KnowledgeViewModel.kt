package com.ve.module.android.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import com.ve.module.android.repository.WazRepository
import com.ve.module.android.repository.bean.Article
import com.ve.lib.common.base.viewmodel.BaseViewModel

class KnowledgeViewModel: BaseViewModel() {


    private val repository by lazy { WazRepository() }

    val articleList = MutableLiveData<MutableList<Article>>()

    fun getTreeChild(page: Int, cid: Int) {
        launch(
            block = {
                articleList.value = repository.getTreeArticles(page, cid).data().datas
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