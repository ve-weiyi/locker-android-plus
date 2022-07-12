package com.ve.module.android.repository.bean

import com.chad.library.adapter.base.entity.SectionEntity
import com.squareup.moshi.Json
import java.io.Serializable


data class TodoResponseBody(
    @Json(name = "curPage") val curPage: Int,
    @Json(name = "datas") val datas: MutableList<TodoBean>,
    @Json(name = "offset") val offset: Int,
    @Json(name = "over") val over: Boolean,
    @Json(name = "pageCount") val pageCount: Int,
    @Json(name = "size") val size: Int,
    @Json(name = "total") val total: Int
)

// 所有TODO，包括待办和已完成
data class AllTodoResponseBody(
    val type: Int,
    val doneList: MutableList<TodoListBean>,
    val todoList: MutableList<TodoListBean>,
)

data class TodoListBean(
    val date: Long,
    val todoList: MutableList<TodoBean>,
)

// TODO实体类
data class TodoBean(
    @Json(name = "id") val id: Int,
    @Json(name = "completeDate") val completeDate: String,
    @Json(name = "completeDateStr") val completeDateStr: String,
    @Json(name = "content") val content: String,
    @Json(name = "date") val date: Long,
    @Json(name = "dateStr") val dateStr: String,
    @Json(name = "status") val status: Int,
    @Json(name = "title") val title: String,
    @Json(name = "type") val type: Int,
    @Json(name = "userId") val userId: Int,
    @Json(name = "priority") val priority: Int
) : Serializable

data class TodoDataBean(var headerName: String = "", var todoBean: TodoBean? = null) : SectionEntity {

    override val isHeader: Boolean
        get() = headerName.isNotEmpty()

}
// TODO工具 类型
data class TodoTypeBean(
    val type: Int,
    val name: String,
    var isSelected: Boolean
)

// 新增TODO的实体
data class AddTodoBean(
    @Json(name = "title") val title: String,
    @Json(name = "content") val content: String,
    @Json(name = "date") val date: String,
    @Json(name = "type") val type: Int
)

// 更新TODO的实体
data class UpdateTodoBean(
    @Json(name = "title") val title: String,
    @Json(name = "content") val content: String,
    @Json(name = "date") val date: String,
    @Json(name = "status") val status: Int,
    @Json(name = "type") val type: Int
)