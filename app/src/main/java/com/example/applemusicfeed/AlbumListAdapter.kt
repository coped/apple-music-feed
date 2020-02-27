package com.example.applemusicfeed

import android.content.Context
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

        val albumItemView: TextView = itemView.findViewById(R.id.album)
        val albumImageView: ImageView = itemView.findViewById(R.id.album_image)
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
        holder.albumItemView.text = mCurrent["name"]
        Picasso
            .get()
            .load(mCurrent["artworkUrl"])
            .resize(500, 500)
            .into(holder.albumImageView)
    }

}