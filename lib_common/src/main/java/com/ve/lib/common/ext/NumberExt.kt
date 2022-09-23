package com.ve.lib.common.ext

/**
 * @author waynie
 * @date 2022/9/23
 * @desc EufyHomeNew
 */

fun Float.keep2Decimal():String{
    return "%.2f".format(this)
}