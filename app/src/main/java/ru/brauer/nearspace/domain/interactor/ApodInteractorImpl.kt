package ru.brauer.nearspace.domain.interactor

import ru.brauer.nearspace.domain.repository.Callback
import ru.brauer.nearspace.domain.entities.Apod
import ru.brauer.nearspace.domain.repository.Repository
import ru.brauer.nearspace.util.toFormate

class ApodInteractorImpl(private val repository: Repository) : ApodInteractor {
    override fun getApod(date: Long?, callbackApod: ApodInteractor.CallbackApod) {

        val callback = object : Callback<Apod> {
            override fun notify(response: Apod?, onServerFailureMessage: String?) {
                callbackApod.notify(response, onServerFailureMessage, null)
            }

            override fun notify(ex: Throwable) {
                callbackApod.notify(null, null, ex)
            }
        }

        repository.getData(date?.toFormate("yyyy-MM-dd"), callback)
    }
}