package ru.brauer.nearspace.domain.repository

import ru.brauer.nearspace.domain.entities.Apod

interface Repository {
    fun <T>getData(date:String?, callback: Callback<T>)
}