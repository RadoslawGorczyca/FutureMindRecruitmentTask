package org.gorczyca.futuremindrecruitmenttask.database.list_item

import androidx.lifecycle.LiveData
import androidx.room.*

/**
 * Created by: Radosław Gorczyca
 * 24.01.2021 18:19
 */
@Dao
interface ListItemDao {
    @Query("SELECT * FROM listitem ORDER BY orderId ASC")
    fun getAllItemsLiveData(): LiveData<List<ListItem>>

    @Query("SELECT * FROM listitem ORDER BY orderId ASC")
    fun getAllItems(): List<ListItem>

    @Query("SELECT * FROM listitem WHERE orderId=:id")
    fun getItemLiveData(id: Int): LiveData<ListItem>?

    @Query("SELECT * FROM listitem WHERE orderId=:id")
    fun getItem(id: Int): ListItem?

    @Query("DELETE FROM listitem WHERE orderId=:id")
    fun deleteById(id: Long)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun update(item: ListItem)

    @Insert
    fun insert(item: ListItem)

    @Delete
    fun delete(item: ListItem)

    @Delete
    fun deleteItems(itemsList: List<ListItem>)
}