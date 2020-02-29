package com.example.applemusicfeed

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso

class DisplayAlbumActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_display_album)

        val album: Map<String, String> = mapOf(
            "artworkUrl"  to intent.getStringExtra("artworkUrl"),
            "name"        to intent.getStringExtra("name"),
            "artistName"  to intent.getStringExtra("artistName"),
            "releaseDate" to intent.getStringExtra("releaseDate"),
            "genres"      to intent.getStringExtra("genres"),
            "copyright"   to intent.getStringExtra("copyright")
        )

        findViewById<ImageView>(R.id.display_album_artwork).apply {
            Picasso
                .get()
                .load(album["artworkUrl"])
                .error(R.drawable.ic_album_artwork_error_foreground)
                .resize(1000, 1000)
                .into(this)
        }

        val name = findViewById<TextView>(R.id.display_album_name)
        name.text = album["name"]

        val albumBy = findViewById<TextView>(R.id.display_album_by)
        albumBy.text = resources.getString(R.string.album_by, album["artistName"], album["releaseDate"])

        val genres = findViewById<TextView>(R.id.display_album_genres)
        genres.text = resources.getString(R.string.album_genres, album["genres"])

        val copyright = findViewById<TextView>(R.id.display_album_copyright)
        copyright.text = album["copyright"]
    }
}
