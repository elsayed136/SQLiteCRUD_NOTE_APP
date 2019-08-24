package com.example.sqlitecrud

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import android.widget.Toast
import java.lang.Exception

class DbManger(val context: Context) : SQLiteOpenHelper(context, DB_NAME, FACTORY, DB_VERSION) {

    companion object {
        private val DB_NAME = "MyNote"
        private val FACTORY = null
        private val DB_VERSION = 1
        private val DB_TABLE = "Notes"
        private val COL_NOTE_ID = "ID"
        private val COL_NOTE_TITLE = "Title"
        private val COL_NOTE_DESCRIPTION = "Description"
    }

    override fun onCreate(p0: SQLiteDatabase?) {
        val CREATE_NOTE_TABLE =
            "CREATE TABLE IF NOT EXISTS $DB_TABLE($COL_NOTE_ID INTEGER PRIMARY KEY autoincrement ,$COL_NOTE_TITLE TEXT,$COL_NOTE_DESCRIPTION TEXT);"
        p0?.execSQL(CREATE_NOTE_TABLE)
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
        val DROP_NOTE_TABLE = "DROP TABLE IF EXISTS $DB_TABLE"
        p0?.execSQL(DROP_NOTE_TABLE)
    }

    fun getAllNotes(context: Context): ArrayList<Notes> {
        val db = this.readableDatabase
        val qry = "SELECT * FROM $DB_TABLE"
        val cursor = db.rawQuery(qry, null)
        val notes = ArrayList<Notes>()

        if (cursor.count == 0) {
            Toast.makeText(context, "No Record Found", Toast.LENGTH_SHORT).show()
        } else {
            cursor.moveToFirst()
            while (!cursor.isAfterLast) {
                val n = Notes()
                n.id = cursor.getInt(cursor.getColumnIndex(COL_NOTE_ID))
                n.title = cursor.getString(cursor.getColumnIndex(COL_NOTE_TITLE))
                n.des = cursor.getString(cursor.getColumnIndex(COL_NOTE_DESCRIPTION))
                notes.add(n)
                cursor.moveToNext()
            }
            Toast.makeText(context, "${cursor.count} Record Found", Toast.LENGTH_SHORT).show()
        }
        cursor.close()
        db.close()
        return notes
    }

    fun addNote(context: Context, n: Notes) {
        val values = ContentValues()
        values.put(COL_NOTE_TITLE, n.title)
        values.put(COL_NOTE_DESCRIPTION, n.des)
        val db = this.writableDatabase
        try {
            db.insert(DB_TABLE, null, values)
            Toast.makeText(context, "Note Add", Toast.LENGTH_SHORT).show()
//            var d= AlertDialog.Builder(context)
//
//            d.setMessage("added su")
//            d.show()

        } catch (e: Exception) {
            Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
        }
        db.close()
    }

    fun deleteNote(id: Int): Boolean {
        val db = this.writableDatabase
//        val qry = "DELETE FROM $DB_TABLE WHERE $COL_NOTE_ID = $id"
        var r = false
        try {
            db.delete(DB_TABLE, "$COL_NOTE_ID = ?", arrayOf(id.toString()))
//            db.execSQL(qry)
            r = true
        } catch (e: Exception) {
            Log.e(ContentValues.TAG, "Error Deleting")
            r = false
        }
        db.close()
        return r
    }

    fun updateNote(note:Notes): Boolean {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COL_NOTE_TITLE, note.title)
            put(COL_NOTE_DESCRIPTION, note.des)
        }
        var r = false
        try {
            db.update(DB_TABLE, values, "$COL_NOTE_ID = ?", arrayOf(note.id.toString()))
            r = true
        } catch (e: Exception) {
            Log.e(ContentValues.TAG, "Error Updating")
            r = false
        }
        return r
    }


}

