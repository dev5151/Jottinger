package com.dev5151.jottinger.ui

import com.dev5151.jottinger.data.Note

interface NoteClickInterface {
    fun noteClick(id: Int, note: Note)
}