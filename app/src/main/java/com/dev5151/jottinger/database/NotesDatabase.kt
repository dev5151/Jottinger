package com.dev5151.jottinger.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.dev5151.jottinger.data.Note
import com.dev5151.jottinger.data.NoteDao

@Database(entities = [Note::class], version = 1, exportSchema = false)
abstract class NotesDatabase : RoomDatabase() {
    abstract fun getNoteDao(): NoteDao

    companion object {
        private var instance: NotesDatabase? = null

        @Synchronized
        fun getDatabase(context: Context?): NotesDatabase? {
            if (instance == null) {
                instance = Room.databaseBuilder(
                        context!!,
                        NotesDatabase::class.java,
                        "notes_db"
                ).build()
            }
            return instance
        }
    }
}