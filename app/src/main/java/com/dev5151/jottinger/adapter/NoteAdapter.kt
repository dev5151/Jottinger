package com.dev5151.jottinger.adapter

import android.graphics.BitmapFactory
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.dev5151.jottinger.R
import com.dev5151.jottinger.data.Note
import com.dev5151.jottinger.ui.NoteClickInterface
import kotlinx.android.synthetic.main.note_item.view.*
import kotlin.collections.ArrayList

class NoteAdapter(noteList: List<Note>, private val noteClickInterface: NoteClickInterface) : RecyclerView.Adapter<NoteAdapter.NoteViewHolder>(), Filterable {

    private val notes = ArrayList<Note>()

    init {
        notes.addAll(noteList)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.note_item, parent, false)
        return NoteViewHolder(view, noteClickInterface)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        holder.setNote(note = notes[position])

    }

    override fun getItemCount(): Int {
        return notes.size
    }

    class NoteViewHolder(itemView: View, private val noteClickInterface: NoteClickInterface) : RecyclerView.ViewHolder(itemView) {
        fun setNote(note: Note) {
            itemView.tvTitle.text = note.title
            itemView.tvSubtitle.text = note.subtitle
            itemView.tvDateTime.text = note.dateTime

            //Setting background
            val gradientDrawable = itemView.layoutNote.background
            if (note.color != null) {
                gradientDrawable.setTint(Color.parseColor(note.color))
            } else {
                gradientDrawable.setTint(Color.parseColor("#333333"))
            }

            if (note.imagePath != null) {
                itemView.imgNote.setImageBitmap(BitmapFactory.decodeFile(note.imagePath))
                itemView.imgNote.visibility = View.VISIBLE
            } else {
                itemView.imgNote.visibility = View.GONE
            }

            //Handle item click
            itemView.setOnClickListener {
                noteClickInterface.noteClick(adapterPosition, note)
            }
        }

    }

    override fun getFilter(): Filter {
        return notesFilter
    }

    private var notesFilter = object : Filter() {
        override fun performFiltering(charSequence: CharSequence?): FilterResults {
            val filteredNotes = ArrayList<Note>()
            if (charSequence == null || charSequence.isEmpty()) {
                filteredNotes.addAll(noteList)
            } else {
                val filterPattern = charSequence.toString().toLowerCase().trim()
                for (note: Note in noteList) {
                    if (note.title!!.toLowerCase().contains(filterPattern)) {
                        filteredNotes.add(note)
                    }
                }
            }

            val results = FilterResults()
            results.values = filteredNotes
            return results
        }

        override fun publishResults(p0: CharSequence?, filterResults: FilterResults?) {
            notes.clear()
            notes.addAll(filterResults?.values as ArrayList<Note>)
            notifyDataSetChanged()
        }

    }


}

