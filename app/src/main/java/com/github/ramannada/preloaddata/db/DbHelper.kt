package com.github.ramannada.preloaddata.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

/**
 * Created by labibmuhajir on 2019-07-11.
 * labibmuhajir@yahoo.com
 */
class DbHelper(context: Context) :
    SQLiteOpenHelper(context, DbContract.DATABASE_NAME, null, DbContract.DATABASE_VERSION) {

    companion object {
        private val SQL_CREATE_TABLE_MAHASISWA = "CREATE TABLE ${DbContract.Table.MAHASISWA.name} " +
                "(${DbContract.MahasiswaColumn.ID.name} INTEGER PRIMARY KEY AUTOINCREMENT," +
                "${DbContract.MahasiswaColumn.NIM.name} TEXT NOT NULL," +
                "${DbContract.MahasiswaColumn.NAMA.name} TEXT NOT NULL)"

        private val SQL_DELETE_TABLE_MAHASISWA = "DROP TABLE IF EXISTS ${DbContract.Table.MAHASISWA.name}"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(SQL_CREATE_TABLE_MAHASISWA)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL(SQL_DELETE_TABLE_MAHASISWA)
        onCreate(db)
    }
}