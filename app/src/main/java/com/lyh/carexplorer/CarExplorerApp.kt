package com.lyh.carexplorer

import android.app.Application
import coil.ImageLoader
import coil.ImageLoaderFactory
import coil.disk.DiskCache
import coil.memory.MemoryCache
import com.lyh.carexplorer.data.di.getDataModule
import com.lyh.carexplorer.domain.di.domainModule
import com.lyh.carexplorer.feature.car.di.featureCarModule
import com.lyh.carexplorer.feature.core.R
import com.lyh.carexplorer.feature.user.di.featureUserModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import timber.log.Timber

class CarExplorerApp : Application(), ImageLoaderFactory {

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        startKoin {
            androidContext(this@CarExplorerApp)
            modules(
                getDataModule(BuildConfig.DEBUG),
                domainModule,
                featureCarModule,
                featureUserModule
            )
        }
    }

    override fun newImageLoader(): ImageLoader {
        return ImageLoader.Builder(this)
            .placeholder(R.drawable.ic_no_image)
            .error(R.drawable.ic_no_image)
            .crossfade(true)
            .memoryCache {
                MemoryCache.Builder(this)
                    .maxSizePercent(0.25)
                    .build()
            }
            .diskCache {
                DiskCache.Builder()
                    .directory(this.cacheDir.resolve("image_cache"))
                    .maxSizePercent(0.02)
                    .build()
            }
            .build()
    }
}
