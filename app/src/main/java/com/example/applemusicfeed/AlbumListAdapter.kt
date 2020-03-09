package com.example.applemusicfeed

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

class AlbumListAdapter(context: Context, albumList: List<Album>) :
    RecyclerView.Adapter<AlbumListAdapter.AlbumViewHolder>() {

    private val mAlbumList: List<Album> = albumList
    private val mInflater: LayoutInflater = LayoutInflater.from(context)

    inner class AlbumViewHolder(itemView: View, adapter: AlbumListAdapter) :
        RecyclerView.ViewHolder(itemView) {

        val albumNameView: TextView = itemView.findViewById(R.id.grid_album_name)
        val albumArtistView: TextView = itemView.findViewById(R.id.grid_artist_name)
        val albumImageView: ImageView = itemView.findViewById(R.id.grid_album_artwork)
        val mAdapter: AlbumListAdapter = adapter
    }

    private fun shortenedName(name: String): String {
        return if (name.length > 30) name.substring(0 until 30) + "..." else name
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlbumViewHolder {
        val mItemView: View = mInflater.inflate(R.layout.albumgrid_item, parent, false)
        return AlbumViewHolder(mItemView, this)
    }

    override fun getItemCount(): Int {
        return mAlbumList.size
    }

    override fun onBindViewHolder(holder: AlbumViewHolder, position: Int) {
        val mCurrent: Album = mAlbumList[position]

        holder.albumNameView.text = shortenedName(mCurrent.name)
        holder.albumArtistView.text = shortenedName(mCurrent.artistName)

        Picasso
            .get()
            .load(mCurrent.artworkUrl)
            .error(R.drawable.ic_album_artwork_error_foreground)
            .resize(500, 500)
            .transform(RoundedCornersTransform())
            .into(holder.albumImageView)


        holder.albumImageView.setOnClickListener {
            val context = holder.albumImageView.context
            val intent = Intent(context, DisplayAlbumActivity::class.java).apply {
                putExtra("album", mCurrent)
            }
            context.startActivity(intent)
        }
    }

}