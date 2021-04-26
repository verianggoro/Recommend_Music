package com.pembelajar.musicrecommendation.model

import com.google.gson.annotations.SerializedName

data class Player(
    @SerializedName("notifyUrl") var notifyUrl: String? = null,
    @SerializedName("streamingUrl") var streamUrl : String? = null,
    @SerializedName("songId") var songId : Long = 0
)

data class MetaDataSong(
    @SerializedName("songName") var songName : String? = null,
    @SerializedName("artistName") var artistName : String? = null,
    @SerializedName("albumName") var albumName : String? = null
)
