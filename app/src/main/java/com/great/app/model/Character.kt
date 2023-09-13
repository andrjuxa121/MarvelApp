package com.great.app.model

data class Character (
    var id: Int = 0,
    var name: String = "No data",
    var description: String = "No data",
    var thumbnail: Image = Image(),
    var comics: ComicList = ComicList()
)