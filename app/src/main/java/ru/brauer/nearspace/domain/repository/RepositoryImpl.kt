package ru.brauer.nearspace.domain.repository

import retrofit2.Callback
import ru.brauer.nearspace.domain.repository.dto.ApodDTO

class RepositoryImpl(private val remoteDataSource: RemoteDataSource = RemoteDataSource()) :
    Repository {

    override fun getApod(callback: Callback<ApodDTO>) {
        remoteDataSource.getApod(callback)
    }
}