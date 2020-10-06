package com.dev5151.notezz

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.dev5151.notezz.data.Note
import com.dev5151.notezz.repository.NoteRepository
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class NoteViewModel @Inject constructor(private val noteRepository: NoteRepository) : ViewModel() {

    var dateTime: String? = SimpleDateFormat("EEEE, dd MMMM yyyy HH:mm a", Locale.getDefault()).format(Date())

    /*  //private val context = getApplication<Application>().applicationContext
      fun onBackButtonClick(activity: NoteActivity) {
          activity.onBackPressed()
      }
  */
    fun saveNote(id: Int, title: String, subtitle: String, note: String, imagePath: String, color: String, webLink: String) {

        insert(Note(0, title, subtitle, dateTime, note, imagePath, color, webLink))
    }


//Database Operations in view model


    // Method #1
    private fun insert(note: Note) {
        return noteRepository.insert(note)
    }

    // Method #2
    fun delete(note: Note) {
         noteRepository.delete(note)
    }

    // Method #3
    /* fun deleteById(id: Int) {
         noteRepository.deleteById(id)
     }*/

    // Method #4
    fun getAllNotes(): LiveData<List<Note>>? {
        Log.e("DEBUG", "View model getallnotes")
        return noteRepository.getAllNotes()
    }

    //Method #5
    fun updateNote(note: Note) {
        noteRepository.update(note)
    }

}