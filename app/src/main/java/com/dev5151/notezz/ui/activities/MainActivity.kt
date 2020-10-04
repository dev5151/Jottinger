package com.dev5151.notezz.ui.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.dev5151.notezz.NoteViewModel
import com.dev5151.notezz.R
import com.dev5151.notezz.adapter.NoteAdapter
import com.dev5151.notezz.data.Note
import com.dev5151.notezz.di.ViewModelProviderFactory
import com.google.android.material.floatingactionbutton.FloatingActionButton
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

class MainActivity : DaggerAppCompatActivity() {

    private lateinit var noteAdapter: NoteAdapter

    private var fabAddNotes: FloatingActionButton? = null

    private lateinit var noteViewModel: NoteViewModel

    @Inject
    lateinit var viewModelProviderFactory: ViewModelProviderFactory

    lateinit var allNotes: List<Note>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initView()
        setupViewModel()
        initRecyclerView(allNotes)
        observeLiveData()


        fabAddNotes!!.setOnClickListener { startActivity(Intent(this@MainActivity, NoteActivity::class.java)) }


    }

    private fun initView() {
        allNotes= emptyList()
        fabAddNotes = findViewById(R.id.fab)
    }

    private fun setupViewModel() {
        noteViewModel =
                ViewModelProvider(this, viewModelProviderFactory).get(NoteViewModel::class.java)
    }

    private fun observeLiveData(){
        noteViewModel.getAllNotes()?.observe(this, Observer {
            listofNotes->listofNotes?.let{
            allNotes=it
            initRecyclerView(allNotes)
        }
        })
    }

    private fun initRecyclerView(allNote: List<Note>) {
        recyclerView.apply {
            noteAdapter = NoteAdapter(
                    allNotes
            )
            layoutManager = StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL)
            adapter = noteAdapter
        }
    }
}

