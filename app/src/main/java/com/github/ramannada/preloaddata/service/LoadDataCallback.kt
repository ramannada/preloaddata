package com.github.ramannada.preloaddata.service

/**
 * Created by labibmuhajir on 2019-07-11.
 * labibmuhajir@yahoo.com
 */
interface LoadDataCallback {
    fun onPreExecute()

    fun onProgressUpdate(progress: Int)

    fun onSuccessExecute()

    fun onFailedExecute()

    fun onLoadCanceled()
}