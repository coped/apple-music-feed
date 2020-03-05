package com.example.applemusicfeed

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso

class DisplayAlbumActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_display_album)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val album: Album = intent.getSerializableExtra("album") as Album

        findViewById<ImageView>(R.id.display_album_artwork).apply {
            Picasso
                .get()
                .load(album.artworkUrl)
                .error(R.drawable.ic_album_artwork_error_foreground)
                .resize(1000, 1000)
                .into(this)
        }

        val name = findViewById<TextView>(R.id.display_album_name)
        name.text = album.name

        val albumBy = findViewById<TextView>(R.id.display_album_by)
        albumBy.text = resources.getString(R.string.album_by, album.artistName, album.releaseDate)

        val genres = findViewById<TextView>(R.id.display_album_genres)
        genres.text = resources.getString(
            R.string.album_genres,
            album.genres.joinToString(separator = ", ")
        )

        val copyright = findViewById<TextView>(R.id.display_album_copyright)
        copyright.text = album.copyright
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }
}
