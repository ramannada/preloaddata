package com.github.ramannada.preloaddata.service

import android.app.Service
import android.content.Intent
import android.os.Bundle
import android.os.IBinder
import android.os.Message
import android.os.Messenger
import com.github.ramannada.preloaddata.AppPreference
import com.github.ramannada.preloaddata.db.MahasiswaHelper

class DataManagerService : Service(), LoadDataCallback {
    private var loadDataAsync: LoadDataAsync? = null
    private var messenger: Messenger? = null

    enum class Extra {
        PROGRESS, ACTIVITY_HANDLER
    }

    companion object {
        const val PREPARATION_MESSAGE = 0
        const val UPDATE_MESSAGE = 1
        const val SUCCESS_MESSAGE = 2
        const val FAILED_MESSAGE = 3
        const val CANCEL_MESSAGE = 4
    }

    override fun onCreate() {
        super.onCreate()


        loadDataAsync = MahasiswaHelper.getInstance(this)?.let {
            LoadDataAsync(
                it,
                AppPreference(this), this, resources
            )
        }
    }

    override fun onBind(intent: Intent): IBinder {
        messenger = intent.getParcelableExtra(Extra.ACTIVITY_HANDLER.name)
        loadDataAsync?.execute()

        return messenger!!.binder
    }

    override fun onUnbind(intent: Intent?): Boolean {
        loadDataAsync?.cancel(true)
        return super.onUnbind(intent)
    }

    override fun onDestroy() {
        super.onDestroy()
        loadDataAsync?.cancel(true)
    }

    override fun onPreExecute() {
        try {
            messenger?.send(Message.obtain(null, PREPARATION_MESSAGE))
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onProgressUpdate(progress: Int) {
        try {
            messenger?.send(Message.obtain(null, UPDATE_MESSAGE).apply {
                data = Bundle().apply {
                    putInt(Extra.PROGRESS.name, progress)
                }
            })
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onLoadCanceled() {
        try {
            messenger?.send(Message.obtain(null, CANCEL_MESSAGE))
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onSuccessExecute() {
        try {
            messenger?.send(Message.obtain(null, SUCCESS_MESSAGE))
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onFailedExecute() {
        try {
            messenger?.send(Message.obtain(null, FAILED_MESSAGE))
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
