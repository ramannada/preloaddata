package com.github.ramannada.preloaddata

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.github.ramannada.preloaddata.service.DataManagerService
import com.github.ramannada.preloaddata.service.LoadDataCallback
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.ref.WeakReference

class MainActivity : AppCompatActivity(), LoadDataCallback {

    companion object {
        class DataManagerHandler(loadDataCallback: LoadDataCallback) : Handler() {
            private val callback = WeakReference(loadDataCallback)

            override fun handleMessage(msg: Message?) {
                super.handleMessage(msg)

                when (msg?.what) {
                    DataManagerService.FAILED_MESSAGE -> callback.get()?.onFailedExecute()

                    DataManagerService.PREPARATION_MESSAGE -> callback.get()?.onPreExecute()

                    DataManagerService.SUCCESS_MESSAGE -> callback.get()?.onSuccessExecute()

                    DataManagerService.UPDATE_MESSAGE -> {
                        callback.get()?.onProgressUpdate(msg.data.getInt(DataManagerService.Extra.PROGRESS.name))
                    }

                    DataManagerService.CANCEL_MESSAGE -> callback.get()?.onLoadCanceled()

                }
            }
        }
    }

    private var boundService: Messenger? = null
    private var isServiceBound = false

    private val serviceConnection = object : ServiceConnection {
        override fun onServiceDisconnected(name: ComponentName?) {
            isServiceBound = false
        }

        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            boundService = Messenger(service)
            isServiceBound = true
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val messenger = Messenger(DataManagerHandler(this))
        val intentService = Intent(this, DataManagerService::class.java).apply {
            putExtra(DataManagerService.Extra.ACTIVITY_HANDLER.name, messenger)
        }

        bindService(intentService, serviceConnection, Context.BIND_AUTO_CREATE)
    }

    override fun onDestroy() {
        super.onDestroy()
        unbindService(serviceConnection)
    }

    override fun onPreExecute() {
        Toast.makeText(this, "MEMULAI MEMUAT DATA", Toast.LENGTH_LONG).show()
    }

    override fun onProgressUpdate(progress: Int) {
        progress_bar.progress = progress
    }

    override fun onLoadCanceled() {
        finish()
    }

    override fun onSuccessExecute() {
        Toast.makeText(this, "BERHASIL", Toast.LENGTH_LONG).show()
        startActivity(Intent(this@MainActivity, MahasiswaActivity::class.java))
        finish()
    }

    override fun onFailedExecute() {
        Toast.makeText(this, "GAGAL", Toast.LENGTH_LONG).show()
    }
}
