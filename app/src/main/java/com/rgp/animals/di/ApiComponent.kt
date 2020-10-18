package com.rgp.animals.di

import com.rgp.animals.model.AnimalApiService
import dagger.Component

@Component(modules = [ApiModule::class])
interface ApiComponent {
    fun inject(service: AnimalApiService)
}