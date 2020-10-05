package com.dev5151.notezz.ui.activities

import android.content.res.Resources
import android.graphics.Color
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
import kotlinx.android.synthetic.main.activity_note.*
import kotlinx.android.synthetic.main.activity_note.view.*
import javax.inject.Inject


class NoteActivity : DaggerAppCompatActivity() {

    @Inject
    lateinit var viewModelProviderFactory: ViewModelProviderFactory

    lateinit var noteViewModel: NoteViewModel

    lateinit var binding: ActivityNoteBinding

    private var selectedNoteColor: String = "#333333"

    //private var save: ImageView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_note)

        setupViewModel()
        binding.noteViewModel = noteViewModel

        binding.imgSave.setOnClickListener {
            saveNote(binding)
        }

        binding.imgBack.setOnClickListener {
            finish()
        }

        initMiscellaneous()
        setSubTitleIndicatorColor()

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

        noteViewModel.saveNote(title, subtitle, note,selectedNoteColor)
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

        binding.layoutMisc.imgColorDefault.setOnClickListener{
            selectedNoteColor="#333333"
            binding.layoutMisc.imgColorDefault.setImageResource(R.drawable.ic_tick)
            binding.layoutMisc.imgColorYellow.setImageResource(0)
            binding.layoutMisc.imgColorRed.setImageResource(0)
            binding.layoutMisc.imgColorBlue.setImageResource(0)
            binding.layoutMisc.imgColorBlack.setImageResource(0)
            setSubTitleIndicatorColor()
        }

        binding.layoutMisc.imgColorYellow.setOnClickListener{
            selectedNoteColor="#FDBE38"
            binding.layoutMisc.imgColorDefault.setImageResource(0)
            binding.layoutMisc.imgColorYellow.setImageResource(R.drawable.ic_tick)
            binding.layoutMisc.imgColorRed.setImageResource(0)
            binding.layoutMisc.imgColorBlue.setImageResource(0)
            binding.layoutMisc.imgColorBlack.setImageResource(0)
            setSubTitleIndicatorColor()

        }

        binding.layoutMisc.imgColorRed.setOnClickListener{
            selectedNoteColor="#FF4842"
            binding.layoutMisc.imgColorDefault.setImageResource(0)
            binding.layoutMisc.imgColorYellow.setImageResource(0)
            binding.layoutMisc.imgColorRed.setImageResource(R.drawable.ic_tick)
            binding.layoutMisc.imgColorBlue.setImageResource(0)
            binding.layoutMisc.imgColorBlack.setImageResource(0)
            setSubTitleIndicatorColor()
        }

        binding.layoutMisc.imgColorBlue.setOnClickListener{
            selectedNoteColor="#3A52Fc"
            binding.layoutMisc.imgColorDefault.setImageResource(0)
            binding.layoutMisc.imgColorYellow.setImageResource(0)
            binding.layoutMisc.imgColorRed.setImageResource(0)
            binding.layoutMisc.imgColorBlue.setImageResource(R.drawable.ic_tick)
            binding.layoutMisc.imgColorBlack.setImageResource(0)
            setSubTitleIndicatorColor()
        }

        binding.layoutMisc.imgColorBlack.setOnClickListener{
            selectedNoteColor="#000000"
            binding.layoutMisc.imgColorDefault.setImageResource(0)
            binding.layoutMisc.imgColorYellow.setImageResource(0)
            binding.layoutMisc.imgColorRed.setImageResource(0)
            binding.layoutMisc.imgColorBlue.setImageResource(0)
            binding.layoutMisc.imgColorBlack.setImageResource(R.drawable.ic_tick)
            setSubTitleIndicatorColor()
        }
    }

    private fun setSubTitleIndicatorColor(){
        val gradientDrawable=binding.viewSubtitleIndicator.background
        gradientDrawable.setTint(Color.parseColor(selectedNoteColor))
    }


    private fun setupViewModel() {
        noteViewModel = ViewModelProvider(this, viewModelProviderFactory).get(NoteViewModel::class.java)

    }


}