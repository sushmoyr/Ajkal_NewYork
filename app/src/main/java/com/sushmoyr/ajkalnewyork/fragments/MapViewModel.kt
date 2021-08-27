package com.sushmoyr.ajkalnewyork.fragments

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sushmoyr.ajkalnewyork.NetworkResponse
import com.sushmoyr.ajkalnewyork.models.utility.DataModel
import com.sushmoyr.ajkalnewyork.models.core.District
import com.sushmoyr.ajkalnewyork.models.core.Division
import com.sushmoyr.ajkalnewyork.models.core.News
import com.sushmoyr.ajkalnewyork.models.core.locations.Location
import com.sushmoyr.ajkalnewyork.repository.Repository
import com.sushmoyr.ajkalnewyork.utils.ResourceState
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class MapViewModel: ViewModel() {

    private val repository = Repository().remoteDataSource
    private var fetchedAds : List<DataModel.Advertisement> = emptyList()

    init {
        getLocationData()
    }

    private val _locationData = MutableLiveData<Location>()
    val locationData = _locationData

    private fun getLocationData(){
        viewModelScope.launch {
            val response = repository.getAllLocations()
            loadingState.value = ResourceState.Loading(false)
            when(response){
                is NetworkResponse.Success -> {
                    _locationData.postValue(response.response!!)
                }

                else -> loadingState.value = ResourceState.Result(false, response.exception?.message)
            }
        }
    }

    private val _localNewsList = MutableLiveData<NetworkResponse<List<News>>>()
    val localNewsList get() = _localNewsList



    fun getNews(countryId: String, divisionId: String, districtId: String){
        Log.d("mapModel", "Called With $countryId, $divisionId, $districtId")
        _localNewsList.value = NetworkResponse.Loading()
        viewModelScope.launch {
            val response = repository.getLocalNews(countryId, divisionId, districtId)
            Log.d("mapModel", "Received")

            val data = response.response
            if(data!=null){
                Log.d("mapModel", data.size.toString())
                data.forEach {
                    println(it)
                }
            }
            _localNewsList.postValue(response)
        }
    }



    private fun getAds() {
        viewModelScope.launch {
            val adsDeferred = async { repository.getAllAds() }
            val response = adsDeferred.await()
            if(response.isSuccessful){
                Log.d("adSize", "Size ${response.body()!!.size}")
                fetchedAds = response.body()!!
            }
            else{
                fetchedAds = emptyList()
            }
        }
    }

    private val _selectedDivision = MutableLiveData<String>().also {
        it.value = null
    }
    private val selectedDivision = _selectedDivision

    private val selectionPair = MutableLiveData<Pair<String?, String?>>()

    fun setSelectedDivision(division: String?){
        Log.d("bdNews", "division to set ${division?:"null"}")
        _selectedDivision.value = division
        val district = selectedDistrict.value
        val newValue = Pair(division, district)
        selectionPair.value = newValue
    }

    private val _selectedDistrict = MutableLiveData<String>().also {
        it.value = null
    }
    private val selectedDistrict = _selectedDistrict

    fun setSelectedDistrict(district: String?){
        Log.d("bdNews", "district to set ${district?:"null"}")
        _selectedDistrict.value = district
        val division = selectedDivision.value
        val newValue = Pair(division, district)
        selectionPair.value = newValue
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

    private val _bdNews = MutableLiveData<List<DataModel>>()
    val bdNews get() = _bdNews


    val loadingState = MutableLiveData<ResourceState>()

    fun getBdNews(divisionName: String?=null, districtName: String?=null){
        loadingState.value = ResourceState.Loading(true)
        viewModelScope.launch {
            var divisionId: String? = null
            var districtId: String? = null

            if(divisions.value!=null)
                divisionId = divisions.value!!.find { it.divisionName==divisionName}?.id
            if (districts.value!=null)
                districtId = districts.value!!.find { it.districtName == districtName}?.id
            val asyncResponse = when{
                divisionId!=null && districtId!=null -> {
                    Log.d("bdNews", "call both")
                    async { repository.getBdNews(divisionId, districtId) }
                }
                divisionId!=null && districtId==null -> {
                    Log.d("bdNews", "call one")
                    async { repository.getBdNews(divisionId) }
                }
                else -> {
                    Log.d("bdNews", "call none")
                    async { repository.getBdNews() }
                }
            }
            val response = asyncResponse.await()
            val itemList = mutableListOf<DataModel>()

            if(response.isSuccessful){
                if(!response.body().isNullOrEmpty()){
                    val advertisements = fetchedAds.toMutableList()
                    advertisements.shuffle()
                    Log.d("adSize", "Size = ${advertisements.size}")
                    val offset = 5
                    var count = 0
                    var adsIndex = 0
                    val adCount = 2
                    val newsList = response.body()!!.toMutableList()
                    newsList.sortByDescending { it.createdAt }
                    newsList.forEach {
                        Log.d("Maps", it.defaultImage)
                        if (count != 0 && count % offset == 0 && adsIndex < advertisements.size) {
                            for (j in 0 until adCount) {
                                if (adsIndex < advertisements.size) {
                                    itemList.add(advertisements[adsIndex++])
                                    if (adsIndex == advertisements.size) {
                                        adsIndex = 0
                                    }
                                }
                            }
                        } else {
                            itemList.add(it)
                        }
                        count++
                    }

                    loadingState.value = ResourceState.Loading(false)
                    loadingState.value = ResourceState.Result(true)
                }else{
                    loadingState.value = ResourceState.Result(false, "No result found")
                    itemList.clear()
                }
                bdNews.postValue(itemList)

            }
            else{
                loadingState.value = ResourceState.Result(false, "Try again later...")
            }
        }
    }

}