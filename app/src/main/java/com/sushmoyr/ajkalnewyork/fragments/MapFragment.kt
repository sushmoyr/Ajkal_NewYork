package com.sushmoyr.ajkalnewyork.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.sushmoyr.ajkalnewyork.databinding.FragmentHomeBinding
import com.sushmoyr.ajkalnewyork.databinding.FragmentMapBinding

class MapFragment : Fragment() {

    private var _binding: FragmentMapBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMapBinding.inflate(inflater, container, false)



        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}