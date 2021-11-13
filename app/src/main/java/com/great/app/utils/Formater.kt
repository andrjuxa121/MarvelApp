package com.great.app.utils

import com.great.app.model.Image

class Formater {
    companion object {
        fun getImageUrl(image: Image?): String {
            return "${image?.path}.${image?.extension}"
        }
    }
}