package com.dev5151.notezz.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dev5151.notezz.R
import com.dev5151.notezz.data.Note
import com.dev5151.notezz.ui.NoteClickInterface
import kotlinx.android.synthetic.main.note_item.view.*

class NoteAdapter(noteList:List<Note>): RecyclerView.Adapter<NoteAdapter.NoteViewHolder>() {

    private val notes = mutableListOf<Note>()

    init {
        notes.addAll(noteList)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.note_item, parent, false)
        return NoteViewHolder(view)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        holder.bind(item = notes[position])
    }

    override fun getItemCount(): Int {
        return notes.size
    }

    class NoteViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        fun bind(item: Note) {
            itemView.tvTitle.text = item.title
            if(item.subtitle?.isEmpty()!!){
                itemView.tvSubtitle.visibility=View.GONE
            }else{
                itemView.tvSubtitle.text = item.subtitle

            }

            itemView.tvDateTime.text = item.dateTime
            //Handle item click
            itemView.setOnClickListener {

            }
        }
    }

}