package com. chan.marriagepraposals.webservice

import com.chan.marriagepraposals.util.Constants
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import java.util.concurrent.TimeUnit

/**
 * Created by Chan on 10/08/20.
 */


fun getApiService() : ApiService{
    return getRetrofit(okHttpClient(), Constants.BASE_URL, gsonConverterFactory(), rxJava2CallAdapterFactory())!!.create()
}
fun gsonConverterFactory(): GsonConverterFactory {
    val gsonBuilder = GsonBuilder()
    return GsonConverterFactory.create(gsonBuilder.create())
}

fun gson(): Gson? {
    return Gson()
}

fun rxJava2CallAdapterFactory(): RxJava2CallAdapterFactory {
    return RxJava2CallAdapterFactory.create()
}

fun okHttpClient(): OkHttpClient {
    val clientBuilder = OkHttpClient.Builder()
    clientBuilder.connectTimeout(45, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
    return clientBuilder.build()
}

fun getRetrofit(
    okHttpClient: OkHttpClient,
    baseUrl: String,
    gsonConverterFactory: GsonConverterFactory,
    factory: RxJava2CallAdapterFactory
): Retrofit? {
    return Retrofit.Builder()
        .client(okHttpClient)
        .baseUrl(baseUrl)
        .addConverterFactory(gsonConverterFactory)
        .addCallAdapterFactory(factory)
        .build()
}