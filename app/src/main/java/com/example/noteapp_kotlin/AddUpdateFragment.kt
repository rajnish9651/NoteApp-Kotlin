package com.example.noteapp_kotlin

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.noteapp_kotlin.model.Note
import com.example.noteapp_kotlin.repository.NoteDBHelper
import com.example.noteapp_kotlin.repository.NoteRepository
import com.example.noteapp_kotlin.viewmodel.NoteViewModel


class AddUpdateFragment : Fragment() {

    private lateinit var btnAction: ImageView
    private lateinit var title: EditText
    private lateinit var content: EditText
    private lateinit var noteTextView: TextView
    private lateinit var btnBack: ImageView
    private lateinit var btnShare: ImageView

    private lateinit var viewModel: NoteViewModel
    private lateinit var noteDatabase: NoteDBHelper
    private lateinit var repository: NoteRepository


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_add_update, container, false)

        btnAction = view.findViewById(R.id.btnAction)
        title = view.findViewById(R.id.noteTitle)
        content = view.findViewById(R.id.noteContent)
        noteTextView = view.findViewById(R.id.noteText)
        btnBack = view.findViewById(R.id.btnBack)
        btnShare = view.findViewById(R.id.btnShare)

        // Initialize database, repository, and ViewModel
        noteDatabase = NoteDBHelper.getDatabase(requireActivity())
        repository = NoteRepository(noteDatabase.noteDao())
        viewModel = ViewModelProvider(this)[NoteViewModel::class.java]


        // Check if arguments are passed to the fragment (for updating an existing note)
        if (arguments != null) {
            // Get the Note object passed in the arguments bundle
            val notes: Note? = arguments?.getParcelable("noteList")

            noteTextView.setText("Update")
            content.setText(notes?.description)
            title.setText(notes?.title)

            // Setting the value for update not when button is clicked
            btnAction.setOnClickListener(View.OnClickListener {
                val updatedTitle: String = title.getText().toString().trim()
                val updatedContent: String = content.getText().toString().trim()
                val notedata = Note(notes!!.id, updatedTitle, updatedContent)

                viewModel.updateNote(notedata)
                fragmentManager?.popBackStack() // Pop the fragment from the back stack return to previous fragment

            })
        } else {

            // Add new note if no existing note is passed
            btnAction.setOnClickListener(View.OnClickListener {
                var titleStr = title.text.toString().trim()
                var contentStr = content.text.toString().trim()

                // Check if both title and content are not empty before adding the note
                if (titleStr.isNotEmpty() && contentStr.isNotEmpty()) {
                    val note = Note(0, titleStr, contentStr)
                    viewModel.insert(note)
                    fragmentManager?.popBackStack()
                } else {
                    Toast.makeText(
                        context,
                        "please enter the title and content both",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })

        }

        //for sharing note
        btnShare.setOnClickListener(View.OnClickListener {

            val titleShare: String = title.text.toString().trim()
            val contentShare: String = content.text.toString().trim()

            //checking title and content is not empty before sharing note
            if (titleShare.isNotEmpty() && contentShare.isNotEmpty()) {

//                createing intent to send text data
                var shareIntent = Intent(Intent.ACTION_SEND)

                shareIntent.setType("text/plain")
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, titleShare)
                shareIntent.putExtra(Intent.EXTRA_TEXT, contentShare)

                // Check if the activity is available and then start the share Intent.
                activity?.let { context ->
                    //this will creating chooser from where user send the data
                    var chooserIntent = Intent.createChooser(shareIntent, "share note by")
                    context.startActivity(chooserIntent)
                }
            } else {
                Toast.makeText(context, "title or content is empty. cant share", Toast.LENGTH_SHORT)
                    .show()
            }
        })

        //for back button
        btnBack.setOnClickListener(View.OnClickListener {
            fragmentManager?.popBackStack()
        })

        return view // Return the inflated view
    }

}