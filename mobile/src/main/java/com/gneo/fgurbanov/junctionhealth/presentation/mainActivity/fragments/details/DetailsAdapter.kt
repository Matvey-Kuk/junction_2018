package com.gneo.fgurbanov.junctionhealth.presentation.mainActivity.fragments.details

import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.gneo.fgurbanov.junctionhealth.R
import com.gneo.fgurbanov.junctionhealth.data.Detail
import com.gneo.fgurbanov.junctionhealth.utils.inflate
import com.gneo.fgurbanov.junctionhealth.utils.list.ListAdapter
import kotlinx.android.synthetic.main.item_detail.view.*
import javax.inject.Inject


class DetailsAdapter @Inject constructor() :
    ListAdapter<Detail, DetailsAdapter.ConnectionViewHolder>(diffCallback) {

    companion object {
        val diffCallback = object : DiffUtil.ItemCallback<Detail>() {
            override fun areItemsTheSame(oldItem: Detail, newItem: Detail) =
                oldItem == newItem

            override fun areContentsTheSame(oldItem: Detail, newItem: Detail) =
                oldItem == newItem
        }
    }

    var onItemClick: ((id: Int) -> Unit) = {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ConnectionViewHolder =
        ConnectionViewHolder(parent.inflate(R.layout.item_detail))


    override fun onBindViewHolder(holder: ConnectionViewHolder, position: Int) {
        return holder.bindItem(getItem(position))
    }

    inner class ConnectionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindItem(item: Detail) {
            with(itemView) {
                nameTv.text = item.toString()
                descriptionTv.text = item.toString()
                statusTv.text = item.toString()
                setOnClickListener {
                    onItemClick(item.id)
                }
            }
        }
    }
}
