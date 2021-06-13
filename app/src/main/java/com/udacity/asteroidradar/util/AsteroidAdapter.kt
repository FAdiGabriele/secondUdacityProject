package com.udacity.asteroidradar.util

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.udacity.asteroidradar.models.Asteroid
import com.udacity.asteroidradar.databinding.SingleAsteroidElementBinding

class AsteroidAdapter(val clickListener: AsteroidListener) : ListAdapter<Asteroid, AsteroidAdapter.AsteroidHolder>(
        AsteroidDiffCallback()
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AsteroidHolder {
        return AsteroidHolder.from(
                parent
        )
    }

    override fun onBindViewHolder(holder: AsteroidHolder, position: Int) {
        val item = getItem(position)
        holder.bind(clickListener, item)
    }


    class AsteroidHolder private constructor(val binding: SingleAsteroidElementBinding)
        : RecyclerView.ViewHolder(binding.root) {

        fun bind(clickListener: AsteroidListener, item: Asteroid) {
            binding.asteroid = item
            binding.clickListener = clickListener
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): AsteroidHolder {
                val binding = SingleAsteroidElementBinding
                        .inflate(LayoutInflater.from(parent.context), parent, false)
                return AsteroidHolder(
                        binding
                )
            }
        }
    }

    class AsteroidDiffCallback : DiffUtil.ItemCallback<Asteroid>() {
        override fun areItemsTheSame(oldItem: Asteroid, newItem: Asteroid): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Asteroid, newItem: Asteroid): Boolean {
            return oldItem == newItem
        }
    }
}

class AsteroidListener(val clickListener: (asteroid: Asteroid) -> Unit) {
    fun onClick(asteroid: Asteroid) = clickListener(asteroid)
}