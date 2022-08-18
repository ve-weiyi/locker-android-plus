package com.ve.lib.common.utils.file

import android.graphics.Bitmap
import android.os.Environment
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream

object FileHelp {

    /**
     * 将bitmap保存为Img
     */
    fun saveImageToLocal(bitmap: Bitmap?): String? {
        var saveImagePath: String? = ""

        bitmap?.let {
            val fileName = System.currentTimeMillis().toString()
            val imageFileName = "$fileName.png"
            val storageDir = File(
                Environment.getExternalStorageDirectory()
                    .toString() + File.separator + Environment.DIRECTORY_PICTURES
            )
            var success = true
            if (!storageDir.exists()) {
                success = storageDir.mkdirs()
            }

            if (success) {
                val imageFile = File(storageDir, imageFileName)
                saveImagePath = imageFile.absolutePath
                try {
                    val fout: OutputStream = FileOutputStream(imageFile)
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, fout)
                    fout.close()
//                    ToastUtils.showShort(resources.getString(R.string.robo_edit_schedule_toast_save_success))
                } catch (e: java.lang.Exception) {
                    e.printStackTrace()
                    saveImagePath = ""
//                    ToastUtils.showShort(resources.getString(R.string.robo_edit_schedule_toast_save_success))
                }
            }
        }
        return saveImagePath
    }
}