package com.pembelajar.musicrecommendation.model

import com.google.gson.annotations.SerializedName

data class Detail(
    @SerializedName("detail") var detail: String? = null,
    @SerializedName("status") var status: String? = null,
    @SerializedName("version") var versions: String? = null
)