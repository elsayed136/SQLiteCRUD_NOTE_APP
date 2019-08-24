package com.example.sqlitecrud

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.*
import android.widget.SearchView
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    companion
    object {
        lateinit var db: DbManger
    }

    var noteList = ArrayList<Notes>()
    lateinit var adapter: NoteAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        db = DbManger(this)

        try {
            viewNotes()
        } catch (e: Exception) {
            Log.e("Error:", e.message)
        }


    }

    override fun onResume() {
        viewNotes()
        super.onResume()
    }

    private fun viewNotes() {
        noteList = db.getAllNotes(this)
        adapter = NoteAdapter(this, noteList)
        lvNotes.adapter = adapter
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        //search in list
        val sv = menu!!.findItem(R.id.app_bar_search).actionView as SearchView
        val sm = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        sv.setSearchableInfo(sm.getSearchableInfo(componentName))
        sv.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(s: String?): Boolean {

                return false
            }

            override fun onQueryTextChange(s: String?): Boolean {
                var filteredNote = ArrayList<Notes>()
                if (!TextUtils.isEmpty(s)) {
                    for (i in 0..noteList.size - 1){
                        if (noteList.get(i).title!!.toLowerCase().contains(s.toString().toLowerCase()) ||
                            noteList.get(i).des!!.toLowerCase().contains(s.toString().toLowerCase()) )
                            filteredNote.add(noteList[i])
                    }
                    adapter = NoteAdapter(this@MainActivity,filteredNote)
                    lvNotes.adapter = adapter
                } else {// if its empty
                    adapter = NoteAdapter(this@MainActivity,noteList)
                    lvNotes.adapter = adapter
                }
                return false
            }
        })
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.add_note -> {
                startActivity(Intent(this, AddOrEdteActivity::class.java))
            }
        }
        return super.onOptionsItemSelected(item)
    }

}
