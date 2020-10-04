package com.dev5151.notezz.data

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface NoteDao {

    @Query("SELECT * FROM notes ORDER BY id DESC")
    fun getAllNotes(): LiveData<List<Note>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertNote(note: Note?)

    @Delete
    fun deleteNote(note: Note?)

    @Query("delete from notes where id = :id")
    fun deleteById(id: Int)
}