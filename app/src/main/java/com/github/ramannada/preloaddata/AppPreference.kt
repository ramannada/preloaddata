package com.github.ramannada.preloaddata

import android.content.Context

/**
 * Created by labibmuhajir on 2019-07-11.
 * labibmuhajir@yahoo.com
 */
class AppPreference(context: Context) {
    private val preference = context.getSharedPreferences(Preference.MAHASISWA.name, Context.MODE_PRIVATE)

    enum class Preference {
        MAHASISWA, IS_FIRST_RUN
    }

    fun setFirstRunStatus(boolean: Boolean) {
        preference.edit().putBoolean(Preference.IS_FIRST_RUN.name, boolean).apply()
    }

    fun isFirstReun() = preference.getBoolean(Preference.IS_FIRST_RUN.name, true)


}