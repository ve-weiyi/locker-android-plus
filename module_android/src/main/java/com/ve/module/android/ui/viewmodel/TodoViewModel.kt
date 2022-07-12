package com.ve.module.android.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import com.ve.module.android.repository.WazRepository
import com.ve.module.android.repository.model.AllTodoResponseBody
import com.ve.module.android.repository.model.TodoResponseBody
import com.ve.lib.common.base.viewmodel.BaseViewModel

/**
 * service by TodoActivity
 */
class TodoViewModel: BaseViewModel() {
    private val repository by lazy { WazRepository() }

    val TodoList = MutableLiveData<AllTodoResponseBody>()

    fun getTodoList(type: Int) {
        //开线程
        launch(
            block = {
                TodoList.value = repository.getTodoList(type).data()
            }
        )
    }

    val noTodoList = MutableLiveData<TodoResponseBody>()
    fun getNoTodoList(page: Int, type: Int) {
        launch(
            block = {
                noTodoList.value=repository.getNoTodoList(page, type).data()
            }
        )
    }

    val doneTodoList = MutableLiveData<TodoResponseBody>()
    fun getDoneList(page: Int, type: Int){
        launch(
            block = {
                doneTodoList.value=repository.getDoneList(page, type).data()
            }
        )
    }

    val deleteTodoCode = MutableLiveData<Any>()
    fun deleteTodoById(id: Int){
        launch(
            block = {
               deleteTodoCode.value=repository.deleteTodoById(id).errorCode
            }
        )
    }

    val updateTodoByIdCode = MutableLiveData<Any>()
    fun updateTodoById(id: Int, status: Int) {
        launch(
            block = {
                updateTodoByIdCode.value=repository.updateTodoById(id, status).errorCode
            }
        )
    }

    val addTodoCode = MutableLiveData<Any>()
     fun addTodo(map: MutableMap<String, Any>){
         launch(
             block = {
                 addTodoCode.value=repository.addTodo(map)
             }
         )
     }
    val updateTodoCode = MutableLiveData<Any>()
     fun updateTodo(id: Int, map: MutableMap<String, Any>) {
         launch(
             block = {
                 updateTodoCode.value=repository.updateTodo(id,map)
             }
         )
    }
}