package com.kaplan.musicexplore.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.map
import com.kaplan.musicexplore.data.Result.Status.ERROR
import com.kaplan.musicexplore.data.Result.Status.SUCCESS
import kotlinx.coroutines.Dispatchers

fun <T, A> resultLiveData(databaseQuery: () -> LiveData<T>,
                          networkCall: suspend () -> Result<A>,
                          saveCallResult: suspend (A) -> Unit): LiveData<Result<T>> =
    liveData(Dispatchers.IO) {
        emit(Result.loading<T>())
        val source = databaseQuery.invoke().map { Result.success(it) }
        emitSource(source)

        val responseStatus = networkCall.invoke()
        if (responseStatus.status == SUCCESS) {
            saveCallResult(responseStatus.data!!)
        } else if (responseStatus.status == ERROR) {
            emit(Result.error<T>(responseStatus.message!!))
            emitSource(source)
        }
    }

fun <T> resultDbData(databaseQuery: () -> LiveData<T>): LiveData<Result<T>> =
    liveData(Dispatchers.IO) {
        emit(Result.loading<T>())
        val source = databaseQuery.invoke().map {
            Result.success(it)
        }
        emitSource(source)
    }

fun <A> resultNetworkData(networkCall: suspend () -> Result<A>): LiveData<Result<A>> =
    liveData(Dispatchers.IO) {
        emit(Result.loading<A>())
        val responseStatus = networkCall.invoke()
        if (responseStatus.status == Result.Status.SUCCESS) {
            emit(Result.success(responseStatus.data!!))
        } else if (responseStatus.status == Result.Status.ERROR) {
            emit(Result.error<A>(responseStatus.message!!))
        }
    }

fun <A> resultNetworkDataAndSave(networkCall: suspend () -> Result<A>,
                                 saveCallResult: suspend (A) -> Unit): LiveData<Result<A>> =
    liveData(Dispatchers.IO) {
        emit(Result.loading<A>())
        val responseStatus = networkCall.invoke()
        if (responseStatus.status == Result.Status.SUCCESS) {
            saveCallResult(responseStatus.data!!)
            emit(Result.success(responseStatus.data!!))
        } else if (responseStatus.status == Result.Status.ERROR) {
            emit(Result.error<A>(responseStatus.message!!))
        }
    }
