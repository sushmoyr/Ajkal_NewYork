package com.sushmoyr.ajkalnewyork.fragments.gallery

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.sushmoyr.ajkalnewyork.databinding.FragmentGalleryBinding

class GalleryFragment : Fragment() {

    private var _binding: FragmentGalleryBinding?= null
    private val binding get() = _binding!!
    private val model: GalleryViewModel by viewModels()
    private val imageAdapter by lazy {
        GalleryAdapter()
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGalleryBinding.inflate(inflater, container, false)

        val rv = binding.galleryRv
        val lm = StaggeredGridLayoutManager(2, RecyclerView.VERTICAL)
        rv.apply {
            layoutManager = lm
            adapter = imageAdapter
        }

        model.getImages()
        model.galleryImages.observe(viewLifecycleOwner, {
            imageAdapter.setData(it)
        })

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}