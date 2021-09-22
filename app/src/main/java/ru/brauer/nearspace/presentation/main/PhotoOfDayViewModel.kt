package ru.brauer.nearspace.presentation.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.brauer.nearspace.domain.entities.Apod
import ru.brauer.nearspace.domain.interactor.ApodInteractor
import ru.brauer.nearspace.domain.interactor.ApodInteractorImpl

class PhotoOfDayViewModel(private val interactor: ApodInteractor = ApodInteractorImpl()) :
    ViewModel() {

    val photoOfDayLiveData: MutableLiveData<PhotoOfDayAppState> = MutableLiveData()

    fun getPhotoOfDay(date: Long?) {
        photoOfDayLiveData.value = PhotoOfDayAppState.Loading
        interactor.getApod(date, object : ApodInteractor.CallbackApod {
            override fun notify(apod: Apod?, onServerFailureMessage: String?, ex: Throwable?) {
                photoOfDayLiveData.value = apod?.let { PhotoOfDayAppState.Success(it) }
                    ?: onServerFailureMessage?.let {
                        PhotoOfDayAppState.Error(IllegalStateException(it))
                    } ?: ex?.let { PhotoOfDayAppState.Error(it) }
            }
        })
    }
}