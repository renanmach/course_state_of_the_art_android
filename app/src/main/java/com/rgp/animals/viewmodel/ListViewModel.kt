package com.rgp.animals.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.rgp.animals.di.AppModule
import com.rgp.animals.di.DaggerViewModelComponent
import com.rgp.animals.model.Animal
import com.rgp.animals.model.AnimalApiService
import com.rgp.animals.model.ApiKey
import com.rgp.animals.util.SharedPreferencesHelper
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class ListViewModel(application: Application): AndroidViewModel(application) {
    val animals by lazy {MutableLiveData<List<Animal>>()}
    val loadError by lazy {MutableLiveData<Boolean>()}
    val loading by lazy {MutableLiveData<Boolean>()}

    @Inject
    lateinit var prefs: SharedPreferencesHelper

    // collects links and clears them
    // avoids memory leaks
    private val disposable = CompositeDisposable()

    @Inject
    lateinit var apiService: AnimalApiService

    private var invalidApiKey = false

    init {
        DaggerViewModelComponent.builder()
            .appModule(AppModule(getApplication()))
            .build()
            .inject(this)
    }

    fun refresh() {
        invalidApiKey = false
        loading.value = true
        val key = prefs.getApiKey()
        if(key.isNullOrEmpty()) {
            getKey()
        } else {
            getAnimals(key)
        }
    }

    private fun getKey() {
        // observe Singles
        disposable.add(

            apiService.getApiKey()
                // do the operation on a background thread
                .subscribeOn(Schedulers.newThread())
                    // return the result to the mainthread
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object: DisposableSingleObserver<ApiKey>() {
                    override fun onSuccess(response: ApiKey) {
                        if(response.key.isNullOrEmpty()) {
                            loadError.value = true
                            loading.value = false
                        } else {
                            prefs.saveApiKey(response.key)
                            getAnimals(response.key)
                        }
                    }

                    override fun onError(e: Throwable) {
                        if(!invalidApiKey) {
                            invalidApiKey = true
                            getKey()
                        } else {
                            e.printStackTrace()
                            loadError.value = true
                            loading.value = false
                        }
                    }
                })
        )
    }
    
    fun hardRefresh() {
        loading.value = true
        getKey()
    }

    private fun getAnimals(key: String) {
        disposable.add(
            apiService.getAnimals(key)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object: DisposableSingleObserver<List<Animal>>() {
                    override fun onSuccess(list: List<Animal>) {
                        loadError.value = false
                        animals.value = list
                        loading.value = false
                    }

                    override fun onError(e: Throwable) {
                        e.printStackTrace()
                        loading.value = false
                        animals.value = null
                        loadError.value = true
                    }
                })
        )
    }

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }

}