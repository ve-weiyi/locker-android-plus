package com.ve.module.lockit.respository.database.entity


import android.graphics.Color
import org.litepal.LitePal
import org.litepal.annotation.Column
import org.litepal.crud.LitePalSupport
import java.io.Serializable

/**
 *
 * @author weiyi
 * @since 2022-04-10
 */
data class PrivacyFolder(

    var id: Long=0,

    @Column(index = true, unique = true)
    val folderName: String,

    val folderCover: String=Color.BLUE.toString(),

    val folderDesc: String? = null,

    val folderEnable: Int=0,
) : LitePalSupport(),Serializable {

    companion object {
        private const val serialVersionUID = 1L
    }

//    override fun save(): Boolean {
//        val folder=LitePal.where("folderName=?", folderName).findFirst(PrivacyFolder::class.java)
//        if(folder!=null){
//            this.id=folder.id
//            return this.saveOrUpdate()
//        }
//        return super.save()
//    }

    override fun saveOrUpdate(vararg conditions: String?): Boolean {
        var res=super.saveOrUpdate(*conditions)
        if(id==0L){
            id=LitePal.where(*conditions).findFirst(this::class.java).id
        }
        return res
    }
}