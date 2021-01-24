package org.gorczyca.futuremindrecruitmenttask.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import org.gorczyca.futuremindrecruitmenttask.database.list_item.ListItem
import org.gorczyca.futuremindrecruitmenttask.database.list_item.ListItemDao
import org.gorczyca.futuremindrecruitmenttask.models.ListItemModel
import org.gorczyca.futuremindrecruitmenttask.utils.Constants

/**
 * Created by: Radosław Gorczyca
 * 28.12.2020 12:09
 */
@Database(
    entities = [
        ListItem::class
    ], version = Constants.DATABASE_VERSION
)
abstract class AppDb : RoomDatabase() {
    abstract fun listItemsDao(): ListItemDao

    companion object{
        private var DB_NAME = Constants.DATABASE_NAME
        private var INSTANCE: AppDb? = null

        fun getDatabase(context: Context): AppDb? {
            if (INSTANCE == null){
                synchronized(AppDb::class){
                    INSTANCE = Room.databaseBuilder(context.applicationContext, AppDb::class.java, DB_NAME)
                        .build()
                }
            }
            return INSTANCE
        }

        fun destroyDataBase(){
            INSTANCE = null
        }
    }
}