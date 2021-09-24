package ru.brauer.nearspace.presentation.main

import android.util.Log
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.brauer.nearspace.data.repository.RepositoryImpl
import ru.brauer.nearspace.domain.entities.Apod
import ru.brauer.nearspace.domain.interactor.ApodInteractor
import ru.brauer.nearspace.domain.interactor.ApodInteractorImpl
import kotlin.random.Random

class PhotoOfDayViewModel : ViewModel() {

    private val interactor: ApodInteractor = ApodInteractorImpl(RepositoryImpl())
    private val cachedData: MutableMap<Long?, MutableLiveData<PhotoOfDayAppState>> = mutableMapOf()

    private val id = Random.nextInt(1000)

    fun observe(lifecycleOwner: LifecycleOwner, renderData: RenderData, date: Long?) {
        Log.i("PhotoOfDayViewModel", id.toString())
        if (!cachedData.containsKey(date)) {
            cachedData += date to MutableLiveData<PhotoOfDayAppState>()
        }
        cachedData[date]?.observe(lifecycleOwner, { renderData.renderData(cachedData[date]?.value) })
        if (cachedData[date]?.value !is PhotoOfDayAppState.Success) {
            getPhotoOfDay(date)
        }
    }

    fun getPhotoOfDay(date: Long?) {
        cachedData[date]?.value = PhotoOfDayAppState.Loading
        interactor.getApod(date, object : ApodInteractor.CallbackApod {
            override fun notify(apod: Apod?, onServerFailureMessage: String?, ex: Throwable?) {
                cachedData[date]?.value = apod?.let { PhotoOfDayAppState.Success(it) }
                    ?: onServerFailureMessage?.let {
                        PhotoOfDayAppState.Error(IllegalStateException(it))
                    } ?: ex?.let { PhotoOfDayAppState.Error(it) }
            }
        })
    }

    interface RenderData {
        fun renderData(appState: PhotoOfDayAppState?)
    }
}