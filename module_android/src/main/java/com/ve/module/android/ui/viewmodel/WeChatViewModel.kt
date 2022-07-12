package com.ve.module.android.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import com.ve.module.android.repository.WazRepository
import com.ve.module.android.repository.bean.Article
import com.ve.module.android.repository.bean.WxChapters
import com.ve.lib.common.base.viewmodel.BaseViewModel


class WeChatViewModel : BaseViewModel() {
    private val repository by lazy { WazRepository() }

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
}