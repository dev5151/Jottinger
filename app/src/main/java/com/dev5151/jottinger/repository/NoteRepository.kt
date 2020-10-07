package com.dev5151.jottinger.repository

import android.util.Log
import androidx.lifecycle.LiveData
import com.dev5151.jottinger.data.Note
import com.dev5151.jottinger.data.NoteDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class NoteRepository @Inject constructor(private val noteDao: NoteDao) {


    // Method #1
    //function to insert note in database
    fun insert(note: Note) {
        CoroutineScope(Dispatchers.IO).launch {
            noteDao.insertNote(note)

        }
    }

    // Method #2
    //function to delete note in database
    fun delete(note: Note) {
        CoroutineScope(Dispatchers.IO).launch {
            noteDao.deleteNote(note)
        }
    }

    // Method #3
    //function to get all notes in database
    fun getAllNotes(): LiveData<List<Note>>? {
        return noteDao.getAllNotes()
    }

    //Method #4
    //function to update note in database
    fun update(note: Note) {
        CoroutineScope(Dispatchers.IO).launch {
            noteDao.update(note)
            Log.e("DEBUG", "update is called in repo")
        }
    }


}