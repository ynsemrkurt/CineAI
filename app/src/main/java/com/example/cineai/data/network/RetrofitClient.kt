package com.example.cineai.data.network

import com.example.cineai.BuildConfig
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitClient {

    private val client = OkHttpClient.Builder()
        .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
        .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
        .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
        .addInterceptor { chain ->
            val originalRequest = chain.request()
            val url = originalRequest.url
                .newBuilder()
                .addQueryParameter(API_KEY_PARAM, BuildConfig.TMDB_API_KEY)
                .build()

            val request = originalRequest.newBuilder()
                .url(url)
                .addHeader(ACCEPT_HEADER, APPLICATION_JSON)
                .build()

            chain.proceed(request)
        }
        .build()

    val api: TMDbService by lazy {
        Retrofit.Builder()
            .baseUrl(BuildConfig.TMDB_BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(TMDbService::class.java)
    }
}

private const val API_KEY_PARAM = "api_key"
private const val ACCEPT_HEADER = "Accept"
private const val APPLICATION_JSON = "application/json"
private const val CONNECT_TIMEOUT = 30L
private const val READ_TIMEOUT = 30L
private const val WRITE_TIMEOUT = 30L