package com.example.cineai.data.network

import com.example.cineai.BuildConfig
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitClient {
    private const val BASE_URL = "https://api.themoviedb.org/3/"
    private const val API_KEY = BuildConfig.API_KEY

    private val client = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .addInterceptor { chain ->
            val originalRequest = chain.request()
            val url = originalRequest.url()
                .newBuilder()
                .addQueryParameter("api_key", API_KEY)
                .build()

            val request = originalRequest.newBuilder()
                .url(url)
                .addHeader("Accept", "application/json")
                .build()

            chain.proceed(request)
        }
        .build()

    val api: TMDbService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(TMDbService::class.java)
    }
}