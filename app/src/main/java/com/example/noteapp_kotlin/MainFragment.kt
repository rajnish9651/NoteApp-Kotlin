package com.example.noteapp_kotlin

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.noteapp_kotlin.adapter.NoteAdapter
import com.example.noteapp_kotlin.model.Note
import com.example.noteapp_kotlin.repository.NoteDBHelper
import com.example.noteapp_kotlin.repository.NoteRepository
import com.example.noteapp_kotlin.viewmodel.NoteViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton


class MainFragment : Fragment(), ItemClick {

    private lateinit var addBtn: FloatingActionButton
    private lateinit var recyclerView: RecyclerView
    private lateinit var img: ImageView
    private lateinit var searchNote: EditText
    private lateinit var viewModel: NoteViewModel
    private lateinit var noteDatabase: NoteDBHelper
    private lateinit var repository: NoteRepository
    private lateinit var noteAdapter: NoteAdapter
    private var originalNoteList: List<Note> = listOf()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_main, container, false)

        addBtn = view.findViewById(R.id.add_note)
        recyclerView = view.findViewById(R.id.recycler_view)
        img = view.findViewById(R.id.imageView)
        searchNote = view.findViewById(R.id.searchNote)

        // Setting up database, repository, and ViewModel
        noteDatabase = NoteDBHelper.getDatabase(requireActivity())
        repository = NoteRepository(noteDatabase.noteDao())
        viewModel = ViewModelProvider(this)[NoteViewModel::class.java]

//    setting the adapter
        noteAdapter = NoteAdapter(requireActivity(), this)
        recyclerView.layoutManager = GridLayoutManager(requireActivity(), 2)
        recyclerView.adapter = noteAdapter

        //featch all notes initially
        viewModel.fetchAllNotes()

        // Observing the data from ViewModel to update the UI whenever notes change
        viewModel.allNote.observe(requireActivity(), Observer { list ->
            originalNoteList = list
            if (list != null && list.isNotEmpty()) {
                // Update the adapter with the latest note list
                noteAdapter.updateList(list)
                noteAdapter.notifyDataSetChanged()
                img.visibility = View.GONE
                recyclerView.visibility = View.VISIBLE
            } else {

                img.visibility = View.VISIBLE
                recyclerView.visibility = View.GONE
            }

        })

        //adding note
        addBtn.setOnClickListener(View.OnClickListener {

            // Replacing the current fragment with AddUpdateFragment to add or update notes
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentMain, AddUpdateFragment())
                .addToBackStack(null)
                .commit()

        })

        // for searching note based on each character
        searchNote.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                filterNote(s.toString())
            }

        })

        return view
    }

    //filtering the note
    private fun filterNote(query: String) {

        var filterList = originalNoteList.filter {
            it.title.contains(
                query,
                ignoreCase = true
            ) || it.description.contains(query, ignoreCase = true)
        }
        noteAdapter.updateList(filterList)
        noteAdapter.notifyDataSetChanged()
    }


    //deleting note
    override fun deleteNote(note: Note) {
        viewModel.deleteNote(note)
    }


}