package com.ve.module.android.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import com.ve.module.android.repository.WazRepository
import com.ve.module.android.repository.database.entity.SearchHistory
import com.ve.module.android.repository.bean.ArticleResponseBody
import com.ve.module.android.repository.bean.Hotkey
import com.ve.lib.common.base.viewmodel.BaseViewModel
import com.ve.lib.common.utils.system.LogUtil
import kotlinx.coroutines.flow.collect


class SearchViewModel : BaseViewModel() {

    private val repository by lazy { WazRepository() }

    val hotkeyList = MutableLiveData<MutableList<Hotkey>>()
    /************   activity  *************/
    fun getHotkey() {
        launch(
            block = {
                hotkeyList.value = repository.getHotkey()
            }
        )
    }

    /************   数据库  *************/
    val historyList = MutableLiveData<List<SearchHistory>>()
    fun getHistorySearch() {
        launch(
            block = {
                repository.getSearchHistory().collect {
                    historyList.value=it
                }
            }
        )
    }

    fun cleanHistorySearch() {
        launchMain(
            block = {
                LogUtil.d("clean database")
                repository.cleanSearchHistory()
            }
        )
    }

    fun saveHistorySearch(key: String) {
        launchMain(
            block = {
                LogUtil.d("save database")
                repository.saveSearchHistory(key)
            }
        )
    }

    fun deleteHistorySearch(key: String) {
        launchMain(
            block = {
                LogUtil.d("save database")
                repository.deleteSearchHistory(key)
            }
        )
    }
    /************   fragment  *************/
    val searchReslut = MutableLiveData<ArticleResponseBody>()

    fun getSearchList(page: Int, key: String) {
        launch(
            block = {
                searchReslut.value = repository.getSearchList(page, key).data()
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