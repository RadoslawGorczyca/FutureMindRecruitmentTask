package org.gorczyca.futuremindrecruitmenttask.api

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import org.gorczyca.futuremindrecruitmenttask.api.services.ApiService
import org.gorczyca.futuremindrecruitmenttask.utils.Constants
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Created by: Radosław Gorczyca
 * 24.01.2021 18:27
 */
object RetrofitInstance {

    private val gson = GsonBuilder()
        .setLenient()
        .create()


    fun getService(): ApiService {
        val retrofit = Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
        return retrofit.create(ApiService::class.java)
    }
}