package com.mozgolom112.colormyviews

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.mozgolom112.colormyviews.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        setListeners()
    }

    private fun setListeners() {
        //Синтетики теперь деприкейт(( Нужно использовать либо Data binding либо View Binding
        //https://android-developers.googleblog.com/2022/02/discontinuing-kotlin-synthetics-for-views.html
//        val clickableViews:List<View> =
//            listOf(txtBoxOne, txtBoxTwo, txtBoxThree, txtBoxFour, txtBoxFive)
        val clickableViews: List<View> =
            listOf(
                binding.txtBoxOne,
                binding.txtBoxTwo,
                binding.txtBoxThree,
                binding.txtBoxFour,
                binding.txtBoxFive,
                binding.btnRed,
                binding.btnYellow,
                binding.btnGreen
            )

        for (item in clickableViews) {
            item.setOnClickListener { makeColored(it) }
        }
    }

    fun makeColored(view: View) {
        when (view.id) {

            // Boxes using Color class colors for background
            R.id.txtBoxOne -> view.setBackgroundColor(Color.DKGRAY)
            R.id.txtBoxTwo -> view.setBackgroundColor(Color.GRAY)

            // Boxes using Android color resources for background
            R.id.txtBoxThree -> view.setBackgroundResource(android.R.color.holo_green_light)
            R.id.txtBoxFour -> view.setBackgroundResource(android.R.color.holo_green_dark)
            R.id.txtBoxFive -> view.setBackgroundResource(android.R.color.holo_green_light)

            R.id.btnRed -> binding.txtBoxThree.setBackgroundColor(resources.getColor(R.color.my_red))
            R.id.btnYellow -> binding.txtBoxFour.setBackgroundColor(resources.getColor(R.color.my_yellow))
            R.id.btnGreen -> binding.txtBoxFive.setBackgroundColor(resources.getColor(R.color.my_green))

            else -> view.setBackgroundColor(Color.LTGRAY)
        }
    }
}