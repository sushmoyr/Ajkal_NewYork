package com.sushmoyr.ajkalnewyork

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.sushmoyr.ajkalnewyork.activities.viewmodels.SearchViewModel
import com.sushmoyr.ajkalnewyork.databinding.FragmentSearchBinding
import com.sushmoyr.ajkalnewyork.fragments.NewsAdapter
import com.sushmoyr.ajkalnewyork.models.core.News
import com.sushmoyr.ajkalnewyork.utils.SearchState

class SearchFragment : Fragment() {

    private var _binding : FragmentSearchBinding ?= null
    private val binding get() = _binding!!
    private val searchViewModel: SearchViewModel by viewModels()
    private val searchAdapter: NewsAdapter by lazy{
        NewsAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)

        val search = binding.searchView
        search.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null) {
                    searchViewModel.searchNews(query)
                }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }

        })

        searchViewModel.newsList.observe(viewLifecycleOwner, {
            if(it!=null)
                searchAdapter.setData(it)
        })

        searchViewModel.searchState.observe(viewLifecycleOwner, {state ->
            when(state) {
                is SearchState.Loading -> {
                    when(state.isLoading){
                        true -> {
                            binding.errorText.hide()
                            binding.searchProgress.show()
                        }
                        false -> {
                            binding.errorText.hide()
                            binding.searchProgress.hide()
                        }
                    }
                }
                is SearchState.Result -> {
                    when(state.hasResults){
                        true -> {
                            binding.errorText.hide()
                            binding.searchProgress.hide()
                        }
                        false -> {
                            binding.errorText.show()
                            binding.searchProgress.hide()
                        }
                    }
                }
            }
        })

        val rv = binding.searchResultsRv
        rv.apply {
            adapter = searchAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }



        return binding.root
    }

}

private fun View.hide() {
    this.visibility = View.GONE
}

private fun View.show() {
    this.visibility = View.VISIBLE
}
