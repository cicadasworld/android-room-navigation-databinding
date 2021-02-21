package com.jin.notes.ui

import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import android.widget.EditText
import android.widget.Toast
import androidx.navigation.Navigation
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.jin.notes.R
import com.jin.notes.room.Note
import com.jin.notes.room.NoteDatabase
import kotlinx.coroutines.launch

class AddNoteFragment : BaseFragment() {

    private var note: Note? = null
    private lateinit var buttonSave: FloatingActionButton

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.fragment_add_note, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        buttonSave = view.findViewById(R.id.button_save)
        val noteTitle: EditText = view.findViewById(R.id.edit_text_title)
        val noteBody: EditText = view.findViewById(R.id.edit_text_body)

        arguments?.let {
            note = AddNoteFragmentArgs.fromBundle(it).note
            noteTitle.setText(note?.title)
            noteBody.setText(note?.body)
        }

        buttonSave.setOnClickListener {
            val title = noteTitle.text.toString().trim()
            val body = noteBody.text.toString().trim()

            if (title.isEmpty()) {
                noteTitle.error = "Title required"
                return@setOnClickListener
            }
            if (body.isEmpty()) {
                noteBody.error = "Note body required"
                return@setOnClickListener
            }
            val newNote = Note(title, body)
            launch {
                if (note == null) {
                    saveNote(newNote)
                } else {
                    updateNote(newNote)
                }
            }

            navigateBack()
        }
    }

    private fun navigateBack() {
        val action = AddNoteFragmentDirections.actionSaveNote()
        Navigation.findNavController(buttonSave).navigate(action)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.note_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_delete -> {
                note?.let {
                    deleteNote()
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private suspend fun saveNote(note: Note) {
        context?.let {
            NoteDatabase(it).getNoteDao().addNote(note)
            Toast.makeText(it, "Note created", Toast.LENGTH_SHORT).show()
        }
    }

    private suspend fun updateNote(newNote: Note) {
        newNote.id = note!!.id
        context?.let {
            NoteDatabase(it).getNoteDao().updateNote(newNote)
            Toast.makeText(it, "Note updated", Toast.LENGTH_SHORT).show()
        }
    }

    private fun deleteNote() {
        context?.let {
            AlertDialog.Builder(it).apply {
                setTitle("Are you sure?")
                setMessage("You cannot undo this operation.")
                setPositiveButton("OK") { _, _ ->
                    launch {
                        NoteDatabase(it).getNoteDao().deleteNote(note!!)
                        navigateBack()
                    }
                }
                setNegativeButton("Cancel") { _, _ ->
                }
                show()
            }
        }
    }
}