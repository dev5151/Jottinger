package com.dev5151.notezz

import android.app.Application
import android.view.View
import androidx.lifecycle.ViewModel
import com.dev5151.notezz.activities.NoteActivity
import java.text.SimpleDateFormat
import java.util.*

class NoteViewModel() : ViewModel() {
    var title: String? = null
    var subtitle: String? = null
    var dateTime: String? = SimpleDateFormat("EEEE, dd MMMM yyyy HH:mm a", Locale.getDefault()).format(Date());
    var note: String? = null

    //private val context = getApplication<Application>().applicationContext
    fun onBackButtonClick() {
        //   activity.onBackPressed()
    }
}