package com.great.testapp.utils

import com.great.testapp.model.Image

class Formater {
    companion object {
        fun getImageUrl(image: Image?): String {
            return "${image?.path}.${image?.extension}"
        }
    }
}