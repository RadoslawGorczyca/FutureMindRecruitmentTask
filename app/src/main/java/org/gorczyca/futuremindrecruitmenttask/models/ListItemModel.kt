package org.gorczyca.futuremindrecruitmenttask.models


import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

data class ListItemModel(
        @PrimaryKey
        @SerializedName("orderId")
        val orderId: Int,
        @SerializedName("description")
        val description: String,
        @SerializedName("image_url")
        val imageUrl: String,
        @SerializedName("modificationDate")
        val modificationDate: String,
        @SerializedName("title")
        val title: String
)