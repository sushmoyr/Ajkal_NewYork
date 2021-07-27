package com.sushmoyr.ajkalnewyork.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.sushmoyr.ajkalnewyork.VideosAdapter
import com.sushmoyr.ajkalnewyork.VideosPageViewModel
import com.sushmoyr.ajkalnewyork.databinding.FragmentVideosBinding

class VideosFragment : Fragment() {

    private var _binding: FragmentVideosBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: VideosPageViewModel
    private val adapter: VideosAdapter by lazy {
        VideosAdapter()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider.AndroidViewModelFactory(requireActivity().application)
            .create(VideosPageViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentVideosBinding.inflate(inflater, container, false)

        viewModel.allVideos.observe(viewLifecycleOwner, {data->
            adapter.setData(data)
        })


        setupRecyclerView()

        return binding.root
    }

    private fun setupRecyclerView() {
        val recyclerView = binding.videoRv
        val layoutManager = LinearLayoutManager(requireContext())
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}