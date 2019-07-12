package com.github.ramannada.preloaddata.entity

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Created by labibmuhajir on 2019-07-11.
 * labibmuhajir@yahoo.com
 */
@Parcelize
data class Mahasiswa(
    var nim: String?,
    var name: String?,
    var id: Int = 0
) : Parcelable