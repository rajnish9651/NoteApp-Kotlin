package com.example.noteapp_kotlin.repository

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.noteapp_kotlin.dao.NoteDao
import com.example.noteapp_kotlin.model.Note


@Database(entities = [Note::class], version = 1, exportSchema = false)
abstract class NoteDBHelper : RoomDatabase() {
    abstract fun noteDao(): NoteDao

    companion object {

        private var INSTANCE: NoteDBHelper? = null

        fun getDatabase(context: Context): NoteDBHelper {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        NoteDBHelper::class.java,
                        "note_db"
                    ).build()

                    INSTANCE = instance

                }
                return instance

            }

        }
    }
}