package ru.brauer.nearspace.domain.repository

interface Repository {
    fun getData(date:String?, callbackApod: CallbackApod)
}