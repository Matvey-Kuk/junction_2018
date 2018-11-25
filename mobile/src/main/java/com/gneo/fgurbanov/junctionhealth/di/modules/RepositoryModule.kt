package com.gneo.fgurbanov.junctionhealth.di.modules

import com.gneo.fgurbanov.junctionhealth.data.DataRepository
import com.gneo.fgurbanov.junctionhealth.data.DataRepositoryImpl
import dagger.Module
import dagger.Provides
import javax.inject.Singleton


@Module
class RepositoryModule {


    @Provides
    @Singleton
    fun provideMainRepository(dataRepository: DataRepositoryImpl): DataRepository = dataRepository
}