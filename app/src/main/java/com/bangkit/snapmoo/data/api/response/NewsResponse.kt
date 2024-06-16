package com.bangkit.snapmoo.data.api.response

import com.google.gson.annotations.SerializedName

data class NewsResponse(

    @field:SerializedName("data")
    val data: List<NewsResult>,

    @field:SerializedName("message")
    val message: String
)

data class NewsResult(

    @field:SerializedName("Penerbit Berita")
    val author: String? = null,

    @field:SerializedName("Link Berita")
    val link: String? = null,

    @field:SerializedName("Judul Berita")
    val title: String? = null,

    @field:SerializedName("Tanggal Berita")
    val publishedAt: PublishedAtNewsResult? = null,

    @field:SerializedName("Link Logo Penerbit Berita")
    val authorImage: String? = null,

    @field:SerializedName("Link Gambar Berita")
    val thumbnail: String? = null
)

data class PublishedAtNewsResult(

    @field:SerializedName("_nanoseconds")
    val nanoseconds: Long? = null,

    @field:SerializedName("_seconds")
    val seconds: Long? = null
)



