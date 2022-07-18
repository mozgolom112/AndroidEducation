package com.example.android.navigation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import com.example.android.navigation.databinding.FragmentTitleBinding

class TitleFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentTitleBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_title, container, false
        )
        //1 вариант
//        binding.btnPlay.setOnClickListener {
//            Navigation.findNavController(it).navigate(R.id.actionTitleFragmentToGameFragment)
//        }
        //2 вариант
        binding.btnPlay.setOnClickListener {
            it.findNavController().navigate(R.id.actionTitleFragmentToGameFragment)
        }
        //3 вариант (почему-то не работает)
//        binding.btnPlay.setOnClickListener {
//            Navigation.createNavigateOnClickListener(R.id.actionTitleFragmentToGameFragment)
//        }
        return binding.root
    }


}