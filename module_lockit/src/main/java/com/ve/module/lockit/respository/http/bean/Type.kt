package com.ve.module.lockit.respository.http.bean


import org.litepal.crud.LitePalSupport
import java.io.Serializable

/**
@Entity 定义数据表，可以通过@Entity(tableName = "table_name")定义表的名称，如果不定义默认为类名
@PrimaryKey 表示主键，autoGenerate = true表示数据库自动生成。
如果一个Entity使用的是复合主键，可以通过@Entity注解的primaryKeys 属性定义复合主键：@Entity(primaryKeys = {"**", "**"})
@ColumnInfo(name = “column_name”)定义数据表中的字段名，可以省略，默认名字为字段名
@Ignore 用于告诉Room需要忽略的字段或方法
@Embedded 注解用于嵌套对象，表生成的时候会把它标注对象中的字段生成column
建立索引：在@Entity注解的indices属性中添加索引字段。例如：indices = {@Index(value = {"column1", "column2"}, unique = true), ...}, unique = true可以确保表中不会出现{"column1", "column2"} 相同的数据。
————————————————
版权声明：本文为CSDN博主「Tommy-sw」的原创文章，遵循CC 4.0 BY-SA版权协议，转载请附上原文出处链接及本声明。
原文链接：https://blog.csdn.net/u013356780/article/details/117702373
 * @author weiyi
 * @since 2022-04-11
 */

data class Type(


    val id: Int? = null,

    //(value = "类型名称")

    val typeName: String? = null,

    //(value = "英文名称")

    val typeNameEn: String? = null,

    //(value = "图标")

    val typeCover: String? = null,

    //(value = "颜色")

    val typeColor: String? = null,

    //(value = "描述")

    val typeDesc: String? = null,

    var ownerId: Int? = null,
) : LitePalSupport(),Serializable {

    companion object {
         const val serialVersionUID = 1L
    }
}