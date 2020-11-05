package me.ryanpierce.mapssharedflowdemo

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import me.ryanpierce.mapssharedflowdemo.databinding.LocationListItemBinding

class LocationListAdapter : ListAdapter<Location, RecyclerView.ViewHolder>(LocationDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return LocationViewHolder(
            LocationListItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as LocationViewHolder).bind(getItem(position))
    }

    class LocationViewHolder(
        private val binding: LocationListItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Location) = binding.apply {
            location = item
            locationCoordinate.text = with(item.coordinate) {
                "\uD83C\uDF0E   ${latitude.toString().take(5)}  ×  ${longitude.toString().take(6)}"
            }
            Glide.with(root).load(item.imageId).into(locationImage)
            executePendingBindings()
        }
    }
}

private class LocationDiffCallback : DiffUtil.ItemCallback<Location>() {

    override fun areItemsTheSame(oldItem: Location, newItem: Location): Boolean {
        return oldItem.name == newItem.name
    }

    override fun areContentsTheSame(oldItem: Location, newItem: Location): Boolean {
        return oldItem == newItem
    }
}