package com.gneo.fgurbanov.junctionhealth.utils

sealed class DState<T> {
    abstract val data: T?

    data class Success<T>(
        override val data: T
    ) : DState<T>()

    data class Loading<T>(
        val errorState: LoadingState = LoadingState.DEFAULT,
        override val data: T? = null
    ) : DState<T>()

    data class None<T>(
        override val data: T? = null
    ) : DState<T>()

    data class Error<T>(
        override val data: T? = null,
        val description: String
    ) : DState<T>()

    abstract class Update<T>(
        override val data: T? = null
    ) : DState<T>()
}

enum class LoadingState {
    DEFAULT,
    PAGINATION,
    PULL
}