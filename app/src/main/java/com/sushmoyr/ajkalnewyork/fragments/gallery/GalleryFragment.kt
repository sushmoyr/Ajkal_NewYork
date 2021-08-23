package com.sushmoyr.ajkalnewyork.fragments.gallery

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.sushmoyr.ajkalnewyork.R
import com.sushmoyr.ajkalnewyork.databinding.FragmentGalleryBinding

class GalleryFragment : Fragment() {

    private var _binding: FragmentGalleryBinding?= null
    private val binding get() = _binding!!
    private val model: GalleryViewModel by viewModels()
    private val imageAdapter by lazy {
        GalleryAdapter()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val callback = requireActivity().onBackPressedDispatcher.addCallback(this) {
            findNavController().navigateUp()
        }
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

        imageAdapter.itemClickListener = {
            val action = GalleryFragmentDirections.actionGalleryFragmentToDetailImageActivity(it)
            findNavController().navigate(action)
        }

        binding.loading.visibility = View.VISIBLE
        model.getImages()
        model.galleryImages.observe(viewLifecycleOwner, {
            if(it.isSuccessful){
                binding.loading.visibility = View.GONE
                if(it.body().isNullOrEmpty()){
                    binding.errorMsgText.visibility = View.VISIBLE
                    binding.errorMsgText.text = resources.getString(R.string.no_data)
                }
                else{
                    binding.errorMsgText.visibility = View.GONE
                    imageAdapter.setData(it.body()!!)
                }
            }
            else{
                binding.loading.visibility = View.GONE
                binding.errorMsgText.text = it.message()
            }
        })

        return binding.root
    }




    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}