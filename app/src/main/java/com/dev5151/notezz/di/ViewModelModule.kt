package com.dev5151.notezz.di

import androidx.lifecycle.ViewModel
import com.dev5151.notezz.NoteViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(NoteViewModel::class)
    internal abstract fun bindMyViewModel(viewModel: NoteViewModel): ViewModel
}