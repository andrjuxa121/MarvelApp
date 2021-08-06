package com.great.testapp.model

data class Character (
    var id: Int? = null,
    var name: String? = null,
    var description: String? = null,
    var thumbnail: Image? = null,
    var comics: ComicList? = null
)