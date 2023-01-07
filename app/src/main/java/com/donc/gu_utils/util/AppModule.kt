package com.donc.gu_utils.util

import com.donc.gu_utils.data.remote.RecordApi
import com.donc.gu_utils.repository.cardsearch.DefaultCardRepository
import com.donc.gu_utils.util.Constants.BASE_PROTO_URL
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Singleton
    @Provides
    fun provideCardRepository(apiService: RecordApi) = DefaultCardRepository(apiService)

    @Singleton
    @Provides
    fun provideRecordApiService() : RecordApi{
        return Retrofit.Builder()
            .baseUrl(BASE_PROTO_URL)
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .build().create(RecordApi::class.java)
    }

//    @Singleton
//    @Provides
//    fun providePicsApiService() : PicsApi{
//        return Retrofit.Builder()
//            .baseUrl(BASE_PIC_URL)
//            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
//            .build().create(PicsApi::class.java)
//    }
}