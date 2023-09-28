package com.guap.vkr.playlistmaker.di

import android.content.Context.MODE_PRIVATE
import android.media.MediaPlayer
import com.google.gson.Gson
import com.guap.vkr.playlistmaker.search.data.NetworkClient
import com.guap.vkr.playlistmaker.search.data.SearchDataStorage
import com.guap.vkr.playlistmaker.search.data.network.ITunesApi
import com.guap.vkr.playlistmaker.search.data.network.RetrofitNetworkClient
import com.guap.vkr.playlistmaker.search.data.sharedPrefs.SharedPrefsSearchDataStorage
import com.guap.vkr.playlistmaker.settings.data.SettingsDataStorage
import com.guap.vkr.playlistmaker.settings.data.sharedPrefs.SharedPrefSettingsDataStorage
import com.guap.vkr.playlistmaker.utils.SHARED_PREFERENCES
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val dataModule = module {

    factory {
        MediaPlayer()
    }

    single {
        androidContext().getSharedPreferences(
            SHARED_PREFERENCES,
            MODE_PRIVATE
        )
    }

    single<SettingsDataStorage> {
        SharedPrefSettingsDataStorage(sharedPrefs = get())
    }

    single<ITunesApi> {
        Retrofit.Builder()
            .baseUrl("https://itunes.apple.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ITunesApi::class.java)
    }

    single<NetworkClient> {
        RetrofitNetworkClient(apiService = get(), androidContext())
    }

    single<SearchDataStorage> {
        SharedPrefsSearchDataStorage(sharedPref = get(), gson = get())
    }

    factory { Gson() }
}