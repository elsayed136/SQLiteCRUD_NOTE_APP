package com.example.sqlitecrud

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.text.ClipboardManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Toast
import kotlinx.android.synthetic.main.row.view.*

class NoteAdapter(val context: Context, val notes: ArrayList<Notes>) : BaseAdapter() {

    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
        val note = notes[p0]
        val inflater = LayoutInflater.from(p2!!.context).inflate(R.layout.row, null)
        inflater.tvTitle.text = note.title
        inflater.tvDes.text = note.des

        // btn Delete
        inflater.btnDelete.setOnClickListener {
            val note_title = note.title
            var alertDialog = AlertDialog.Builder(context)
                .setTitle("Warning")
                .setMessage("Are You Sure To Delete ${note_title}")
                .setPositiveButton("Yes", DialogInterface.OnClickListener { dialog, which ->
                    if (MainActivity.db.deleteNote(note.id!!)) {
                        notes.removeAt(p0)
                        notifyDataSetChanged()
                        Toast.makeText(context, "$note_title Deleted", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(context, "Error Deleting", Toast.LENGTH_SHORT).show()
                    }
                })
                .setNegativeButton("No", DialogInterface.OnClickListener { dialog, which -> })
                .setIcon(R.drawable.ic_warning)
                .show()
        }

        // btn Update
        inflater.btnEdit.setOnClickListener {
            var intent = Intent(context, AddOrEdteActivity::class.java)
            intent.putExtra("id", note.id)
            intent.putExtra("title", note.title)
            intent.putExtra("des", note.des)
            context.startActivity(intent)
        }

        // btn copy
        inflater.btnCopy.setOnClickListener {
            val title = inflater.tvTitle.toString()  // get title
            val des = inflater.tvDes.toString()     // get desc
            //concatinate
            val s = title + "\n" + des
            val clipbord = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            clipbord.text = s
            Toast.makeText(context  , "Copied...", Toast.LENGTH_LONG).show()
        }

        // btn share
        inflater.btnShare.setOnClickListener {
            val title = inflater.tvTitle.text.toString()  // get title
            val des = inflater.tvDes.text.toString()     // get desc
            //concatinate
            val s = title + "\n" + des
            val intent = Intent()
            intent.action = Intent.ACTION_SEND
            intent.putExtra(Intent.EXTRA_TEXT,s)
            intent.type = "text/plain"
            context.startActivity(Intent.createChooser(intent,"Share to :"))
        }
        return inflater
    }

    override fun getItem(p0: Int): Any {
        return notes[p0]
    }

    override fun getItemId(p0: Int): Long {
        return p0.toLong()
    }

    override fun getCount(): Int {
        return notes.size
    }
}