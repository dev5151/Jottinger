package com.dev5151.notezz.ui.activities

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Patterns
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.SearchView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.dev5151.notezz.NoteListener
import com.dev5151.notezz.NoteViewModel
import com.dev5151.notezz.R
import com.dev5151.notezz.adapter.NoteAdapter
import com.dev5151.notezz.data.Note
import com.dev5151.notezz.databinding.ActivityMainBinding
import com.dev5151.notezz.databinding.ActivityNoteBinding
import com.dev5151.notezz.di.ViewModelProviderFactory
import com.dev5151.notezz.ui.NoteClickInterface
import com.google.android.material.floatingactionbutton.FloatingActionButton
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

class MainActivity : DaggerAppCompatActivity(), NoteClickInterface, SearchView.OnQueryTextListener {

    private lateinit var noteAdapter: NoteAdapter

    private lateinit var noteViewModel: NoteViewModel

    private var noteClickInterface: NoteClickInterface? = null

    private var noteClickedPosition = -1


    private lateinit var mainActivityBinding: ActivityMainBinding

    @Inject
    lateinit var viewModelProviderFactory: ViewModelProviderFactory

    lateinit var allNotes: List<Note>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mainActivityBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        setSupportActionBar(mainActivityBinding.bottomAppBar)

        initView()
        setupViewModel()
        setUpBottomAppBar()
        noteClickInterface = this
        initRecyclerView(allNotes, this)
        observeLiveData()
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        mainActivityBinding.searchView.setOnClickListener {
            mainActivityBinding.searchView.setSearchableInfo(searchManager.getSearchableInfo(this.componentName))

        }
        mainActivityBinding.searchView.isIconifiedByDefault = false
        mainActivityBinding.searchView.setOnQueryTextListener(this)


        mainActivityBinding.fab.setOnClickListener { startActivity(Intent(this@MainActivity, NoteActivity::class.java)) }


    }

    private fun initView() {
        allNotes = emptyList()
    }

    private fun setupViewModel() {
        noteViewModel =
                ViewModelProvider(this, viewModelProviderFactory).get(NoteViewModel::class.java)
    }

    private fun setUpBottomAppBar() {
        mainActivityBinding.bottomAppBar.setOnMenuItemClickListener(object : MenuItem.OnMenuItemClickListener, Toolbar.OnMenuItemClickListener {
            override fun onMenuItemClick(item: MenuItem?): Boolean {
                when (item!!.itemId) {
                    R.id.menuAddUrl -> {
                        showUrlDialog()
                    }
                    R.id.menuAddPic -> {
                        if (ActivityCompat.checkSelfPermission(this@MainActivity, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(
                                    this@MainActivity,
                                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                                    NoteActivity.READ_EXTERNAL_STORAGE_REQUEST_CODE
                            )
                        } else {
                            pickImage()
                        }
                    }
                    R.id.menuAddList -> Toast.makeText(this@MainActivity, "Coming Soon", Toast.LENGTH_LONG).show()
                }

                return true
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.app_bar_menu, menu)
        return true
    }

    private fun observeLiveData() {
        noteViewModel.getAllNotes()?.observe(this, Observer { listofNotes ->
            listofNotes?.let {
                allNotes = it
                initRecyclerView(allNotes, noteClickInterface!!)
            }
        })
    }

    private fun initRecyclerView(allNote: List<Note>, noteClickInterface: NoteClickInterface) {
        mainActivityBinding.recyclerView.apply {
            noteAdapter = NoteAdapter(
                    allNotes, noteClickInterface
            )
            layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
            adapter = noteAdapter
        }
    }

    companion object {
        const val REQUEST_CODE_UPDATE_NOTE = 2
        const val PICK_IMAGE_REQUEST_CODE = 3
    }

    override fun noteClick(id: Int, note: Note) {
        noteClickedPosition = id
        val intent = Intent(this@MainActivity, NoteActivity::class.java)
        intent.putExtra("isViewOrUpdate", true)
        intent.putExtra("note", note)
        startActivityForResult(intent, REQUEST_CODE_UPDATE_NOTE)
    }

    override fun onQueryTextSubmit(p0: String?): Boolean {
        return false
    }

    override fun onQueryTextChange(p0: String?): Boolean {
        noteAdapter.filter.filter(p0)
        return false
    }

    private fun pickImage() {
        val intent = Intent(
                Intent.ACTION_PICK,
                MediaStore.Images.Media.INTERNAL_CONTENT_URI
        )
        intent.type = "image/*"
        startActivityForResult(intent, PICK_IMAGE_REQUEST_CODE)
    }

    override fun onRequestPermissionsResult(
            requestCode: Int,
            permissions: Array<out String>,
            grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            NoteActivity.READ_EXTERNAL_STORAGE_REQUEST_CODE -> {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // pick image after request permission success
                    pickImage()
                } else {
                    Toast.makeText(this@MainActivity, "Permission is required", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun showUrlDialog() {
        val builder = AlertDialog.Builder(this@MainActivity)
        val view = LayoutInflater.from(this).inflate(R.layout.layout_add_url_dialog, mainActivityBinding.root.findViewById(R.id.layoutWebUrl), false)
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
                alertDialog.dismiss()
                val intent = Intent(this@MainActivity, NoteActivity::class.java)
                intent.putExtra("isFromQuickAction", true)
                intent.putExtra("quickActionType", "url")
                intent.putExtra("url", urlEdt.text.toString().trim())
                startActivity(intent)
            }
        }
        view.findViewById<TextView>(R.id.tv_cancel).setOnClickListener {
            alertDialog.dismiss()
        }

        alertDialog.show()
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                val uri = data.data
                if (uri != null) {
                    val selectedImagePath = getPathFromUri(uri)
                    val intent = Intent(this@MainActivity, NoteActivity::class.java)
                    intent.putExtra("isFromQuickAction", true)
                    intent.putExtra("quickActionType", "image")
                    intent.putExtra("selectedImagePath", selectedImagePath)
                    startActivity(intent)
                }
            }
        }
    }

}

