package com.example.applemusicfeed

import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.*
import org.json.JSONObject

class MainActivity : AppCompatActivity() {
    private var mAlbumList: MutableList<Map<String, String>> = mutableListOf()

    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mAdapter: AlbumListAdapter

    private fun topAlbumsUrl(n: Int): String =
        "https://rss.itunes.apple.com/api/v1/us/apple-music/top-albums/all/$n/non-explicit.json"

    private fun sendRequest(url: String): String? {
        try {
            val client = OkHttpClient()
            val request: Request = Request.Builder()
                .url(url)
                .build()
            val response: Response = client.newCall(request).execute()
            return response.body!!.string()
        } catch(e: Throwable) {
            Log.d("sendRequest_thrown", e.toString())
            return null
        }
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

    // Retrieve album data from RSS Feed Generator API and parse JSON to map
    inner class HTTPAsyncTask : AsyncTask<String, Void, String>() {
        override fun doInBackground(vararg urls: String): String? {
            return sendRequest(urls[0])
        }
        override fun onPostExecute(result: String?) {
            if (result != null) {
                mAlbumList = parseAlbumsFromResponse(result)

                mRecyclerView = findViewById(R.id.recyclerview)
                mAdapter = AlbumListAdapter(this@MainActivity, mAlbumList)
                mRecyclerView.adapter = mAdapter
                mRecyclerView.layoutManager = GridLayoutManager(this@MainActivity, 2)
            } else {
                findViewById<TextView>(R.id.error_message).apply {
                    text = resources.getText(R.string.error_message)
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        HTTPAsyncTask().execute(topAlbumsUrl(n = 25))
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
            R.id.top_25 -> true
            else -> super.onOptionsItemSelected(item)
        }
    }
}
