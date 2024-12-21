package com.example.noteapp_kotlin.repository

import com.example.noteapp_kotlin.dao.NoteDao
import com.example.noteapp_kotlin.model.Note

class NoteRepository(private val noteDao: NoteDao) {


    suspend fun getAllNotes(): List<Note> {
        return noteDao.getAllNotes()
    }


    suspend fun insert(note: Note) {

        noteDao.insert(note)
    }

    suspend fun update(note: Note) {
        noteDao.update(note)
    }

    suspend fun delete(note: Note) {
        noteDao.delete(note)
    }


}