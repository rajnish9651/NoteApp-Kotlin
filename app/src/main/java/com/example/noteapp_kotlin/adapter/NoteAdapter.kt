package com.example.noteapp_kotlin.adapter

import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.noteapp_kotlin.AddUpdateFragment
import com.example.noteapp_kotlin.ItemClick
import com.example.noteapp_kotlin.R
import com.example.noteapp_kotlin.model.Note


class NoteAdapter(private val context: Context, private val itemClick: ItemClick) :
    RecyclerView.Adapter<NoteAdapter.ViewHolder>() {

    // List to store the notes that will be displayed in the RecyclerView
    private var noteList = mutableListOf<Note>()

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView = view.findViewById(R.id.titleText)
        val content: TextView = view.findViewById(R.id.contentText)
    }

    // Update list and notify the adapter that the data has changed
    fun updateList(newNoteList: List<Note>) {
        noteList.clear()
        noteList.addAll(newNoteList)


    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(context).inflate(R.layout.item_note, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val note = noteList[position]
        holder.title.text = note.title
        holder.content.text = note.description

        //for edit
        holder.itemView.setOnClickListener(View.OnClickListener {
            val fragmentManager = (context as FragmentActivity).supportFragmentManager
            val fragmentTransaction = fragmentManager.beginTransaction()

            val addUpdateFragment = AddUpdateFragment()

            val bundle = Bundle()
// Pass the note data to the fragment using a bundle
            bundle.putParcelable("noteList", note)
            addUpdateFragment.arguments = bundle// Set the arguments for the fragment

            // Replace the current fragment with AddUpdateFragment and add it to the back stack
            fragmentTransaction.replace(R.id.fragmentMain, addUpdateFragment)
            fragmentTransaction.addToBackStack(null)  // Add to back stack so user can navigate back
            fragmentTransaction.commit()
        })

        //for delete
        holder.itemView.setOnLongClickListener(View.OnLongClickListener {

            // creating dialog box for confirm deletion
            var builder = AlertDialog.Builder(context)
            builder.setTitle("Delete Note")
            builder.setMessage("Are you sure you want to delete note?")
            builder.setIcon(R.drawable.delete_forever_icon)
            builder.setPositiveButton("Yes", DialogInterface.OnClickListener { dialog, which ->
                // Call the deleteNote method from the ItemClick interface
                itemClick.deleteNote(note)
                // Remove the note from the list and notify the adapter
                noteList = noteList.apply { removeAt(position) }
                notifyItemRemoved(position)
                dialog.dismiss()
            })

            builder.setNegativeButton("No", DialogInterface.OnClickListener { dialog, which ->
                dialog.dismiss()
            })

            builder.create().show()
            true
        })
    }

    override fun getItemCount(): Int {
        return noteList.size
    }
}