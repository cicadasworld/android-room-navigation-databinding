package com.jin.notes.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.jin.notes.R
import com.jin.notes.databinding.ItemNoteBinding
import com.jin.notes.room.Note

class NotesAdapter(val notes: List<Note>) : RecyclerView.Adapter<NotesAdapter.NoteViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view =
            DataBindingUtil.inflate<ItemNoteBinding>(inflater, R.layout.item_note, parent, false)
        return NoteViewHolder(view)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        holder.view.note = notes[position]
        holder.view.root.setOnClickListener {
            val action = HomeFragmentDirections.actionAddNote(notes[position])
            Navigation.findNavController(it).navigate(action)
        }
    }

    override fun getItemCount(): Int = notes.size

    class NoteViewHolder(val view: ItemNoteBinding) : RecyclerView.ViewHolder(view.root)

}