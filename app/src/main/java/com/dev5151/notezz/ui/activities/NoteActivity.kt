package com.dev5151.notezz.ui.activities

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.dev5151.notezz.NoteViewModel
import com.dev5151.notezz.R
import com.dev5151.notezz.data.Note
import com.dev5151.notezz.di.ViewModelProviderFactory
import com.dev5151.notezz.databinding.ActivityNoteBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_note.*
import kotlinx.android.synthetic.main.layout_add_url_dialog.*
import java.util.regex.Pattern
import javax.inject.Inject


class NoteActivity : DaggerAppCompatActivity() {

    @Inject
    lateinit var viewModelProviderFactory: ViewModelProviderFactory

    lateinit var noteViewModel: NoteViewModel

    lateinit var binding: ActivityNoteBinding

    private var selectedNoteColor: String = "#333333"

    private var imagePath = ""

    private var webLink = ""

    private var alreadyAvailableNote: Note? = null


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

        binding.imgRemoveWebUrl.setOnClickListener {
            binding.tvWebUrl.text = null
            binding.layoutWebUrl.visibility = View.GONE
        }

        binding.imgRemoveImage.setOnClickListener {
            binding.imageNote.setImageBitmap(null)
            imageNote.visibility = View.GONE
            binding.imgRemoveImage.visibility = View.GONE
            imagePath = ""
        }

        if (intent.getBooleanExtra("isViewOrUpdate", false)) {
            alreadyAvailableNote = intent.getSerializableExtra("note") as Note?
            setViewOrUpdateNote()
        }else if (intent.getBooleanExtra("isFromQuickAction", false)) {
            val type = intent.getStringExtra("quickActionType");
            if (type != null) {
                if (type == "image") {
                    imagePath = intent.getStringExtra("selectedImagePath")
                    binding.imageNote.setImageBitmap(BitmapFactory.decodeFile(imagePath))
                    binding.imageNote.visibility = View.VISIBLE
                    binding.imgRemoveImage.visibility = View.VISIBLE

                }else if( type=="url"){
                    binding.tvWebUrl.text = intent.getStringExtra("url")
                    binding.layoutWebUrl.visibility = View.VISIBLE
                }
            }
        }



        initMiscellaneous()
        setSubTitleIndicatorColor()

    }

    private fun setViewOrUpdateNote() {
        binding.edtNoteTitle.setText(alreadyAvailableNote?.title)
        binding.edtSubTitle.setText(alreadyAvailableNote?.subtitle)
        binding.edtNote.setText(alreadyAvailableNote?.noteText)
        binding.tvDateTime.text = alreadyAvailableNote?.dateTime

        if (alreadyAvailableNote!!.imagePath != null && alreadyAvailableNote!!.imagePath.toString().trim().isNotEmpty()) {
            binding.imageNote.setImageBitmap(BitmapFactory.decodeFile(alreadyAvailableNote!!.imagePath))
            binding.imageNote.visibility = View.VISIBLE
            binding.imgRemoveImage.visibility = View.VISIBLE
            imagePath = alreadyAvailableNote!!.imagePath.toString()
        } else {
            binding.imageNote.visibility = View.GONE
        }

        if (alreadyAvailableNote!!.web_link != null && alreadyAvailableNote!!.web_link.toString().trim().isNotEmpty()) {
            binding.tvWebUrl.text = alreadyAvailableNote!!.web_link
            binding.layoutWebUrl.visibility = View.VISIBLE
        } else {
            binding.imgRemoveWebUrl.visibility = View.GONE
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

        if (binding.layoutWebUrl.visibility == View.VISIBLE) {
            webLink = binding.tvWebUrl.text.toString()

        }

        if (alreadyAvailableNote == null) {
            noteViewModel.saveNote(0, title, subtitle, note, imagePath, selectedNoteColor, webLink)
        } else {
            noteViewModel.updateNote(note = Note(alreadyAvailableNote!!.id, title, subtitle, noteViewModel.dateTime, note, imagePath, selectedNoteColor, webLink))
        }

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

        binding.layoutMisc.imgColorDefault.setOnClickListener {
            selectedNoteColor = "#333333"
            binding.layoutMisc.imgColorDefault.setImageResource(R.drawable.ic_tick)
            binding.layoutMisc.imgColorYellow.setImageResource(0)
            binding.layoutMisc.imgColorRed.setImageResource(0)
            binding.layoutMisc.imgColorBlue.setImageResource(0)
            binding.layoutMisc.imgColorBlack.setImageResource(0)
            setSubTitleIndicatorColor()
        }

        binding.layoutMisc.imgColorYellow.setOnClickListener {
            selectedNoteColor = "#FDBE38"
            binding.layoutMisc.imgColorDefault.setImageResource(0)
            binding.layoutMisc.imgColorYellow.setImageResource(R.drawable.ic_tick)
            binding.layoutMisc.imgColorRed.setImageResource(0)
            binding.layoutMisc.imgColorBlue.setImageResource(0)
            binding.layoutMisc.imgColorBlack.setImageResource(0)
            setSubTitleIndicatorColor()
        }

        binding.layoutMisc.imgColorRed.setOnClickListener {
            selectedNoteColor = "#FF4842"
            binding.layoutMisc.imgColorDefault.setImageResource(0)
            binding.layoutMisc.imgColorYellow.setImageResource(0)
            binding.layoutMisc.imgColorRed.setImageResource(R.drawable.ic_tick)
            binding.layoutMisc.imgColorBlue.setImageResource(0)
            binding.layoutMisc.imgColorBlack.setImageResource(0)
            setSubTitleIndicatorColor()
        }

        binding.layoutMisc.imgColorBlue.setOnClickListener {
            selectedNoteColor = "#3A52Fc"
            binding.layoutMisc.imgColorDefault.setImageResource(0)
            binding.layoutMisc.imgColorYellow.setImageResource(0)
            binding.layoutMisc.imgColorRed.setImageResource(0)
            binding.layoutMisc.imgColorBlue.setImageResource(R.drawable.ic_tick)
            binding.layoutMisc.imgColorBlack.setImageResource(0)
            setSubTitleIndicatorColor()
        }

        binding.layoutMisc.imgColorBlack.setOnClickListener {
            selectedNoteColor = "#000000"
            binding.layoutMisc.imgColorDefault.setImageResource(0)
            binding.layoutMisc.imgColorYellow.setImageResource(0)
            binding.layoutMisc.imgColorRed.setImageResource(0)
            binding.layoutMisc.imgColorBlue.setImageResource(0)
            binding.layoutMisc.imgColorBlack.setImageResource(R.drawable.ic_tick)
            setSubTitleIndicatorColor()
        }

        if (alreadyAvailableNote != null && alreadyAvailableNote!!.color != null) {
            when (alreadyAvailableNote!!.color) {
                "#FDBE38" -> binding.layoutMisc.imgColorYellow.performClick()
                "#FF4842" -> binding.layoutMisc.imgColorRed.performClick()
                "#3A52Fc" -> binding.layoutMisc.imgColorBlue.performClick()
                "#000000" -> binding.layoutMisc.imgColorBlack.performClick()
            }
        }

        binding.layoutMisc.layoutAddImage.setOnClickListener {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
            if (ActivityCompat.checkSelfPermission(this, READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(
                        this,
                        arrayOf(READ_EXTERNAL_STORAGE),
                        READ_EXTERNAL_STORAGE_REQUEST_CODE
                )
            } else {
                pickImage()
            }
        }

        binding.layoutMisc.layoutAddUrl.setOnClickListener {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
            showUrlDialog()
        }

        if (alreadyAvailableNote != null) {
            binding.layoutMisc.layoutDeleteNote.visibility = View.VISIBLE
            binding.layoutMisc.layoutDeleteNote.setOnClickListener {
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
                showDeleteNoteDialog()
            }
        }

    }

    private fun showDeleteNoteDialog() {
        val builder = AlertDialog.Builder(this@NoteActivity)
        val view = LayoutInflater.from(this).inflate(R.layout.layout_delete_note, null)
        builder.setView(view)

        val alertDialog = builder.create()
        if (alertDialog.window != null) {
            alertDialog.window!!.setBackgroundDrawable(ColorDrawable(0))
        }

        view.findViewById<TextView>(R.id.tvDeleteNote).setOnClickListener {
            noteViewModel.delete(alreadyAvailableNote!!)
            alertDialog.dismiss()
            finish()
        }


        view.findViewById<TextView>(R.id.tvCancelDeleteNote).setOnClickListener {
            alertDialog.dismiss()
            finish()
        }

        alertDialog.show()

    }

    private fun pickImage() {
        val intent = Intent(
                Intent.ACTION_PICK,
                MediaStore.Images.Media.INTERNAL_CONTENT_URI
        )
        intent.type = "image/*"
        startActivityForResult(intent, PICK_IMAGE_REQUEST_CODE)
    }

    private fun getPathFromUri(uri: Uri): String? {
        var filePath: String? = null
        val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
        val cursor = contentResolver.query(uri, filePathColumn, null, null, null)
        if (cursor != null) {
            cursor.moveToFirst()
            val index = cursor.getColumnIndex("_data")
            filePath = cursor.getString(index)
            cursor.close()
        } else {
            filePath = uri.path.toString()
        }
        return filePath

    }

    private fun showUrlDialog() {
        val builder = AlertDialog.Builder(this@NoteActivity)
        val view = LayoutInflater.from(this).inflate(R.layout.layout_add_url_dialog, binding.root.findViewById(R.id.layoutWebUrl), false)
        builder.setView(view)

        val alertDialog = builder.create()
        if (alertDialog.window != null) {
            alertDialog.window!!.setBackgroundDrawable(ColorDrawable(0))
        }

        val urlEdt: EditText = view.findViewById(R.id.edtUrl)
        view.findViewById<TextView>(R.id.tv_add).setOnClickListener {
            if (urlEdt.text.toString().trim().isEmpty()) {
                Toast.makeText(applicationContext, "Enter Url", Toast.LENGTH_LONG).show()
            } else if (!Patterns.WEB_URL.matcher(urlEdt.text.toString()).matches()) {
                Toast.makeText(this, "Enter a valid url", Toast.LENGTH_SHORT).show()
            } else {
                binding.tvWebUrl.text = urlEdt.text.toString()
                binding.layoutWebUrl.visibility = View.VISIBLE
                alertDialog.dismiss()
            }
        }
        view.findViewById<TextView>(R.id.tv_cancel).setOnClickListener {
            alertDialog.dismiss()
        }

        alertDialog.show()
    }

    private fun setSubTitleIndicatorColor() {
        val gradientDrawable = binding.viewSubtitleIndicator.background
        gradientDrawable.setTint(Color.parseColor(selectedNoteColor))
    }

    private fun setupViewModel() {
        noteViewModel = ViewModelProvider(this, viewModelProviderFactory).get(NoteViewModel::class.java)

    }

    override fun onRequestPermissionsResult(
            requestCode: Int,
            permissions: Array<out String>,
            grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            READ_EXTERNAL_STORAGE_REQUEST_CODE -> {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // pick image after request permission success
                    pickImage()
                } else {
                    Toast.makeText(this, "Permission is required", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST_CODE) {
            if (resultCode != Activity.RESULT_OK) {
                return
            }
            val uri = data?.data
            if (uri != null) {
                val inputStream = contentResolver.openInputStream(uri)
                val bitmap = BitmapFactory.decodeStream(inputStream)
                binding.imageNote.setImageBitmap(bitmap)
                binding.imageNote.visibility = View.VISIBLE
                binding.imgRemoveImage.visibility = View.VISIBLE
                imagePath = getPathFromUri(uri).toString()
            }
        }
    }

    companion object {
        const val PICK_IMAGE_REQUEST_CODE = 1000
        const val READ_EXTERNAL_STORAGE_REQUEST_CODE = 1001
    }


}