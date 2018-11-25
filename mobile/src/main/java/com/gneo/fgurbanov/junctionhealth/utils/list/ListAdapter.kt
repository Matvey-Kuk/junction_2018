package com.gneo.fgurbanov.junctionhealth.utils.list

import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView

abstract class ListAdapter<T, VH : RecyclerView.ViewHolder> protected constructor(
    diffCallback: DiffUtil.ItemCallback<T>
) : RecyclerView.Adapter<VH>() {

    private val listDiffer: AsyncListDiffer<T> = AsyncListDiffer(this, diffCallback)

    var statusCallback: AsyncListDiffer.AsynListDifferStatusCallback?
        set(value) {
            listDiffer.statusCallback = value
        }
        get() = listDiffer.statusCallback

    fun submitList(list: List<T>) {
        listDiffer.submitList(list)
    }

    fun addItem(item: T) {
        listDiffer.addItem(item)
    }

    fun getList() = listDiffer.currentList

    protected fun getItem(position: Int): T {
        return listDiffer.currentList[position]
    }

    override fun getItemCount(): Int {
        return listDiffer.currentList.size
    }
}