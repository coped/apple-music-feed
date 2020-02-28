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

class AlbumListAdapter(context: Context, albumList: MutableList<Map<String, String>>) :
                                RecyclerView.Adapter<AlbumListAdapter.AlbumViewHolder>() {

    private val mAlbumList: MutableList<Map<String, String>> = albumList
    private val mInflater: LayoutInflater = LayoutInflater.from(context)

    inner class AlbumViewHolder(itemView: View, adapter: AlbumListAdapter) :
                                            RecyclerView.ViewHolder(itemView) {

        val albumTextView: TextView = itemView.findViewById(R.id.grid_album_name)
        val albumImageView: ImageView = itemView.findViewById(R.id.grid_album_artwork)
        val mAdapter: AlbumListAdapter = adapter
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlbumViewHolder {
        val mItemView: View = mInflater.inflate(R.layout.albumgrid_item, parent, false)
        return AlbumViewHolder(mItemView, this)
    }

    override fun getItemCount(): Int {
        return mAlbumList.size
    }

    override fun onBindViewHolder(holder: AlbumViewHolder, position: Int) {
        val mCurrent: Map<String, String> = mAlbumList[position]
        holder.albumTextView.text = mCurrent["name"]
        Picasso
            .get()
            .load(mCurrent["artworkUrl"])
            .error(R.drawable.ic_album_artwork_error_foreground)
            .resize(500, 500)
            .into(holder.albumImageView)


        holder.albumImageView.setOnClickListener {
            val context = holder.albumImageView.context
            val intent = Intent(context, DisplayAlbumActivity::class.java).apply {
                putExtra("artistName", mCurrent["artistName"])
                putExtra("name", mCurrent["name"])
                putExtra("artworkUrl", mCurrent["artworkUrl"])
                putExtra("genres", mCurrent["genres"])
                putExtra("releaseDate", mCurrent["releaseDate"])
                putExtra("copyright", mCurrent["copyright"])
            }
            context.startActivity(intent)
        }
    }

}