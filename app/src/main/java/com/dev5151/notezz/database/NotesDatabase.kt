package com.dev5151.notezz.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.dev5151.notezz.data.Note
import com.dev5151.notezz.data.NoteDao

@Database(entities = [Note::class], version = 1, exportSchema = false)
abstract class NotesDatabase : RoomDatabase() {
    abstract fun noteDao(): NoteDao?

    companion object {
        private var notesDatabase: NotesDatabase? = null

        @Synchronized
        fun getDatabase(context: Context?): NotesDatabase? {
            if (notesDatabase == null) {
                notesDatabase = Room.databaseBuilder(
                        context!!,
                        NotesDatabase::class.java,
                        "notes_db"
                ).build()
            }
            return notesDatabase
        }
    }
}