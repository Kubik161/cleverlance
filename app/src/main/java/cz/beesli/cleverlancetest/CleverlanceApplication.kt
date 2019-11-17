package cz.beesli.cleverlancetest;

import android.app.Application;
import cz.beesli.cleverlancetest.data.LoginRepository
import cz.beesli.cleverlancetest.network.CleverlanceServiceApi
import cz.beesli.cleverlancetest.ui.login.LoginViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module
import timber.log.Timber

class CleverlanceApplication : Application() {

    var listOfModules = module {

        single { CleverlanceServiceApi() }

        single { LoginRepository(get()) }

        viewModel { LoginViewModel(get()) }
    }

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@CleverlanceApplication)
            modules(listOfModules)
        }

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}
