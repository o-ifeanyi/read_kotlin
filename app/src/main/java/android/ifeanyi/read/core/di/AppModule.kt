package android.ifeanyi.read.core.di

import android.content.Context
import android.ifeanyi.read.app.data.LibraryRepository
import android.ifeanyi.read.app.data.source.LibraryDao
import android.ifeanyi.read.core.services.DatabaseService
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
    @Singleton
    @Provides
    fun provideLibraryDao(databaseService: DatabaseService) : LibraryDao = databaseService.library()

    @Singleton
    @Provides
    fun provideAppDatabase(@ApplicationContext context: Context): DatabaseService = Room.databaseBuilder(
        context,
        DatabaseService::class.java,
        name = "read_db"
    ).fallbackToDestructiveMigration().build()

    @Singleton
    @Provides
    fun provideLibraryRepository(libraryDao: LibraryDao) : LibraryRepository = LibraryRepository(libraryDao)
}