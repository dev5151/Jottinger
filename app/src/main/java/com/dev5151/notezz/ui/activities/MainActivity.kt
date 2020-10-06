package com.dev5151.notezz.ui.activities

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
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


    lateinit var mainActivityBinding: ActivityMainBinding

    @Inject
    lateinit var viewModelProviderFactory: ViewModelProviderFactory

    lateinit var allNotes: List<Note>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mainActivityBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)



        initView()
        setupViewModel()
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
        const val REQUEST_CODE_ADD_NOTE = 1
        const val REQUEST_CODE_UPDATE_NOTE = 2
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

}

