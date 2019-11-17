package cz.beesli.cleverlancetest.data.model

import com.squareup.moshi.Json

data class ImageEntity(@field:Json(name = "image") val base64data : String)