package com.mozgolom112.aboutme

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import com.mozgolom112.aboutme.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_main)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
//        findViewById<Button>(R.id.btnDone).setOnClickListener {
//            addNickname(it)
//        }
        binding.btnDone.setOnClickListener { addNickname(it) }
    }

    private fun addNickname(view: View) {
//        var edittxtNickname = findViewById<EditText>(R.id.edittxtvNickname)
//        var txtNickname = findViewById<TextView>(R.id.txtNickname)
        binding.apply {
            txtNickname.text = binding.edittxtvNickname.text
            invalidateAll()
            edittxtvNickname.visibility = View.GONE
            btnDone.visibility = View.GONE
            txtNickname.visibility = View.VISIBLE
        }

//        binding.txtNickname.text = binding.edittxtvNickname.text
//        binding.edittxtvNickname.visibility = View.GONE
//        binding.txtNickname.visibility = View.VISIBLE
//        view.visibility = View.GONE

        //hide keyboard
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }


}