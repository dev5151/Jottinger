package com.dev5151.jottinger.di

import android.app.Application
import androidx.room.Room
import com.dev5151.jottinger.data.NoteDao
import com.dev5151.jottinger.repository.NoteRepository
import com.dev5151.jottinger.database.NotesDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule {


    // Method #1
    @Singleton
    @Provides
    fun providesAppDatabase(app: Application): NotesDatabase {
        return Room.databaseBuilder(app,NotesDatabase::class.java,"notes_db").build()
    }

    // Method #2
    @Singleton
    @Provides
    fun providesNoteDao(db: NotesDatabase): NoteDao {
        return db.getNoteDao()
    }

    // Method #3
    @Provides
    fun providesRepository(noteDao: NoteDao): NoteRepository {
        return NoteRepository(noteDao)
    }
}