package com.github.ramannada.preloaddata

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.github.ramannada.preloaddata.db.MahasiswaHelper
import kotlinx.android.synthetic.main.activity_mahasiswa.*

class MahasiswaActivity : AppCompatActivity() {

    private val adapter = MahasiswaAdapter()
    private var mahasiswaHelper: MahasiswaHelper? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mahasiswa)
        mahasiswaHelper = MahasiswaHelper.getInstance(this)

        mahasiswaHelper?.apply {
            open()
            getAllMahasiswa().let { adapter.setData(it) }
            close()
        }

        recyclerview.adapter = adapter
    }

}
