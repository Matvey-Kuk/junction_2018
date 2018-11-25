package com.gneo.fgurbanov.junctionhealth.di.modules

import com.gneo.fgurbanov.junctionhealth.BuildConfig
import com.gneo.fgurbanov.junctionhealth.data.ApiService
import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.ihsanbal.logging.Level
import com.ihsanbal.logging.LoggingInterceptor
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.internal.platform.Platform
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
//import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

/**
 * Created by fedorgurbanov on 30.01.2018.
 */

@Module
class NetworkModule {

    @Provides
    @Singleton
    fun provideMainApiService(retrofit: Retrofit): ApiService =
        retrofit.create(ApiService::class.java)


    @Provides
    @Singleton
    fun provideRetrofit(client: OkHttpClient, gson: Gson): Retrofit = Retrofit.Builder()
        .baseUrl("http://35.185.199.57/")
        .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
        .addConverterFactory(GsonConverterFactory.create(gson))
        .client(client)
        .build()


    @Provides
    @Singleton
    fun provideOkHttpClient(loggingInterceptor: LoggingInterceptor): OkHttpClient = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .addInterceptor(loggingInterceptor)
        .build()


    @Provides
    @Singleton
    fun provideGson(
        gsonBuilder: GsonBuilder
    ): Gson = gsonBuilder
        .create()


    @Provides
    @Singleton
    fun provideGsonBuilder(): GsonBuilder = GsonBuilder()
        .setLenient()
        .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
        .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)


    @Provides
    @Singleton
    fun provideLoggingInterceptor(): LoggingInterceptor {
        return LoggingInterceptor.Builder()
            .loggable(BuildConfig.DEBUG)
            .setLevel(Level.BASIC)
            .log(Platform.INFO)
            .tag("ServerLogging")
            .executor(Executors.newSingleThreadExecutor())
            .build()
    }

}
