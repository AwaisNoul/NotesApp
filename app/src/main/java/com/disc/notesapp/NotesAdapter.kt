package com.disc.notesapp

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class NotesAdapter(val context: Context, val list: List<Notes>) :
    RecyclerView.Adapter<NotesAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view: View = inflater.inflate(R.layout.item_view, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val data = list[position]
        holder.title.text = data.title
        holder.description.text = data.description

        holder.cardview.setOnClickListener {
            val intent = Intent(context, EditorActivity::class.java)
            intent.putExtra("tit", data.title)
            intent.putExtra("des", data.description)
            context.startActivity(intent)
        }

        holder.cardview.setOnLongClickListener {

            val dialogView = LayoutInflater.from(context).inflate(R.layout.save_dialog_layout, null)
            val dialog = AlertDialog.Builder(context).setView(dialogView).create()

            val saveTextView = dialogView.findViewById<TextView>(R.id.save)
            val discardTextView = dialogView.findViewById<TextView>(R.id.discard)

            saveTextView.setOnClickListener {
                val database = NotesDatabase.getDatabase(context)
                GlobalScope.launch(Dispatchers.IO) {
                    database.notesDao().deleteNotes(data)
                }
                dialog.dismiss()
            }
            discardTextView.setOnClickListener {
                dialog.dismiss()
            }

            dialog.show()

            true
        }
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title = itemView.findViewById<TextView>(R.id.title)
        val cardview = itemView.findViewById<CardView>(R.id.cardview)
        val description = itemView.findViewById<TextView>(R.id.description)
    }

}