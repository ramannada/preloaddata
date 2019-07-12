package com.github.ramannada.preloaddata.service

import android.content.res.Resources
import android.os.AsyncTask
import com.github.ramannada.preloaddata.AppPreference
import com.github.ramannada.preloaddata.R
import com.github.ramannada.preloaddata.db.MahasiswaHelper
import com.github.ramannada.preloaddata.entity.Mahasiswa
import java.io.BufferedReader
import java.io.InputStreamReader
import java.lang.ref.WeakReference

/**
 * Created by labibmuhajir on 2019-07-11.
 * labibmuhajir@yahoo.com
 */
class LoadDataAsync(
    private var mahasiswaHelper: MahasiswaHelper,
    private var preference: AppPreference,
    loadDataCallback: LoadDataCallback,
    resources: Resources
) : AsyncTask<Any?, Int, Boolean>() {

    private var progress: Double = 30.0
    private var maxProgress = 100.0

    private var callback = WeakReference(loadDataCallback)
    private var resource = WeakReference(resources)

    override fun onPreExecute() {
        super.onPreExecute()
        callback.get()?.onPreExecute()
    }

    override fun doInBackground(vararg params: Any?): Boolean {
        if (preference.isFirstReun()) {
            val data = preLoadRaw()

            val progressDiff = (maxProgress - progress) / data.size
            publishProgress(progress.toInt())

            var isSuccessFull = false

            try {
                mahasiswaHelper.open()
                mahasiswaHelper.beginTransaction()

                data.forEachIndexed { i, value ->
                    mahasiswaHelper.saveTransactional(value)
                    progress += progressDiff

                    publishProgress(progress.toInt())

                    if (i == data.size - 1) {
                        isSuccessFull = true
                        preference.setFirstRunStatus(false)
                        mahasiswaHelper.transactionSuccess()
                    }
                }
            } catch (e: Exception) {
                isSuccessFull = false
            } finally {
                if (!isCancelled) {
                    publishProgress(maxProgress.toInt())
                    mahasiswaHelper.endTransaction()
                    mahasiswaHelper.close()
                }
            }

            if (isCancelled) {
                isSuccessFull = false
                preference.setFirstRunStatus(true)
                mahasiswaHelper.endTransaction()
                mahasiswaHelper.close()
                callback.get()?.onLoadCanceled()
            }

            return isSuccessFull
        } else {
            try {
                synchronized(this) {
                    return true
                }
            } catch (e: Exception) {
                return false
            }
        }
    }

    override fun onProgressUpdate(vararg values: Int?) {
        super.onProgressUpdate(*values)
        values.firstOrNull()?.let { callback.get()?.onProgressUpdate(it) }
    }

    override fun onPostExecute(result: Boolean?) {
        super.onPostExecute(result)
        if (result == true) callback.get()?.onSuccessExecute()
        else callback.get()?.onFailedExecute()
    }

    private fun preLoadRaw(): MutableList<Mahasiswa> {
        val result = mutableListOf<Mahasiswa>()

        try {
            val res = resource.get()?.openRawResource(R.raw.data_mahasiswa)
            val reader = BufferedReader(InputStreamReader(res))

            do {
                val line = reader.readLine()
                val splitstr: List<String?> = line.split("\t")

                result.add(Mahasiswa(splitstr[0], splitstr[1]))
            } while (line != null)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return result
    }
}