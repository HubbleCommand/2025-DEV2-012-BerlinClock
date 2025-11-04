package com.dev2012.berlinclock

import android.app.Application
import com.dev2012.berlinclock.ui.BerlinClockViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin
import org.koin.dsl.module
import org.koin.core.module.dsl.viewModel

val appModule = module {
    viewModel { BerlinClockViewModel() }
}

class BerlinClockApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@BerlinClockApplication)
            modules(appModule)
        }
    }
}