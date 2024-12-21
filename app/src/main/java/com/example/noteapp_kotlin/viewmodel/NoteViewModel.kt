package com.example.noteapp_kotlin.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.noteapp_kotlin.model.Note
import com.example.noteapp_kotlin.repository.NoteDBHelper
import com.example.noteapp_kotlin.repository.NoteRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class NoteViewModel(application: Application) : AndroidViewModel(application) {

    //  MutableLiveData  to update data
    private val allNotesMutableLiveData = MutableLiveData<List<Note>>()

    // LiveData for exposing the note to the UI
    val allNote: LiveData<List<Note>> = allNotesMutableLiveData

    // initializing the database using the singleton instance
    val noteViewModel = NoteDBHelper.getDatabase(application)
    //creating the instance of repository to interact with db
    val repository = NoteRepository(noteViewModel.noteDao())


    fun insert(note: Note) {
        viewModelScope.launch {   //  Using viewModelScope to launch a coroutine in a background thread.
            repository.insert(note)
        }
    }

    // Fetch all notes asynchronously from database
    fun fetchAllNotes() {
        viewModelScope.launch {
            // Launching an async coroutine in the background thread
            val notes = async {
                repository.getAllNotes()
            }.await()
            // Update LiveData on the main thread
            allNotesMutableLiveData.postValue(notes)
        }
    }

    // this will update the specific note in database
    fun updateNote(notedata: Note) {
        viewModelScope.launch {
            repository.update(notedata)
        }
    }

    fun deleteNote(note: Note) {

        viewModelScope.launch {
            repository.delete(note)
            fetchAllNotes()  // fetching notes after delete
        }
    }


}

