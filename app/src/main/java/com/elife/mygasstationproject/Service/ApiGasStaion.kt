package com.example.mygasstation.network

import com.elife.mygasstationproject.Model.GasStation
import retrofit2.Call
import retrofit2.http.GET

interface GasStationApi {
    @GET("/manager/all/gas-stations")
    fun getGasStations(): Call<List<GasStation>>
}
