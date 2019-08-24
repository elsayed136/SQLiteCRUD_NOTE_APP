package com.example.sqlitecrud

import android.content.ContentValues
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_add_or_edte.*
import java.lang.Exception

class AddOrEdteActivity : AppCompatActivity() {

    val n = Notes()
    var id: Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_or_edte)

//        val c = Calendar.getInstance()
//        val f = SimpleDateFormat("yyyy/MM/dd")
//        val date = f.format(c.time)
        //update note
        try {
            val bundle = intent.extras
            id = bundle!!.getInt("id", 0)
            if (id != 0) {
                //change Actionbar title
                supportActionBar!!.title = "Update Note"
                // change btnadd to update
                btnAdd.text = "Update"
                etTitle.setText(bundle.getString("title"))
                etDes.setText(bundle.getString("des"))
            }
        } catch (e: Exception) {

        }

        btnAdd.setOnClickListener {
            n.id = id
            n.title = etTitle.text.toString()
            n.des = etDes.text.toString()

            if (id == 0){
                if (etTitle.text.isEmpty()) {
                    Toast.makeText(this, "TITLE IS REQUERED", Toast.LENGTH_SHORT).show()
                    etTitle.requestFocus()
                } else {
                    MainActivity.db.addNote(this, n)
                    Clear()
                    finish()
                }
            }else{
                if (etTitle.text.isEmpty()) {
                    Toast.makeText(this.baseContext, "TITLE IS REQUERED", Toast.LENGTH_SHORT).show()
                    etTitle.requestFocus()
                } else {
                    try {
                        MainActivity.db.updateNote(n)
                    }catch (e:Exception){
                        Log.e(ContentValues.TAG,e.message)
                    }
                    Clear()
                    finish()
                }
            }

        }
    }

    fun Clear() {
        etTitle.text.clear()
        etDes.text.clear()
    }
}
