package com.dev5151.notezz.di

import com.dev5151.notezz.ui.activities.MainActivity
import com.dev5151.notezz.ui.activities.NoteActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBuildersModule {


    @ContributesAndroidInjector(modules = [ViewModelModule::class])
    abstract fun noteActivity(): NoteActivity

    @ContributesAndroidInjector(modules = [ViewModelModule::class])
    abstract fun mainActivity(): MainActivity
}