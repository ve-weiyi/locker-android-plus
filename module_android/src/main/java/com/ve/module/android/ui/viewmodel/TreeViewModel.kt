package com.ve.module.android.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import com.ve.module.android.repository.WazRepository
import com.ve.module.android.repository.model.Navigation
import com.ve.module.android.repository.model.Tree
import com.ve.lib.common.base.viewmodel.BaseViewModel


class TreeViewModel : BaseViewModel() {

    private val repository by lazy { WazRepository() }

    val treeList = MutableLiveData<MutableList<Tree>>()

    fun getTree() {
        launch(
            block = {
                treeList.value = repository.getTree()
            }
        )
    }


    val naviList = MutableLiveData<MutableList<Navigation>>()

    fun getNavi() {
        launch(
            block = {
                naviList.value = repository.getNavi()
            }
        )
    }

}