package com.emissa.apps.rockclassicpop.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.emissa.apps.rockclassicpop.R
import com.emissa.apps.rockclassicpop.model.Pop
import com.squareup.picasso.Picasso

class PopAdapter(
    private val pops: MutableList<Pop> = mutableListOf(),
    private val popSongClickListener: (Pop) -> Unit
) : RecyclerView.Adapter<PopViewHolder>() {

    fun updatePopSongs(popSongs: List<Pop>) {
        pops.clear()
        pops.addAll(popSongs)
        notifyItemRangeChanged(0, itemCount)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PopViewHolder {
        val musicItem = LayoutInflater
            .from(parent.context).inflate(R.layout.music_item, parent, false)

        return PopViewHolder(musicItem, popSongClickListener)
    }

    override fun onBindViewHolder(holder: PopViewHolder, position: Int) {
        val musicItem = pops[position]
        holder.bind(musicItem)
    }

    override fun getItemCount(): Int = pops.size
}

class PopViewHolder(
    itemView: View,
    private val itemClicked: (Pop) -> Unit
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
    private val popImage: ImageView = itemView.findViewById(R.id.music_photo)
    private val popTitle: TextView = itemView.findViewById(R.id.music_title)
    private val popArtist: TextView = itemView.findViewById(R.id.music_artist)
    private val popPrice: TextView = itemView.findViewById(R.id.music_price)


    fun bind(musicItem: Pop) {
        popTitle.text = musicItem.collectionName
        popArtist.text = musicItem.artistName
        popPrice.text = musicItem.trackPrice.toString()

        Picasso.get()
            .load(musicItem.artworkUrl60)
            .placeholder(R.drawable.ic_baseline_camera_loading)
            .error(R.drawable.ic_baseline_broken_image_error)
            .fit()
            .into(popImage)

        itemView.setOnClickListener {
            itemClicked.invoke(musicItem)
        }
    }
}