package com.disc.notesapp

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class EditorActivity : AppCompatActivity() {

    lateinit var save : CardView
    private lateinit var database: NotesDatabase
    private lateinit var description: EditText
    private lateinit var title: EditText

    lateinit var notesdao : NotesDAO

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editor)
        val color = android.graphics.Color.parseColor("#252525")
        window.statusBarColor = color

        database = NotesDatabase.getDatabase(this)
        notesdao = database.notesDao()

        save = findViewById(R.id.save)
        title = findViewById(R.id.title)
        description = findViewById(R.id.description)

        val intent = intent
        if (intent.hasExtra("tit") && intent.hasExtra("des")) {
            val titleText = intent.getStringExtra("tit")
            val descriptionText = intent.getStringExtra("des")

            title.setText(titleText)
            description.setText(descriptionText)
        }

        save.setOnClickListener {
            // Update notes or insert new notes as needed
            CoroutineScope(Dispatchers.IO).launch {
                if (intent.hasExtra("tit") && intent.hasExtra("des")) {
                    // Update existing note
                    val updatedTitle = title.text.toString()
                    val updatedDescription = description.text.toString()
                    val originalTitle = intent.getStringExtra("tit")

                    notesdao.updateNotes(updatedTitle, updatedDescription, originalTitle.toString())
                } else {
                    // Insert new note
                    notesdao.insertNotes(Notes(0, title.text.toString(), description.text.toString()))
                }
            }
            finish()
        }
    }
}