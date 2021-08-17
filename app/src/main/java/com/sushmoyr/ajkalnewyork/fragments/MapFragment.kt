package com.sushmoyr.ajkalnewyork.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.children
import androidx.fragment.app.viewModels
import com.google.android.material.chip.Chip
import com.sushmoyr.ajkalnewyork.R
import com.sushmoyr.ajkalnewyork.databinding.FragmentHomeBinding
import com.sushmoyr.ajkalnewyork.databinding.FragmentMapBinding
import com.sushmoyr.ajkalnewyork.models.core.Category
import com.sushmoyr.ajkalnewyork.models.core.District
import com.sushmoyr.ajkalnewyork.models.core.Division
import java.util.Observer

class MapFragment : Fragment() {

    private var _binding: FragmentMapBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MapViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMapBinding.inflate(inflater, container, false)

        setUpDivisionGroup()

        viewModel.selectedDivision.observe(viewLifecycleOwner,  {
            val selectedDivision = viewModel.getDivisionFromName(it)
            if(selectedDivision!=null){
                viewModel.getDistrictByDivision(selectedDivision.id)
            }
        })

        viewModel.districts.observe(viewLifecycleOwner,  {
            if(!it.isNullOrEmpty()){
                setUpDistrictGroup()
            }
            else
                binding.districtScroll.visibility = View.GONE

        })

        return binding.root
    }

    private fun setUpDistrictGroup() {
        //Get filters from api
        viewModel.districts.observe(viewLifecycleOwner, { districts ->
            val filters = districts.toMutableList()
            val firstItem =
                District("default_district", districtName = resources.getString(R.string
                    .default_district))
            filters.add(0, firstItem)

            setUpDistrictSelectionChips(filters)
        })
    }

    private fun setUpDistrictSelectionChips(filters: List<District>) {
        val chipGroup = binding.districtGroup
        chipGroup.removeAllViews()
        var isChecked = false
        filters.forEach {
            val chip = Chip(requireContext())
            chip.id = View.generateViewId()
            chip.text = it.districtName
            chipGroup.addView(chip)

            if (!isChecked) {
                chipGroup.check(chip.id)
                chip.chipStrokeWidth = 0f
                chip.setChipBackgroundColorResource(R.color.secondaryColor)
                chip.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
                isChecked = true
            }
        }

        chipGroup.isSingleSelection = true

        val placeHolderChip = Chip(requireContext())

        chipGroup.setOnCheckedChangeListener { group, checkedId ->
            group.children.forEach { chip ->
                if (chip is Chip) {
                    if (chip.id == checkedId) {
                        chip.chipStrokeWidth = 0f
                        chip.setChipBackgroundColorResource(R.color.secondaryColor)
                        chip.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
                        viewModel.setSelectedDistrict(chip.text.toString())
                    } else {
                        chip.chipBackgroundColor = placeHolderChip.chipBackgroundColor
                        chip.chipStrokeWidth = placeHolderChip.chipStrokeWidth
                        chip.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
                    }
                }
            }
            val chip = view?.findViewById<Chip>(checkedId)
            if (chip != null) {
                chip.chipStrokeWidth = 0f
                chip.setChipBackgroundColorResource(R.color.secondaryColor)
            }

        }
    }



    private fun setUpDivisionGroup() {
        //Get filters from api
        viewModel.divisions.observe(viewLifecycleOwner, { divisions ->
            val filters = divisions.toMutableList()
            val firstItem =
                Division("default_division", divisionName = resources.getString(R.string.default_division))
            filters.add(0, firstItem)

            setUpSelectionChips(filters)
        })
    }

    private fun setUpSelectionChips(filters: List<Division>) {
        val chipGroup = binding.divisionGroup
        chipGroup.removeAllViews()
        var isChecked = false
        filters.forEach {
            val chip = Chip(requireContext())
            chip.id = View.generateViewId()
            chip.text = it.divisionName
            chipGroup.addView(chip)

            if (!isChecked) {
                chipGroup.check(chip.id)
                chip.chipStrokeWidth = 0f
                chip.setChipBackgroundColorResource(R.color.secondaryColor)
                chip.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
                isChecked = true
            }
        }

        chipGroup.isSingleSelection = true

        val placeHolderChip = Chip(requireContext())

        chipGroup.setOnCheckedChangeListener { group, checkedId ->
            group.children.forEach { chip ->
                if (chip is Chip) {
                    if (chip.id == checkedId) {
                        chip.chipStrokeWidth = 0f
                        chip.setChipBackgroundColorResource(R.color.secondaryColor)
                        chip.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
                        viewModel.setSelectedDivision(chip.text.toString())
                    } else {
                        chip.chipBackgroundColor = placeHolderChip.chipBackgroundColor
                        chip.chipStrokeWidth = placeHolderChip.chipStrokeWidth
                        chip.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
                    }
                }
            }
            val chip = view?.findViewById<Chip>(checkedId)
            if (chip != null) {
                chip.chipStrokeWidth = 0f
                chip.setChipBackgroundColorResource(R.color.secondaryColor)
            }

        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}