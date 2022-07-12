package com.ve.module.android.repository.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.ve.module.android.repository.database.dao.HistoryDao
import com.ve.module.android.repository.database.entity.SearchHistory

/**
 * @Author  weiyi
 * @Date 2022/4/14
 * @Description  current project locker-android
 *
 * ！！！ 使用room数据库：
 * 不这么写apt不会工作生成_Impl类，导致运行时找不到此类。
 * 记得在使用room的module里，添加apply plugin: 'kotlin-kapt'  插件。
 * 和 kapt "androidx.room:room-compiler:2.2.3"
 *
 * entities 数组，对应此数据库中的所有表
 * version 数据库版本号
 */
@Database(entities = [SearchHistory::class], version = 1,exportSchema = true)
abstract class HistoryDatabase : RoomDatabase() {

    abstract fun historyDao(): HistoryDao

    companion object {
        private const val DATABASE_NAME = "history.db"
        private lateinit var mPersonDatabase: HistoryDatabase

        //注意：如果您的应用在单个进程中运行，在实例化 AppDatabase 对象时应遵循单例设计模式。
        //每个 RoomDatabase 实例的成本相当高，而您几乎不需要在单个进程中访问多个实例
        fun getInstance(context: Context): HistoryDatabase {
            if (!this::mPersonDatabase.isInitialized) {
                //创建的数据库的实例
                mPersonDatabase = Room.databaseBuilder(
                    context.applicationContext,
                    HistoryDatabase::class.java,
                    DATABASE_NAME
                ).build()
            }
            return mPersonDatabase
        }
    }

}