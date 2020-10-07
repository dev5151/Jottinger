package com.dev5151.jottinger.di

import com.dev5151.jottinger.ui.activities.MainActivity
import com.dev5151.jottinger.ui.activities.NoteActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBuildersModule {


    @ContributesAndroidInjector(modules = [ViewModelModule::class])
    abstract fun noteActivity(): NoteActivity

    @ContributesAndroidInjector(modules = [ViewModelModule::class])
    abstract fun mainActivity(): MainActivity
}