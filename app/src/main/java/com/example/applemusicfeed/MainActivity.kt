package com.example.applemusicfeed

import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem

import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.*
import org.json.JSONArray
import org.json.JSONObject

class MainActivity : AppCompatActivity() {
    private var albumList: MutableList<Map<String, String>> = mutableListOf()

    private fun urlForTop(n: Int): String {
        return "https://rss.itunes.apple.com/api/v1/us/apple-music/coming-soon/all/$n/explicit.json"
    }

    private fun getAlbums(url: String): String {
        val client = OkHttpClient()
        val request: Request = Request.Builder()
            .url(url)
            .build()
        val response: Response = client.newCall(request).execute()
        return response.body!!.string()
    }

    // Retrieve album data from RSS Feed Generator API and parse JSON to map
    inner class HTTPAsyncTask : AsyncTask<String, Void, String>() {
        override fun doInBackground(vararg urls: String): String {
            return getAlbums(urls[0])
        }
        override fun onPostExecute(result: String) {
            val jsonAlbums = JSONObject(result)
                .getJSONObject("feed")
                .getJSONArray("results")
            for (i in (0 until jsonAlbums.length())) {
                val a = jsonAlbums.getJSONObject(i)
                val genreList = a.getJSONArray("genres")
                var genres: MutableList<String> = mutableListOf()
                for (i in (0 until genreList.length())) {
                    genres.add(i, genreList.getJSONObject(i).getString("name"))
                }
                val album: Map<String, String> = mapOf(
                    "artistName"  to a.getString("artistName"),
                    "id"          to a.getString("id"),
                    "releaseDate" to a.getString("releaseDate"),
                    "name"        to a.getString("name"),
                    "copyright"   to a.getString("copyright"),
                    "artworkUrl"  to a.getString("artworkUrl100"),
                    "genres"      to genres.joinToString(", ")
                )
                albumList.add(album)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        HTTPAsyncTask().execute(urlForTop(n = 25))
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
        Log.d("menu-item-dialog", "I've been clicked!")
        return when (item.itemId) {
            R.id.top_25 -> true
            else -> super.onOptionsItemSelected(item)
        }
    }
}
