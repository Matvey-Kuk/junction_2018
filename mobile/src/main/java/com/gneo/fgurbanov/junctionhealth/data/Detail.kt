package com.gneo.fgurbanov.junctionhealth.data

data class Detail(
    val id: Int,
    val proceed: Boolean,
    val renderedVideo: String?,
    val videofile: String,
    val mistakes: List<Mistake>
)

data class Mistake(
    val title: String
)