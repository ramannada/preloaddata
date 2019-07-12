package com.github.ramannada.preloaddata.db

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import androidx.core.database.getIntOrNull
import androidx.core.database.getStringOrNull
import com.github.ramannada.preloaddata.entity.Mahasiswa

/**
 * Created by labibmuhajir on 2019-07-11.
 * labibmuhajir@yahoo.com
 */
class MahasiswaHelper private constructor(context: Context) {
    private val dbHelper = DbHelper(context)
    private var db: SQLiteDatabase? = null

    companion object {
        private var instance: MahasiswaHelper? = null

        fun getInstance(context: Context): MahasiswaHelper? {
            if (instance == null) synchronized(SQLiteOpenHelper::class) {
                if (instance == null) instance = MahasiswaHelper(context)
            }

            return instance
        }
    }

    fun open() {
        db = dbHelper.writableDatabase
    }

    fun close() {
        dbHelper.close()

        if (db?.isOpen == true) db?.close()
    }

    fun save(mahasiswa: Mahasiswa): Long? {
        return db?.insert(DbContract.Table.MAHASISWA.name, null, ContentValues().apply {
            put(DbContract.MahasiswaColumn.NIM.name, mahasiswa.nim)
            put(DbContract.MahasiswaColumn.NAMA.name, mahasiswa.name)
        })
    }

    fun beginTransaction() {
        db?.beginTransaction()
    }

    fun transactionSuccess() {
        db?.setTransactionSuccessful()
    }

    fun endTransaction() {
        if (db?.inTransaction() == true) db?.endTransaction()
    }

    fun saveTransactional(mahasiswa: Mahasiswa) {
        val query =
            "INSERT INTO ${DbContract.Table.MAHASISWA.name} (${DbContract.MahasiswaColumn.NIM.name}, ${DbContract.MahasiswaColumn.NAMA.name}) VALUES (?, ?)"

        db?.compileStatement(query)?.apply {
            bindString(1, mahasiswa.nim)
            bindString(2, mahasiswa.name)
            execute()
        }?.clearBindings()
    }

    fun getAllMahasiswa(): List<Mahasiswa> {
        return handleCursor(
            db?.query(
                DbContract.Table.MAHASISWA.name,
                null,
                null,
                null,
                null,
                null,
                "${DbContract.MahasiswaColumn.ID.name} ASC",
                null
            )
        )

    }

    fun findMahasiswaByName(name: String): List<Mahasiswa> {
        return handleCursor(
            db?.query(
                DbContract.Table.MAHASISWA.name,
                null,
                "${DbContract.MahasiswaColumn.NAMA.name} LIKE ?",
                arrayOf(name),
                null,
                null,
                "${DbContract.MahasiswaColumn.ID.name} ASC",
                null
            )
        )
    }

    fun findMahasiswaById(id: Int): Mahasiswa? {
        return handleCursor(
            db?.query(
                DbContract.Table.MAHASISWA.name,
                null,
                "${DbContract.MahasiswaColumn.ID.name} = ?",
                arrayOf(id.toString()),
                null,
                null,
                "${DbContract.MahasiswaColumn.ID.name} ASC",
                null
            )
        ).firstOrNull()
    }

    private fun handleCursor(cursor: Cursor?): MutableList<Mahasiswa> {
        val result = mutableListOf<Mahasiswa>()

        cursor?.let {
            it.moveToFirst()


            if (it.count > 0) {
                do {
                    result.add(
                        Mahasiswa(
                            it.getStringOrNull(it.getColumnIndexOrThrow(DbContract.MahasiswaColumn.NIM.name)),
                            it.getStringOrNull(it.getColumnIndexOrThrow(DbContract.MahasiswaColumn.NAMA.name)),
                            it.getIntOrNull(it.getColumnIndexOrThrow(DbContract.MahasiswaColumn.ID.name)) ?: 0
                        )
                    )
                    it.moveToNext()
                } while (!it.isAfterLast)
            }
        }

        cursor?.close()

        return result
    }

}