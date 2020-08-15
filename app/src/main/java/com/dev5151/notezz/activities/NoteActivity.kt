package com.dev5151.notezz.activities

import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.dev5151.notezz.NoteViewModel
import com.dev5151.notezz.R
import com.dev5151.notezz.data.Note
import com.dev5151.notezz.databinding.ActivityNoteBinding
import java.text.SimpleDateFormat
import java.util.*

class NoteActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityNoteBinding = DataBindingUtil.setContentView(this, R.layout.activity_note);
        val noteViewModel = ViewModelProvider(this).get(NoteViewModel::class.java)
        binding.noteViewModel = noteViewModel

    }
    /*private fun saveNote() {
        if (edtNoteTitle!!.text.toString().trim { it <= ' ' }.isEmpty()) {
            Toast.makeText(this, "Note title can't be empty!", Toast.LENGTH_SHORT).show()
            return
        } else if (edtNoteSubTitle!!.text.toString().trim { it <= ' ' }.isEmpty() && edtNote!!.text.toString().trim { it <= ' ' }.isEmpty()) {
            Toast.makeText(this, "Note can't be empty!", Toast.LENGTH_SHORT).show()
            return
        }
        val note = Note()
        note.title = edtNoteTitle!!.text.toString()
        note.subtitle = edtNoteSubTitle!!.text.toString()
        note.noteText = edtNote!!.text.toString()
        note.dateTime = tvDateTime!!.text.toString()
    }*/
}