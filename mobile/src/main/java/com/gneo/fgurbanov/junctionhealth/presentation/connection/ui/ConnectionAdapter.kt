package com.gneo.fgurbanov.junctionhealth.presentation.connection.ui

import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.gneo.fgurbanov.junctionhealth.R
import com.gneo.fgurbanov.junctionhealth.utils.inflate
import com.gneo.fgurbanov.junctionhealth.utils.list.ListAdapter
import kotlinx.android.synthetic.main.item_connection.view.*
import javax.inject.Inject


class ConnectionAdapter @Inject constructor() :
    ListAdapter<ConnectionVO, ConnectionAdapter.ConnectionViewHolder>(diffCallback) {

    companion object {
        val diffCallback = object : DiffUtil.ItemCallback<ConnectionVO>() {
            override fun areItemsTheSame(oldItem: ConnectionVO, newItem: ConnectionVO) =
                oldItem == newItem

            override fun areContentsTheSame(oldItem: ConnectionVO, newItem: ConnectionVO) =
                oldItem == newItem
        }
    }

    var onItemClick: ((serial: String) -> Unit) = {}

    var onConnectClick: ((mac: String) -> Unit) = {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ConnectionViewHolder =
        ConnectionViewHolder(parent.inflate(R.layout.item_connection))


    override fun onBindViewHolder(holder: ConnectionViewHolder, position: Int) {
        return holder.bindItem(getItem(position))
    }

    inner class ConnectionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindItem(item: ConnectionVO) {
            with(itemView) {
                nameTv.text = item.name
                descriptionTv.text = item.macAddress
                statusTv.text = item.status
                connectBtn.text = if (item.isConnected) "Disconnect" else "Connect"
                connectBtn.setOnClickListener {
                    onConnectClick(item.macAddress)
                }
            }
        }
    }
}
