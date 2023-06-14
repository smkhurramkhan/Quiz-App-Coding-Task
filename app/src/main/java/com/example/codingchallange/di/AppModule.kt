package com.example.codingchallange.di

import android.content.Context
import com.example.codingchallange.api.ApiService
import com.example.codingchallange.api.Constants
import com.example.codingchallange.roomdb.AppDatabase
import com.example.codingchallange.roomdb.dao.QuizDao
import com.example.codingchallange.ui.main.remote.QuizRemoteDataSource
import com.example.codingchallange.ui.main.repository.QuizRepository
import com.google.gson.Gson
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

    @Singleton
    @Provides
    fun provideRetrofit(gson: Gson): Retrofit = Retrofit.Builder()
        .baseUrl(Constants.BASEURL)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()

    @Provides
    fun provideGson(): Gson = GsonBuilder().create()

    @Provides
    fun provideCharacterService(retrofit: Retrofit): ApiService =
        retrofit.create(ApiService::class.java)

    @Singleton
    @Provides
    fun provideCharacterRemoteDataSource(characterService: ApiService) =
        QuizRemoteDataSource(characterService)

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext appContext: Context) =
        AppDatabase.getDatabase(appContext)

    @Singleton
    @Provides
    fun provideCharacterDao(db: AppDatabase) = db.quizDao()

    @Singleton
    @Provides
    fun provideScoreDao(db: AppDatabase) = db.scoreDao()

    @Singleton
    @Provides
    fun provideRepository(
        remoteDataSource: QuizRemoteDataSource,
        quizDao: QuizDao
    ) =
        QuizRepository(remoteDataSource, quizDao)
}