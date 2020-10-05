package com.dev5151.notezz.ui.activities

import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.dev5151.notezz.NoteViewModel
import com.dev5151.notezz.R
import com.dev5151.notezz.di.ViewModelProviderFactory
import com.dev5151.notezz.databinding.ActivityNoteBinding
import com.dev5151.notezz.databinding.LayoutMiscellaneousBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_note.view.*
import javax.inject.Inject


class NoteActivity : DaggerAppCompatActivity() {

    @Inject
    lateinit var viewModelProviderFactory: ViewModelProviderFactory

    lateinit var noteViewModel: NoteViewModel

    lateinit var binding: ActivityNoteBinding

    //private var save: ImageView? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_note)

        //val includedView: LayoutMiscellaneousBinding = binding.layoutMisc

        setupViewModel()
        binding.noteViewModel = noteViewModel

        binding.imgSave.setOnClickListener {
            saveNote(binding)
        }

        binding.imgBack.setOnClickListener {
            finish()
        }

        initMiscellaneous()

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
        Toast.makeText(this, "Note Saved", Toast.LENGTH_SHORT).show()
        finish()
    }

    private fun initMiscellaneous() {
        val bottomSheetBehavior = BottomSheetBehavior.from(binding.layoutMisc.root)
        binding.layoutMisc.root.viewTreeObserver.addOnGlobalLayoutListener {
            bottomSheetBehavior.peekHeight = binding.layoutMisc.tvMiscellaneous.bottom
        }
        binding.layoutMisc.tvMiscellaneous.setOnClickListener {
            if (bottomSheetBehavior.state != BottomSheetBehavior.STATE_EXPANDED) {
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
            } else {
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
            }
        }
    }


    private fun setupViewModel() {
        noteViewModel = ViewModelProvider(this, viewModelProviderFactory).get(NoteViewModel::class.java)

    }


}