package com.dev5151.notezz.ui.activities

import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.dev5151.notezz.NoteViewModel
import com.dev5151.notezz.R
import com.dev5151.notezz.di.ViewModelProviderFactory
import com.dev5151.notezz.databinding.ActivityNoteBinding
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject


class NoteActivity : DaggerAppCompatActivity() {

    @Inject
    lateinit var viewModelProviderFactory: ViewModelProviderFactory

    lateinit var noteViewModel: NoteViewModel

    //private var save: ImageView? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityNoteBinding = DataBindingUtil.setContentView(this, R.layout.activity_note)

        setupViewModel()
        binding.noteViewModel = noteViewModel


        binding.imgSave.setOnClickListener {
            saveNote(binding)
        }

        binding.imgBack.setOnClickListener {
            finish()
        }

    }


    private fun saveNote(binding: ActivityNoteBinding) {
        if (binding.edtNoteTitle.text.toString().trim { it <= ' ' }.isEmpty()) {
            Toast.makeText(this, "Note title can't be empty!", Toast.LENGTH_SHORT).show()
            return
        } else if (binding.edtSubTitle.text.toString().trim { it <= ' ' }.isEmpty() && binding.edtNote.text.toString().trim { it <= ' ' }.isEmpty()) {
            Toast.makeText(this, "Note can't be empty!", Toast.LENGTH_SHORT).show()
            return
        }
        val title = binding.edtNoteTitle.text.toString()
        val subtitle = binding.edtSubTitle.text.toString()
        val note = binding.edtNote.text.toString()

        noteViewModel.saveNote(title, subtitle, note)
        Toast.makeText(this,"Note Saved",Toast.LENGTH_SHORT).show()
        finish()
    }

    private fun setupViewModel() {
        noteViewModel=ViewModelProvider(this, viewModelProviderFactory).get(NoteViewModel::class.java)

    }

}