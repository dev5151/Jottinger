package com.dev5151.notezz.ui.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.dev5151.notezz.R
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {
    private var fabAddNotes: FloatingActionButton? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initView()
        fabAddNotes!!.setOnClickListener { startActivity(Intent(this@MainActivity, NoteActivity::class.java)) }
    }

    private fun initView() {
        fabAddNotes = findViewById(R.id.fab)
    }
}