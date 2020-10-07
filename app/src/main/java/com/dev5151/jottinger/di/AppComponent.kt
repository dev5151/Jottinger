package com.dev5151.jottinger.di

import android.app.Application
import com.dev5151.jottinger.BaseApplication
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import javax.inject.Singleton


@Singleton
@Component(
        modules = [
            AndroidInjectionModule::class,
            ActivityBuildersModule::class,
            AppModule::class,
            ViewModelFactoryModule::class
        ]
)
interface AppComponent : AndroidInjector<BaseApplication> {

    //We have to instruct the component how to build itself. We do this with the @Component.Builder annotation.

    @Component.Builder
    interface Builder {

        // Method #1
        fun build(): AppComponent

        // Method #2
        @BindsInstance
        fun application(application: Application): Builder
    }
}