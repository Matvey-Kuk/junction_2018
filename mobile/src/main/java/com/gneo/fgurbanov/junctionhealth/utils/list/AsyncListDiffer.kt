package com.gneo.fgurbanov.junctionhealth.utils.list

import android.os.Handler
import android.os.Looper
import android.support.v7.util.AdapterListUpdateCallback
import android.support.v7.util.DiffUtil
import android.support.v7.util.ListUpdateCallback
import android.support.v7.widget.RecyclerView
import java.util.*
import java.util.concurrent.Executor
import java.util.concurrent.Executors

class AsyncListDiffer<T>(
    adapter: RecyclerView.Adapter<*>,
    private val diffCallback: DiffUtil.ItemCallback<T>
) {

    private val listUdateCallback: ListUpdateCallback

    private val mainThreadExecutor = MainThreadExecutor()

    private val backgroundThreadExecutor = Executors.newFixedThreadPool(2)

    private var list: List<T>? = null

    var statusCallback: AsynListDifferStatusCallback? = null

    var currentList = emptyList<T>()
        private set

    private var maxScheduledGeneration: Int = 0

    init {
        listUdateCallback = AdapterListUpdateCallback(adapter)
    }

    fun addItem(item: T) {
        val newList = mutableListOf<T>()
        list?.let {
            newList += it
        }
        newList += item
        submitList(newList)
    }

    fun submitList(newList: List<T>?) {
        statusCallback?.onDiffStarted()
        if (newList === list) {
            // nothing to do
            statusCallback?.onDiffFinished()
            return
        }

        // incrementing generation means any currently-running diffs are discarded when they finish
        val runGeneration = ++maxScheduledGeneration

        // fast simple remove all
        if (newList == null) {

            val countRemoved = list!!.size
            list = null
            currentList = emptyList()
            // notify last, after list is updated
            listUdateCallback.onRemoved(0, countRemoved)
            statusCallback?.onDiffFinished()
            return
        }

        // fast simple first insert
        if (list == null) {
            list = newList
            currentList = Collections.unmodifiableList(newList)
            // notify last, after list is updated
            listUdateCallback.onInserted(0, newList.size)
            statusCallback?.onDiffFinished()
            return
        }

        val oldList = list
        backgroundThreadExecutor.execute {
            val result = DiffUtil.calculateDiff(object : DiffUtil.Callback() {
                override fun getOldListSize(): Int {
                    return oldList!!.size
                }

                override fun getNewListSize(): Int {
                    return newList.size
                }

                override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                    return diffCallback.areItemsTheSame(
                        oldList!![oldItemPosition], newList[newItemPosition]
                    )
                }

                override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                    return diffCallback.areContentsTheSame(
                        oldList!![oldItemPosition], newList[newItemPosition]
                    )
                }

                override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any? {
                    return diffCallback.getChangePayload(
                        oldList!![oldItemPosition], newList[newItemPosition]
                    )
                }
            })

            mainThreadExecutor.execute {
                if (maxScheduledGeneration == runGeneration) {
                    latchList(newList, result)
                }
            }
        }
    }

    private fun latchList(newList: List<T>, diffResult: DiffUtil.DiffResult) {
        list = newList
        // notify last, after list is updated
        currentList = Collections.unmodifiableList(newList)
        diffResult.dispatchUpdatesTo(listUdateCallback)
        statusCallback?.onDiffFinished()
    }

    private class MainThreadExecutor : Executor {
        internal val mHandler = Handler(Looper.getMainLooper())
        override fun execute(command: Runnable) {
            mHandler.post(command)
        }
    }

    interface AsynListDifferStatusCallback {
        fun onDiffStarted()
        fun onDiffFinished()
    }
}