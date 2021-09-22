package ru.brauer.nearspace.domain.repository

import ru.brauer.nearspace.data.repository.CallbackApod

interface Repository {
    fun getApod(date:String?, callback: CallbackApod)
}