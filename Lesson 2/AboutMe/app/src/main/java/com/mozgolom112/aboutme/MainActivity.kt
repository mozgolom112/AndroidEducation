package com.mozgolom112.aboutme

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<Button>(R.id.btnDone).setOnClickListener {
            addNickname(it)
        }
    }

    private fun addNickname(view: View) {
        var edittxtNickname = findViewById<EditText>(R.id.edittxtvNickname)
        var txtNickname = findViewById<TextView>(R.id.txtNickname)

        txtNickname.text = edittxtNickname.text
        edittxtNickname.visibility = View.GONE
        txtNickname.visibility = View.VISIBLE
        view.visibility = View.GONE

        //hide keyboard
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }


}