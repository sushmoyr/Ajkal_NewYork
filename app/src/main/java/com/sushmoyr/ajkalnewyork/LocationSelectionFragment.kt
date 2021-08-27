package com.sushmoyr.ajkalnewyork

import android.R
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.activityViewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.sushmoyr.ajkalnewyork.databinding.FragmentLocationSelectionBinding
import com.sushmoyr.ajkalnewyork.fragments.MapViewModel
import com.sushmoyr.ajkalnewyork.models.core.locations.Country
import com.sushmoyr.ajkalnewyork.models.core.locations.District
import com.sushmoyr.ajkalnewyork.models.core.locations.Location


class LocationSelectionFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentLocationSelectionBinding?= null
    private val binding get() = _binding!!
    private val viewModel: MapViewModel by activityViewModels()

    private var selectedCountry = "-1"
    private var selectedDistrict = "-1"
    private var selectedDivision = "-1"

    private var location = Location()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLocationSelectionBinding.inflate(inflater, container, false)


        viewModel.locationData.observe(viewLifecycleOwner, {
            location = it!!
            setLocationSpinner(location)
        })

        binding.setFilter.setOnClickListener {
            viewModel.getNews(selectedCountry, selectedDivision, selectedDistrict)
            this.dismiss()
        }

        return binding.root
    }

    private fun setLocationSpinner(location: Location) {
        val countrySpinner = binding.countrySpinner
        val countryArray = mutableListOf<String>()

        location.country.forEach { country ->
            countryArray.add(country.countryName)
        }
        val countryAdapter = ArrayAdapter(requireContext(), R.layout
            .simple_dropdown_item_1line, countryArray)
        countrySpinner.setAdapter(countryAdapter)

        countrySpinner.setOnItemClickListener { parent, view, position, id ->
            resetSelections()
            setDivisionSpinner(location.country[position])
            selectedCountry = location.country[position].id
        }

    }

    private fun setDivisionSpinner(location: Country) {
        val divisionSpinner = binding.divisionSpinner
        divisionSpinner.text.clear()
        divisionSpinner.setAdapter(null)
        binding.districtSpinner.text.clear()
        binding.districtSpinner.setAdapter(null)
        val divisionArray = mutableListOf<String>()

        location.divisions.forEach {
            divisionArray.add(it.divisionName)
        }
        val divisionAdapter = ArrayAdapter(requireContext(), R.layout
            .simple_dropdown_item_1line, divisionArray)
        divisionSpinner.setAdapter(divisionAdapter)

        divisionSpinner.setOnItemClickListener { parent, view, position, id ->
            setDistrictSpinner(location.divisions[position].id, location.districts)
            selectedDivision = location.divisions[position].id
        }
    }

    private fun setDistrictSpinner(id: String, districts: List<District>) {
        val filteredDistricts = districts.filter { it.divisionId == id }
        val districtSpinner = binding.districtSpinner
        districtSpinner.text.clear()
        districtSpinner.clearListSelection()
        val districtArray = mutableListOf<String>()
        filteredDistricts.forEach {district ->
            districtArray.add(district.districtName)
        }

        val districtAdapter = ArrayAdapter(requireContext(), R.layout
            .simple_dropdown_item_1line, districtArray)
        districtSpinner.setAdapter(districtAdapter)

        districtSpinner.setOnItemClickListener { parent, view, position, id ->
            selectedDistrict = filteredDistricts[position].id
        }
    }

    private fun resetSelections(){
        selectedCountry = "-1"
        selectedDistrict = "-1"
        selectedDivision = "-1"
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}