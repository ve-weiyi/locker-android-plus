package com.ve.module.locker.model.http.model

import java.io.Serializable

/**
 *
 *
 *
 *
 *
 * @author weiyi
 * @since 2022-04-11
 */

data class PrivacyTagVO(
    val id: Int? = null,


    val tagName: String? = null,


    val tagCover: String? = null,

    val privacyTagDesc: String? = null,
) : Serializable {


    companion object {
        const val serialVersionUID = 1L
    }
}