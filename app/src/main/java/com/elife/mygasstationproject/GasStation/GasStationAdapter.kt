package com.elife.mygasstationproject.adapters

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.elife.mygasstationproject.Model.GasStation
import com.elife.mygasstationproject.R

class GasStationAdapter(
    private val gasStations: List<GasStation>,
    private val context: Context
) : RecyclerView.Adapter<GasStationAdapter.GasStationViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GasStationViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_gas_station, parent, false)
        return GasStationViewHolder(view)
    }

    override fun onBindViewHolder(holder: GasStationViewHolder, position: Int) {
        val station = gasStations[position]
        holder.name.text = "Name: ${station.name}"
        holder.available.text = if (station.available) "Available: Yes" else "Available: No"
        holder.year.text = "Year of Creation: ${station.yearOfCreation}"
        holder.address.text = "Address: ${station.address}"
        holder.location.text = "Location: ${station.location}"

        // Ajouter un gestionnaire de clics pour ouvrir Google Maps
        holder.itemView.setOnClickListener {
            openGoogleMaps(station.location)
        }
    }

    override fun getItemCount(): Int = gasStations.size

    private fun openGoogleMaps(location: String) {
        // Créer un Intent pour ouvrir l'URL dans Google Maps
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(location))
        intent.putExtra(Intent.EXTRA_REFERRER, Uri.parse("android-app://com.google.android.apps.maps"))
        context.startActivity(intent)
    }

    class GasStationViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val name: TextView = view.findViewById(R.id.tvGasStationName)
        val available: TextView = view.findViewById(R.id.tvGasStationAvailable)
        val year: TextView = view.findViewById(R.id.tvGasStationYear)
        val address: TextView = view.findViewById(R.id.tvGasStationAddress)
        val location: TextView = view.findViewById(R.id.tvGasStationLocation)
    }
}
