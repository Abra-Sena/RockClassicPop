package com.emissa.apps.rockclassicpop.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.emissa.apps.rockclassicpop.MusicApp
import com.emissa.apps.rockclassicpop.R
import com.emissa.apps.rockclassicpop.data.MusicItemClicked
import com.emissa.apps.rockclassicpop.model.Pop
import com.emissa.apps.rockclassicpop.model.PopSongs
import com.emissa.apps.rockclassicpop.model.Rock
import com.emissa.apps.rockclassicpop.model.RockSongs
import com.squareup.picasso.Picasso

class RockAdapter(
    private val rockSongClickListener: MusicItemClicked,
    private val rocks: MutableList<Rock> = mutableListOf()
) : RecyclerView.Adapter<RockViewHolder>() {

    fun updatePopSongs(rockSongs: List<Rock>) {
        rocks.clear()
        rocks.addAll(rockSongs)
        notifyItemRangeChanged(0, itemCount)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RockViewHolder {
        val musicItem = LayoutInflater
            .from(parent.context).inflate(R.layout.music_item, parent, false)
        return RockViewHolder(musicItem, rockSongClickListener)
    }

    override fun onBindViewHolder(holder: RockViewHolder, position: Int) {
        val musicItem = rocks[position]
        holder.bind(musicItem)
    }

    override fun getItemCount(): Int = rocks.size
}

class RockViewHolder(
    itemView: View,
    private val itemClicked: MusicItemClicked
): RecyclerView.ViewHolder(itemView) {
    /**
     * DATA TO LOAD:
     * artistName
     * collectionName
     * artworkUrl60
     * trackPrice
     *
     * on click, play song (url to load on click is 'previewURL) using implicit intents
     */
    private val rockImage: ImageView = itemView.findViewById(R.id.music_photo)
    private val rockTitle: TextView = itemView.findViewById(R.id.music_title)
    private val rockArtist: TextView = itemView.findViewById(R.id.music_artist)
    private val rockPrice: TextView = itemView.findViewById(R.id.music_price)


    fun bind(musicItem: Rock) {
        val price = musicItem.trackPrice.toString()

        rockTitle.text = musicItem.collectionName
        rockArtist.text = musicItem.artistName
        rockPrice.text = MusicApp.priceFormatted(price)

        Picasso.get()
            .load(musicItem.artworkUrl60)
            .placeholder(R.drawable.ic_baseline_camera_loading)
            .error(R.drawable.ic_baseline_broken_image_error)
            .resize(250, 250)
//            .fit()
            .into(rockImage)

        itemView.setOnClickListener {
            musicItem.previewUrl?.let { it1 -> itemClicked.onSongClicked(it1) }
        }
    }
}