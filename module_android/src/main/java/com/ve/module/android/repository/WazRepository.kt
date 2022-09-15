package com.ve.module.android.repository

import com.ve.module.android.repository.http.WazApiService
import com.ve.module.android.repository.database.HistoryDatabase
import com.ve.module.android.repository.database.dao.HistoryDao
import com.ve.module.android.repository.database.entity.SearchHistory
import com.ve.lib.application.BaseApplication
import com.ve.lib.common.base.repository.BaseRepository

import com.ve.lib.common.utils.system.LogUtil
import com.ve.lib.common.utils.data.TimeUtil


open class WazRepository : BaseRepository() {

    protected val apiService = WazApiService.apiService

    private val mHistoryDao: HistoryDao by lazy {
        HistoryDatabase.getInstance(BaseApplication.mContext).historyDao()
    }

    suspend fun getUserInfo() = apiService.getUserInfo()

    /*************  首页 ******************/
    //获取网络请求返回的数据
    suspend fun getBanner() = apiService.getBanners().data()

    suspend fun getArticleList(page: Int) = apiService.getArticles(page).data()

    suspend fun collect(id: Int) = apiService.addCollectArticle(id)

    suspend fun unCollectByArticle(id: Int) = apiService.cancelCollectArticle(id)

    suspend fun getTopArticleList() = apiService.getTopArticles().data()

    /*************  广场 ******************/
    suspend fun getSquareList(page: Int) = apiService.getSquareList(page).data()

    /*************  收藏 ******************/
    suspend fun getCollectList(page: Int) = apiService.getCollectList(page).data()
    suspend fun unCollectByCollect(id: Int, originId: Int) =
        apiService.removeCollectArticle(id, originId)

    /*************  积分 ******************/
    suspend fun getUserScore(page: Int) = apiService.getUserScoreList(page).data()

    /*************  分享 ******************/
    suspend fun getUserShare(page: Int) = apiService.getShareList(page).data()

    /*************  搜索 ******************/
    suspend fun getHotkey() = apiService.getHotkey().data()

    suspend fun getSearchList(page: Int, key: String) = apiService.queryBySearchKey(page, key)

    suspend fun getSearchHistory() = mHistoryDao.getAll()

    suspend fun cleanSearchHistory() = mHistoryDao.deleteAll()

    suspend fun saveSearchHistory(text: String) {
        val id = mHistoryDao.queryIdByName(text)
        if (null == id) {
            mHistoryDao.insert(SearchHistory(null, text, TimeUtil.dateAndTime))
        } else {
            mHistoryDao.update(SearchHistory(id, text, TimeUtil.dateAndTime))
        }
    }

    suspend fun deleteSearchHistory(text: String) {
        val id = mHistoryDao.queryIdByName(text)
        if (null == id) {
            mHistoryDao.delete(SearchHistory(id, text, TimeUtil.dateAndTime))
        } else {
            LogUtil.d("database 没有该history $text")
        }
    }

    /*************  微信 公众号 ******************/
    suspend fun getWXChapters() = apiService.getWXChapters().data()

    suspend fun getWXArticles(id: Int, page: Int) = apiService.getWXArticles(id, page).data()

    /*************  体系 ******************/
    suspend fun getTree() = apiService.getTree().data()

    suspend fun getTreeArticles(page: Int, id: Int) = apiService.getTreeArticles(page, id)

    /*************  导航 ******************/
    suspend fun getNavi() = apiService.getNavigationList().data()

    /*************  项目 ******************/
    suspend fun getProjectTree() = apiService.getProjectTree().data()

    suspend fun getProjectList(page: Int, cid: Int) = apiService.getProjectList(page, cid).data()

    /*************  积分排行榜 ******************/
    suspend fun getRankList(page: Int) = apiService.getRankList(page).data()

    /*************  登录 ******************/
    suspend fun login(username: String?, password: String?) =
        apiService.loginWanAndroid(username, password)

    suspend fun logout() = apiService.logout()

    /*************  注册 ******************/
    suspend fun register(username: String, password: String, repassword: String) =
        apiService.registerWanAndroid(username, password, repassword)

    /*************  TO do  ******************/
    suspend fun getTodoList(type: Int) = apiService.getTodoList(type)

    suspend fun getNoTodoList(page: Int, type: Int) = apiService.getNoTodoList(page, type)

    suspend fun getDoneList(page: Int, type: Int) = apiService.getDoneList(page, type)

    suspend fun deleteTodoById(id: Int) = apiService.deleteTodoById(id)

    suspend fun updateTodoById(id: Int, status: Int) = apiService.updateTodoById(id, status)

    suspend fun addTodo(map: MutableMap<String, Any>) = apiService.addTodo(map)

    suspend fun updateTodo(id: Int, map: MutableMap<String, Any>) = apiService.updateTodo(id, map)

    /*************  分享 ******************/
    suspend fun shareArticle(map: MutableMap<String, Any>) = apiService.shareArticle(map)

    suspend fun getShareList(page: Int) = apiService.getShareList(page)

    suspend fun deleteShareArticle(id: Int) = apiService.deleteShareArticle(id)

}