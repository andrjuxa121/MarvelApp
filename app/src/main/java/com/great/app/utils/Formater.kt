package com.great.app.utils

import com.great.app.model.Image

class Formater {
    companion object {
        fun getImageUrl(image: Image?): String {
            val imagePath = image?.path?.replace("http", "https")
            return "$imagePath.${image?.extension}"
        }
    }
}