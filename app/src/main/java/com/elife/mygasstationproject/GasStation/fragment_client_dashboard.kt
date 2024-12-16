package com.elife.mygasstationproject.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.elife.mygasstationproject.Model.GasStation
import com.elife.mygasstationproject.R
import com.elife.mygasstationproject.Utils.RetrofitClient
import com.elife.mygasstationproject.adapters.GasStationAdapter
import com.example.mygasstation.network.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
class ClientDashboardFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var gasStationAdapter: GasStationAdapter
    private val gasStationList = mutableListOf<GasStation>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_client_dashboard, container, false)

        // Initialisation RecyclerView
        recyclerView = view.findViewById(R.id.rvGasStationList)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Initialiser l'adaptateur en passant le contexte
        gasStationAdapter = GasStationAdapter(gasStationList, requireContext())
        recyclerView.adapter = gasStationAdapter

        // Appel API pour récupérer les stations-service
        fetchGasStations()

        return view
    }

    private fun fetchGasStations() {
        val call = RetrofitInstance.api.getGasStations()

        call.enqueue(object : Callback<List<GasStation>> {
            override fun onResponse(
                call: Call<List<GasStation>>,
                response: Response<List<GasStation>>
            ) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        gasStationList.clear()
                        gasStationList.addAll(it)
                        gasStationAdapter.notifyDataSetChanged()
                    }
                } else {
                    Toast.makeText(requireContext(), "Failed to load data", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<GasStation>>, t: Throwable) {
                Toast.makeText(requireContext(), "Error: ${t.message}", Toast.LENGTH_LONG).show()
            }
        })
    }
}


