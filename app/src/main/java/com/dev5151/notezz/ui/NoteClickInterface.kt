package com.dev5151.notezz.ui

import com.dev5151.notezz.data.Note

interface NoteClickInterface {
    fun noteClick(id: Int, note: Note)
}