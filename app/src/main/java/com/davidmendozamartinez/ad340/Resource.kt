package com.davidmendozamartinez.ad340

data class Resource<out T>(val status: Status, val data: T?, val errorId: Int?) {
    companion object {
        fun loading() = Resource(
            status = Status.LOADING,
            data = null,
            errorId = null
        )

        fun <T> success(data: T) =
            Resource(
                status = Status.SUCCESS,
                data = data,
                errorId = null
            )

        fun error(errorId: Int? = null) =
            Resource(
                status = Status.ERROR,
                data = null,
                errorId = errorId
            )
    }
}

enum class Status {
    LOADING,
    SUCCESS,
    ERROR
}