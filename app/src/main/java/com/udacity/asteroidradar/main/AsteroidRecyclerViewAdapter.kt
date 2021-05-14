package com.udacity.asteroidradar.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.R

class AsteroidRecyclerViewAdapter(private val asteroidList: List<Asteroid>) : RecyclerView.Adapter<AsteroidRecyclerViewAdapter.AsteroidHolder>() {
    class AsteroidHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AsteroidHolder {
        val itemView: View = LayoutInflater.from(parent.context).inflate(
            R.layout.single_asteroid_element, parent, false)
        return AsteroidHolder(itemView)
    }

    override fun onBindViewHolder(holder: AsteroidHolder, position: Int) {
        TODO("Not yet implemented")
    }

    override fun getItemCount(): Int {
        return asteroidList.size
    }
}