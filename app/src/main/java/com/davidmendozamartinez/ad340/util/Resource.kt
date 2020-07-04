package com.davidmendozamartinez.ad340.util

data class Resource<out T>(val status: Status, val data: T?, val errorId: Int?) {
    companion object {
        fun loading() = Resource(Status.LOADING, null, null)
        fun <T> success(data: T) = Resource(Status.SUCCESS, data, null)
        fun error(errorId: Int? = null) = Resource(Status.ERROR, null, errorId)
    }
}

enum class Status {
    LOADING,
    SUCCESS,
    ERROR
}