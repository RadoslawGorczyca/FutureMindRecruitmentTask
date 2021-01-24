package org.gorczyca.futuremindrecruitmenttask.database.list_item


import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity
data class ListItem(
        @PrimaryKey
        val orderId: Int,
        val description: String,
        val url: String,
        val imageUrl: String,
        val modificationDate: String,
        val title: String
) :Parcelable