package com.pembelajar.musicrecommendation.model

import com.google.gson.annotations.SerializedName

data class Song(
    @SerializedName("user_id") var userId: Int = 0,
    @SerializedName("mae") var mae: Double = 0.0,
    @SerializedName("rmse") var rmse: Double = 0.0,
    @SerializedName("recommend_list") var dataList: List<DataList>? = null
)

data class DataList(
    @SerializedName("song_id") var songId : String? = null,
    @SerializedName("song_name") var songName : String? = null,
    @SerializedName("accurate") var accurate : Double = 0.0
)