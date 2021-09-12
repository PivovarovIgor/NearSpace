package ru.brauer.nearspace.domain.repository

import retrofit2.Callback
import ru.brauer.nearspace.domain.repository.dto.ApodDTO

interface Repository {
    fun getApod(date:String?, callback: Callback<ApodDTO>)
}