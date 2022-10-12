package com.ve.module.lockit.plus.ui.page.list

import android.graphics.drawable.Drawable
import com.chad.library.adapter.base.entity.MultiItemEntity

/**
 * @author waynie
 * @date 2022/10/9
 * @desc lockit-android
 */
data class HomeRoomBean(
    var name: String,
    var isOnline: Boolean = true,
    var RoomImage: Drawable? = null,
) : MultiItemEntity {

    override val itemType: Int
        get() {
            return sType
        }

    companion object{
        const val VERTICAL=0
        const val HORIZONTAL=1

        private var sType= VERTICAL

        fun swapType(){
            if(sType== VERTICAL)
                sType= HORIZONTAL
            else
                sType= VERTICAL
        }
    }

    private fun getItemGroupRoom(): Collection<HomeRoomBean>? {
        return mutableListOf(
            HomeRoomBean(
                "Room"
            ),
            HomeRoomBean(
                "Room"
            ),
            HomeRoomBean(
                "Room"
            ),
            HomeRoomBean(
                "Room"
            ),
            HomeRoomBean(
                "Room"
            ),
            HomeRoomBean(
                "Room"
            ),
            HomeRoomBean(
                "Room"
            ),
        )
    }
}