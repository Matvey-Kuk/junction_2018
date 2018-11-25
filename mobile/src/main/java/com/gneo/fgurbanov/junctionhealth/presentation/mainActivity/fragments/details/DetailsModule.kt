package com.gneo.fgurbanov.junctionhealth.presentation.mainActivity.fragments.details

import com.gneo.fgurbanov.junctionhealth.utils.viewModel
import dagger.Lazy
import dagger.Module
import dagger.Provides
import dagger.Reusable

@Module
abstract class DetailsModule {

    @Module
    companion object {

        @Provides
        @JvmStatic
        @Reusable
        fun provideConnectionViewModel(
            fragment: DetailsFragment,
            viewModel: Lazy<DetailsViewModelImpl>
        ): DetailsViewModel = fragment.viewModel { viewModel.get() }
    }
}