package org.gorczyca.futuremindrecruitmenttask.repositories

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.gorczyca.futuremindrecruitmenttask.api.RetrofitInstance
import org.gorczyca.futuremindrecruitmenttask.database.AppDb
import org.gorczyca.futuremindrecruitmenttask.database.list_item.ListItem
import org.gorczyca.futuremindrecruitmenttask.models.ListItemModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Created by: Radosław Gorczyca
 * 24.01.2021 18:31
 */
class MainRepository(context: Context) {

    private val retrofitInstance = RetrofitInstance.getService()
    private val appDb = AppDb.getDatabase(context)
    private val listItemsDao = appDb!!.listItemsDao()

    fun getAllItems(): LiveData<List<ListItem>> {
        return listItemsDao.getAllItemsLiveData()
    }

    fun refreshItemsList() {
        val call = retrofitInstance.getItemsList()
        call.enqueue(object : Callback<List<ListItemModel>> {
            override fun onResponse(
                call: Call<List<ListItemModel>>,
                response: Response<List<ListItemModel>>
            ) {
                Log.d("EXAM_RESPONSE", response.message())
                if (response.isSuccessful && response.body() != null) {
                    insertListToDb(response.body())
                } else {
                    Log.d("RESPONSE", response.message() ?: "UNKNOWN ERROR")
                }
            }

            override fun onFailure(call: Call<List<ListItemModel>>, t: Throwable) {
                Log.d("RESPONSE", t.message ?: "UNKNOWN ERROR")
            }

        })
    }

    private fun insertListToDb(listModel: List<ListItemModel>?) {
        if(!listModel.isNullOrEmpty()){
            listModel.forEach {
                insertItemToDb(it)
            }

            deleteRemovedItems(listModel)
        }
    }

    private fun insertItemToDb(listItemModel: ListItemModel) {
        GlobalScope.launch {
            val formattedListItem = formatItem(listItemModel)
            val itemFromDb = listItemsDao.getItem(listItemModel.orderId)
            if(itemFromDb == null){
                listItemsDao.insert(formattedListItem)
            } else if(formattedListItem != itemFromDb){
                listItemsDao.update(formattedListItem)
            }
        }
    }

    private fun formatItem(listItemModel: ListItemModel): ListItem {

        val parts = listItemModel.description.split('\t')
        val description: String
        var url = ""
        if(parts.size == 2) {
            description = parts[0]
            url = parts[1]
        } else {
            description = listItemModel.description
        }

        return ListItem(
            listItemModel.orderId,
            description,
            url,
            listItemModel.imageUrl,
            listItemModel.modificationDate,
            listItemModel.title
        )
    }

    private fun deleteRemovedItems(listModel: List<ListItemModel>) {
        GlobalScope.launch {
            val dbItemList = listItemsDao.getAllItems()
            if(!dbItemList.isNullOrEmpty()) {
                val itemsToRemove = dbItemList.toMutableList()
                listModel.forEach loop@{ item ->
                    dbItemList.forEach { dbItem ->
                        if (item.orderId == dbItem.orderId) {
                            itemsToRemove.remove(dbItem)
                            return@loop
                        }
                    }
                }
                itemsToRemove.forEach {
                    listItemsDao.delete(it)
                }
            }
        }
    }

}