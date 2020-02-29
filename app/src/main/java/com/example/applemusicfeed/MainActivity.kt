package com.example.applemusicfeed

import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar

import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.*
import org.json.JSONObject
import java.lang.ref.WeakReference

class MainActivity : AppCompatActivity() {
    private var mAlbumList: MutableList<Map<String, String>> = mutableListOf()

    private fun topAlbumsUrl(explicit: Boolean): String {
        val explicitness: String = if (explicit) "explicit" else "non-explicit"
        return "https://rss.itunes.apple.com/api/v1/us/apple-music/top-albums/all/25/$explicitness.json"
    }

    private fun parseAlbumsFromResponse(result: String): MutableList<Map<String, String>> {
        var list: MutableList<Map<String, String>> = mutableListOf()

        val jsonAlbums = JSONObject(result)
            .getJSONObject("feed")
            .getJSONArray("results")

        for (i in (0 until jsonAlbums.length())) {
            val a = jsonAlbums.getJSONObject(i)

            val genreList: MutableList<String> = mutableListOf()
            for (i in (0 until a.getJSONArray("genres").length())) {
                genreList.add(i, a
                    .getJSONArray("genres")
                    .getJSONObject(i)
                    .getString("name")
                )
            }

            val albumInfo: Map<String, String> = mapOf(
                "artistName"  to a.getString("artistName"),
                "id"          to a.getString("id"),
                "releaseDate" to a.getString("releaseDate"),
                "name"        to a.getString("name"),
                "copyright"   to a.getString("copyright"),
                "artworkUrl"  to a.getString("artworkUrl100"),
                "genres"      to genreList.joinToString(", ")
            )
            list.add(albumInfo)
        }
        return list
    }

    private fun sendRequest(url: String): String {
        val client = OkHttpClient()
        val request: Request = Request.Builder()
            .url(url)
            .build()
        val response: Response = client.newCall(request).execute()
        return response.body!!.string()
    }


    // Asynchronously retrieve album data
    private inner class GetAlbums : AsyncTask<String, Void, String>() {
        private val recyclerView = findViewById<RecyclerView>(R.id.recycler_view)

        override fun onPreExecute() {
            Snackbar
                .make(recyclerView, R.string.loading_message, Snackbar.LENGTH_SHORT)
                .show()
        }

        override fun doInBackground(vararg urls: String): String? {
            // Catch exceptions when fetching and parsing album data
            return try {
                mAlbumList = parseAlbumsFromResponse(sendRequest(urls[0]))
                "success"
            } catch(e: Throwable) {
                Log.d("doInBackground_thrown", e.toString())
                null
            }
        }

        override fun onPostExecute(result: String?) {
            // Display albums or show error message based on result
            if (result != null) {
                recyclerView.apply {
                    adapter = AlbumListAdapter(context, mAlbumList)
                    layoutManager = GridLayoutManager(context, 2)
                }
            } else {
                Snackbar
                    .make(recyclerView, R.string.error_message, Snackbar.LENGTH_LONG)
                    .show()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        GetAlbums().execute(topAlbumsUrl(explicit = false))
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.non_explicit -> {
                GetAlbums().execute(topAlbumsUrl(explicit = false))
                return true
            }
            R.id.explicit -> {
                GetAlbums().execute(topAlbumsUrl(explicit = true))
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
