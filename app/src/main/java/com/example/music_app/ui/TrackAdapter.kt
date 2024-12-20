package com.example.music_app.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.music_app.R
import com.example.music_app.model.Track

class TrackAdapter(
    private val tracks: List<Track>,
    private val onRatingChanged: (Track, Float) -> Unit
) : RecyclerView.Adapter<TrackAdapter.TrackViewHolder>() {

    class TrackViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val albumCover: ImageView = view.findViewById(R.id.albumCover)
        val trackTitle: TextView = view.findViewById(R.id.trackTitle)
        val artistName: TextView = view.findViewById(R.id.artistName)
        val ratingBar: RatingBar = view.findViewById(R.id.ratingBar)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_track, parent, false)
        return TrackViewHolder(view)
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        val track = tracks[position]
        holder.trackTitle.text = track.name
        holder.artistName.text = track.artists.joinToString(", ") { it.name }


        Glide.with(holder.itemView.context)
            .load(track.albumCoverUrl ?: R.drawable.ic_placeholder)
            .into(holder.albumCover)


        holder.ratingBar.rating = track.rating


        holder.ratingBar.setOnRatingBarChangeListener { _, rating, _ ->
            track.rating = rating
            onRatingChanged(track, rating)
        }


        holder.itemView.setOnClickListener {

        }
    }

    override fun getItemCount(): Int = tracks.size
}
