package com.example.applemusicfeed

import java.io.Serializable

data class Album(
    val name: String,
    val artistName: String,
    val artworkUrl: String,
    val copyright: String,
    val genres: List<String>,
    val id: String,
    val releaseDate: String) : Serializable {

}