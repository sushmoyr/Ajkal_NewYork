package com.sushmoyr.ajkalnewyork

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.sushmoyr.ajkalnewyork.databinding.FragmentUserAdBinding


class UserAdFragment : Fragment() {

    private var _binding: FragmentUserAdBinding ?= null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentUserAdBinding.inflate(inflater, container, false)
        return binding.root
    }

}