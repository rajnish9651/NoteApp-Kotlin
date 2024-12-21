//package com.example.noteapp_kotlin.viewmodelfactory
//
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.ViewModelProvider
//import com.example.noteapp_kotlin.repository.NoteRepository
//import com.example.noteapp_kotlin.viewmodel.NoteViewModel
//
//class NoteViewModelFactory(
////    private val repository: NoteRepository
//) : ViewModelProvider.NewInstanceFactory() {
//
//    override fun <T : ViewModel> create(modelClass: Class<T>): T {
//
//        if (modelClass.isAssignableFrom(NoteViewModel::class.java)) {
//            return NoteViewModel(repository) as T
//        }
//        throw IllegalArgumentException("unknow varibles")
//
//    }
//}