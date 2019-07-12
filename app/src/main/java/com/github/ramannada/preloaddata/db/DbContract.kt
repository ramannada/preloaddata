package com.github.ramannada.preloaddata.db

/**
 * Created by labibmuhajir on 2019-07-11.
 * labibmuhajir@yahoo.com
 */
class DbContract {
    companion object {
        const val DATABASE_NAME = "com.github.ramannada.preloaddata"
        const val DATABASE_VERSION = 1
    }

    enum class Table {
        MAHASISWA
    }

    enum class MahasiswaColumn {
        ID, NIM, NAMA
    }
}