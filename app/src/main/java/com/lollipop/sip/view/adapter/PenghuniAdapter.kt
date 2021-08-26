package com.lollipop.sip.view.adapter

import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.lollipop.sip.R
import com.lollipop.sip.databinding.ItemListPenghuniBinding
import com.lollipop.sip.service.model.PenghuniResult

class PenghuniAdapter : RecyclerView.Adapter<PenghuniAdapter.ViewHolder>() {
    private var _items: MutableList<PenghuniResult> = ArrayList()
    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setList(list: List<PenghuniResult>?) {
        if (list == null) return
        this._items.clear()
        this._items.addAll(list)
        notifyDataSetChanged()
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = PenghuniAdapter.ViewHolder(
        LayoutInflater.from(parent.context)
            .inflate(R.layout.item_list_penghuni, parent, false)
    )

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder) {
            val item = _items[position]
            with(binding) {
                tvNik.text = item.nik
                tvNama.text = item.nama_lengkap
                ivEdit.setOnClickListener {
                    onItemClickCallback.onItemClick(item)
                }
            }
        }
    }

    override fun getItemCount() = _items.size

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = ItemListPenghuniBinding.bind(view)
    }

    interface OnItemClickCallback {
        fun onItemClick(item: PenghuniResult)
    }
}