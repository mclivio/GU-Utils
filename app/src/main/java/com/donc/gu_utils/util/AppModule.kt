package com.donc.gu_utils.util

import android.content.Context
import com.donc.gu_utils.data.remote.HistoryApi
import com.donc.gu_utils.data.remote.ProfileApi
import com.donc.gu_utils.data.remote.RecordApi
import com.donc.gu_utils.repository.cardsearch.CardRepository
import com.donc.gu_utils.repository.cardsearch.DefaultCardRepository
import com.donc.gu_utils.repository.history.DefaultHistoryRepository
import com.donc.gu_utils.repository.history.HistoryRepository
import com.donc.gu_utils.repository.profile.DefaultProfileRepository
import com.donc.gu_utils.repository.profile.ProfileRepository
import com.donc.gu_utils.util.Constants.BASE_PROTO_URL
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    /* Here it's defined how to bring instances of certain types for Hilt to do injections
Being installed in a singleton these dependencies will be available anywhere in the app. */
    @Singleton
    @Provides
    fun provideCardRepository(apiService: RecordApi) = DefaultCardRepository(apiService) as CardRepository

    @Singleton
    @Provides
    fun provideHistoryRepository(apiService: HistoryApi) = DefaultHistoryRepository(apiService) as HistoryRepository

    @Singleton
    @Provides
    fun provideProfileRepository(apiService: ProfileApi) = DefaultProfileRepository(apiService) as ProfileRepository

    @Singleton
    @Provides
    fun provideHistoryApiService() : HistoryApi{
        return Retrofit.Builder()
            .baseUrl(BASE_PROTO_URL)
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .build().create(HistoryApi::class.java)
    }

    @Singleton
    @Provides
    fun provideRecordApiService() : RecordApi{
        return Retrofit.Builder()
            .baseUrl(BASE_PROTO_URL)
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .build().create(RecordApi::class.java)
    }

    @Singleton
    @Provides
    fun provideProfileApiService() : ProfileApi {
        return Retrofit.Builder()
            .baseUrl(BASE_PROTO_URL)
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .build().create(ProfileApi::class.java)
    }

    @Singleton
    @Provides
    fun provideContext(
        @ApplicationContext context: Context,
    ): Context {
        return context
    }
}