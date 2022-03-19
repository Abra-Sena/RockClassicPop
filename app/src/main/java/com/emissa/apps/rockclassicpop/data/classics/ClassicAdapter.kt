package com.emissa.apps.rockclassicpop.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.emissa.apps.rockclassicpop.R
import com.emissa.apps.rockclassicpop.model.Classic
import com.emissa.apps.rockclassicpop.model.ClassicSongs
import com.squareup.picasso.Picasso

/**
 *
 */
class ClassicAdapter(
    private val classics: MutableList<Classic> = mutableListOf(),
    private val classicSongClickListener: (Classic) -> Unit
) : RecyclerView.Adapter<ClassicViewHolder>() {

    fun updateClassicSongs(classicSongs: List<Classic>) {
        classics.clear()
        classics.addAll(classicSongs)
        notifyItemRangeChanged(0, itemCount)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClassicViewHolder {
        val musicItem = LayoutInflater
            .from(parent.context).inflate(R.layout.music_item, parent, false)

        return ClassicViewHolder(musicItem, classicSongClickListener)
    }

    override fun onBindViewHolder(holder: ClassicViewHolder, position: Int) {
        val musicItem = classics[position]
        holder.bind(musicItem)
    }

    override fun getItemCount(): Int = classics.size
}

class ClassicViewHolder(
    itemView: View,
    private val itemClicked: (Classic) -> Unit
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
    private val classicImage: ImageView = itemView.findViewById(R.id.music_photo)
    private val classicTitle: TextView = itemView.findViewById(R.id.music_title)
    private val classicArtist: TextView = itemView.findViewById(R.id.music_artist)
    private val classicPrice: TextView = itemView.findViewById(R.id.music_price)


    fun bind(musicItem: Classic) {
        classicTitle.text = musicItem.collectionName
        classicArtist.text = musicItem.artistName
        classicPrice.text = musicItem.trackPrice.toString()

        Picasso.get()
            .load(musicItem.artworkUrl60)
            .placeholder(R.drawable.ic_baseline_camera_loading)
            .error(R.drawable.ic_baseline_broken_image_error)
            .resize(250, 250)
//            .fit()
            .into(classicImage)

        itemView.setOnClickListener {
            itemClicked.invoke(musicItem)
        }
    }

}