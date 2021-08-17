package com.sushmoyr.ajkalnewyork.fragments

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sushmoyr.ajkalnewyork.models.core.District
import com.sushmoyr.ajkalnewyork.models.core.Division
import com.sushmoyr.ajkalnewyork.models.core.News
import com.sushmoyr.ajkalnewyork.repository.Repository
import kotlinx.coroutines.launch

class MapViewModel: ViewModel() {

    private val repository = Repository().remoteDataSource

    init {
        getAllDivision()
    }

    private val _selectedDivision = MutableLiveData<String>()
    val selectedDivision = _selectedDivision

    fun setSelectedDivision(division: String){
        _selectedDivision.value = division
    }

    private val _selectedDistrict = MutableLiveData<String>()
    val selectedDistrict = _selectedDistrict

    fun setSelectedDistrict(district: String){
        _selectedDivision.value = district
    }

    private val _divisions = MutableLiveData<List<Division>>()
    val divisions get() = _divisions

    private fun getAllDivision() {
        viewModelScope.launch{
            val response = repository.getAllDivision()
            if(response.isSuccessful && response.body() != null){
                Log.d("selection", response.body()!!.size.toString())
                _divisions.postValue(response.body()!!)
            }
            else{
                Log.d("selection", "error")
            }
        }
    }

    fun getDivisionFromName(name: String): Division?{
        return _divisions.value?.find {
            it.divisionName == name
        }
    }

    private val _districts = MutableLiveData<List<District>>()
    val districts get() = _districts

    fun getDistrictByDivision(divisionId: String){
        viewModelScope.launch{
            val response = repository.getDistrictByDivision(divisionId)
            if(response.isSuccessful && response.body() != null){
                _districts.postValue(response.body())
            }
        }
    }

    private val _bdNews = MutableLiveData<List<News>>()
    val bdNews get() = _bdNews

    fun getBdNews(divisionName: String?=null, districtName: String?=null){
        viewModelScope.launch {
            var divisionId: String? = null
            var districtId: String? = null

            if(divisions.value!=null)
                divisionId = divisions.value!!.find { it.divisionName==divisionName}?.id
            if (districts.value!=null)
                districtId = districts.value!!.find { it.districtName == districtName}?.id

            val response = when{
                divisionName!=null && districtName!=null -> {
                    Log.d("bdNews", "call both")
                    repository.getBdNews(divisionName, districtName)
                }
                divisionName!=null && districtName==null -> {
                    Log.d("bdNews", "call one")
                    repository.getBdNews(divisionName)
                }
                else -> {
                    Log.d("bdNews", "call none")
                    repository.getBdNews()
                }
            }

            if(response.isSuccessful){
                Log.d("bdNews", response.body()!!.size.toString())
            }
        }
    }

}