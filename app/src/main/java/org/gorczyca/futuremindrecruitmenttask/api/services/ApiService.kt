package org.gorczyca.futuremindrecruitmenttask.api.services

import org.gorczyca.futuremindrecruitmenttask.models.ListItemModel
import retrofit2.Call
import retrofit2.http.GET

/**
 * Created by: Radosław Gorczyca
 * 24.01.2021 18:28
 */
interface ApiService {
    @GET("recruitment-task")
    fun getItemsList(): Call<List<ListItemModel>>
}