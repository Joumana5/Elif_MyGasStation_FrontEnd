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
import androidx.appcompat.widget.SearchView

class ClientDashboardFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var gasStationAdapter: GasStationAdapter
    private lateinit var searchView: SearchView
    private val gasStationList = mutableListOf<GasStation>()
    private val filteredGasStationList = mutableListOf<GasStation>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_client_dashboard, container, false)

        // Initialisation RecyclerView
        recyclerView = view.findViewById(R.id.rvGasStationList)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Initialiser SearchView
        searchView = view.findViewById(R.id.searchView)

        // Initialiser l'adaptateur avec une liste vide au départ
        gasStationAdapter = GasStationAdapter(filteredGasStationList, requireContext())
        recyclerView.adapter = gasStationAdapter

        // Configurer le SearchView pour filtrer les résultats
        setupSearchView()

        // Charger les données
        fetchGasStations()

        return view
    }

    private fun setupSearchView() {
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                // Pas besoin de gérer la soumission
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                // Filtrer les données à chaque changement de texte
                filterGasStations(newText.orEmpty())
                return true
            }
        })
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
                        // Copier toutes les stations dans la liste filtrée au départ
                        filteredGasStationList.clear()
                        filteredGasStationList.addAll(gasStationList)
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

    private fun filterGasStations(query: String) {
        // Filtrer les stations selon le nom ou l'adresse
        val filteredList = gasStationList.filter { gasStation ->
            gasStation.name.contains(query, ignoreCase = false) || // Sensible à la casse
                    gasStation.address.contains(query, ignoreCase = false) // Sensible à la casse
        }
        // Mettre à jour la liste filtrée et notifier l'adaptateur
        filteredGasStationList.clear()
        filteredGasStationList.addAll(filteredList)
        gasStationAdapter.notifyDataSetChanged()
    }
}



