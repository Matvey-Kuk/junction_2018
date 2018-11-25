package com.gneo.fgurbanov.junctionhealth.utils

import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File


fun File.multipartFromData(name: String): MultipartBody.Part {
    val requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), this)
    return MultipartBody.Part.createFormData("file", this.name, requestFile)
}