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
    private val myName_: MyName = MyName("Nikita Golovanov")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.apply {
            myName = myName_
            btnDone.setOnClickListener { addNickname(it) }
        }
    }

    private fun addNickname(view: View) {
        binding.apply {
            myName?.nickname = binding.edittxtvNickname.text.toString()
            invalidateAll()
            edittxtvNickname.visibility = View.GONE
            btnDone.visibility = View.GONE
            txtNickname.visibility = View.VISIBLE
        }
        //hide keyboard
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }


}