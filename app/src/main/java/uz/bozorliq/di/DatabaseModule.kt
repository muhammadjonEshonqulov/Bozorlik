package uz.bozorliq.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import uz.bozorliq.data.database.MyDatabase
import uz.bozorliq.utils.Prefs
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context) = Room.databaseBuilder(context, MyDatabase::class.java, "DATABASE_NAME").build()

    @Singleton
    @Provides
    fun provideDatabaseDao(recipesDatabase: MyDatabase) = recipesDatabase.dao()

    @Singleton
    @Provides
    fun provideSharedPref(@ApplicationContext context: Context) = Prefs(context)
}