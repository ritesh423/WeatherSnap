package com.ritesh.weathersnap.di

import android.content.Context
import androidx.room.Room
import com.ritesh.weathersnap.data.local.ReportDao
import com.ritesh.weathersnap.data.local.WeatherSnapDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): WeatherSnapDatabase =
        Room.databaseBuilder(
            context,
            WeatherSnapDatabase::class.java,
            "weather_snap.db"
        ).build()

    @Provides
    fun provideReportDao(database: WeatherSnapDatabase): ReportDao = database.reportDao()
}
