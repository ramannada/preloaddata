package com.github.ramannada.preloaddata

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.github.ramannada.preloaddata.entity.Mahasiswa

/**
 * Created by labibmuhajir on 2019-07-11.
 * labibmuhajir@yahoo.com
 */
class MahasiswaAdapter : RecyclerView.Adapter<MahasiswaAdapter.MahasiswaHolder>() {
    private var context: Context? = null
    private var items = mutableSetOf<Mahasiswa?>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MahasiswaHolder {
        context = parent.context
        return MahasiswaHolder(LayoutInflater.from(context).inflate(R.layout.item_mahasiswa, parent, false))
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: MahasiswaHolder, position: Int) {
        holder.bind(items.elementAt(position))
    }

    fun setData(data: List<Mahasiswa?>) {
        items.clear()
        items.addAll(data)
        notifyDataSetChanged()
    }

    inner class MahasiswaHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val tvNim: TextView? = itemView.findViewById(R.id.txt_nim)
        private val tvNama: TextView? = itemView.findViewById(R.id.txt_nama)
        private val iv: ImageView? = itemView.findViewById(R.id.imageView)

        fun bind(mahasiswa: Mahasiswa?) {
            mahasiswa?.let {
                tvNim?.text = it.nim
                tvNama?.text = it.name
            }
        }
    }
}