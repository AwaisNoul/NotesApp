package com.disc.notesapp

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {

    lateinit var info: CardView
    lateinit var action_btn: FloatingActionButton
    private lateinit var database: NotesDatabase
    lateinit var notesdao: NotesDAO
    lateinit var list: ArrayList<Notes>
    lateinit var empty_img: ImageView
    lateinit var textview: TextView
    lateinit var recycler_view: RecyclerView

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val color = android.graphics.Color.parseColor("#252525")
        window.statusBarColor = color

        database = NotesDatabase.getDatabase(this)
        notesdao = database.notesDao()

        info = findViewById(R.id.info)
        action_btn = findViewById(R.id.action_btn)
        empty_img = findViewById(R.id.empty_img)
        textview = findViewById(R.id.textview)
        recycler_view = findViewById(R.id.recycler_view)

        info.setOnClickListener {
            val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_layout, null)
            val dialog = AlertDialog.Builder(this).setView(dialogView).create()
            dialog.show()
        }

        val moveslist = listOf<String>("Hello", "World")

        action_btn.setOnClickListener {
            startActivity(Intent(this, EditorActivity::class.java))
        }

        CoroutineScope(Dispatchers.IO).launch {
            notesdao.getAllNotes().collect {
                withContext(Dispatchers.Main) {
                    list = ArrayList(it)
                    Log.i("TAG", "list: $list")

                    if (list.size == 0) {
                        empty_img.visibility = View.VISIBLE
                        textview.visibility = View.VISIBLE
                        recycler_view.visibility = View.GONE
                    } else {
                        empty_img.visibility = View.GONE
                        textview.visibility = View.GONE
                        recycler_view.visibility = View.VISIBLE
                    }
                    recycler_view.adapter = NotesAdapter(this@MainActivity, list)
                    recycler_view.layoutManager = LinearLayoutManager(this@MainActivity)
                }
            }
        }

        val swipeToDeleteCallback = object : SwipeToDeleteCallback() {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                Toast.makeText(this@MainActivity, "Remove item", Toast.LENGTH_SHORT).show()
                recycler_view.adapter?.notifyItemRemoved(position)
            }
        }

        val itemTouchHelper = ItemTouchHelper(swipeToDeleteCallback)

        itemTouchHelper.attachToRecyclerView(recycler_view)

    }

}