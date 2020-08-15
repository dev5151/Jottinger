package com.dev5151.notezz.data

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface NoteDao {
    @get:Query("SELECT * FROM notes ORDER BY id DESC")
    val allNotes: LiveData<List<Note?>?>?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertNote(note: Note?)

    @Delete
    fun deleteNote(note: Note?)
}