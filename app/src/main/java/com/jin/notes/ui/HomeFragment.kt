package com.jin.notes.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.jin.notes.R
import com.jin.notes.room.NoteDatabase
import kotlinx.coroutines.launch

class HomeFragment : BaseFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val buttonAdd: FloatingActionButton = view.findViewById(R.id.button_add)
        val recyclerViewNotes: RecyclerView = view.findViewById(R.id.recycler_view_notes)

        buttonAdd.setOnClickListener {
            val action = HomeFragmentDirections.actionAddNote()
            Navigation.findNavController(it).navigate(action)
        }

        recyclerViewNotes.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
        }

        launch {
            context?.let {
                val notes = NoteDatabase(it).getNoteDao().getAllNotes()
                recyclerViewNotes.adapter = NotesAdapter(notes)
            }
        }
    }
}