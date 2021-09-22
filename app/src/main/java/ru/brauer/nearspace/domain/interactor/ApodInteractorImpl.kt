package ru.brauer.nearspace.domain.interactor

import ru.brauer.nearspace.data.repository.CallbackApod
import ru.brauer.nearspace.data.repository.RepositoryImpl
import ru.brauer.nearspace.domain.entities.Apod
import ru.brauer.nearspace.domain.repository.Repository
import ru.brauer.nearspace.presentation.util.toFormate

class ApodInteractorImpl(private val repository: Repository = RepositoryImpl()) : ApodInteractor {
    override fun getApod(date: Long?, callbackApod: ApodInteractor.CallbackApod) {

        val callback = object : CallbackApod {
            override fun notify(response: Apod?, onServerFailureMessage: String?) {
                callbackApod.notify(response, onServerFailureMessage, null)
            }

            override fun notify(ex: Throwable) {
                callbackApod.notify(null, null, ex)
            }
        }

        repository.getApod(date?.toFormate("yyyy-MM-dd"), callback)
    }
}