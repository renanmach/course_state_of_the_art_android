package com.rgp.animals.di

import com.rgp.animals.viewmodel.ListViewModel
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [ApiModule::class, PrefsModel::class, AppModule::class])
interface ViewModelComponent {
    fun inject(viewModel: ListViewModel)
}