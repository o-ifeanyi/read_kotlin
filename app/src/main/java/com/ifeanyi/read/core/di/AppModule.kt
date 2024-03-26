package com.ifeanyi.read.core.di

import android.content.Context
import com.ifeanyi.read.app.data.LibraryRepository
import com.ifeanyi.read.app.data.SettingsRepository
import com.ifeanyi.read.app.data.source.FileDao
import com.ifeanyi.read.app.data.source.FolderDao
import com.ifeanyi.read.app.data.source.WhatsNewDao
import com.ifeanyi.read.core.services.DatabaseService
import com.ifeanyi.read.core.services.PreferenceService
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object AppModule {
    @Provides
    @Singleton
    fun providePreferenceService(@ApplicationContext context: Context): PreferenceService {
        return PreferenceService(context = context)
    }

    @Singleton
    @Provides
    fun provideFileDao(databaseService: DatabaseService): FileDao = databaseService.file()

    @Singleton
    @Provides
    fun provideFolderDao(databaseService: DatabaseService): FolderDao = databaseService.folder()

    @Singleton
    @Provides
    fun provideWhatsNewDao(databaseService: DatabaseService): WhatsNewDao = databaseService.whatsNew()

    @Singleton
    @Provides
    fun provideAppDatabase(@ApplicationContext context: Context): DatabaseService =
        Room.databaseBuilder(
            context,
            DatabaseService::class.java,
            name = "read_db"
        ).fallbackToDestructiveMigration().build()

    @Singleton
    @Provides
    fun provideLibraryRepository(fileDao: FileDao, folderDao: FolderDao): LibraryRepository =
        LibraryRepository(fileDao, folderDao)

    @Singleton
    @Provides
    fun provideSettingsRepository(whatsNewDao: WhatsNewDao): SettingsRepository =
        SettingsRepository(whatsNewDao)
}