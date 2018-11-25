package com.gneo.fgurbanov.junctionhealth.presentation.mainActivity.fragments.details

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.gneo.fgurbanov.junctionhealth.R
import com.gneo.fgurbanov.junctionhealth.data.Detail
import com.gneo.fgurbanov.junctionhealth.utils.DState
import com.gneo.fgurbanov.junctionhealth.utils.snackbar
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_details.*
import javax.inject.Inject

class DetailsFragment : DaggerFragment() {

    @Inject
    lateinit var viewModel: DetailsViewModel
    private val adapter = DetailsAdapter()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.checkoutInfoVO.observe(this, Observer { dState ->
            dState?.let {
                handleDataState(it)
            }
        })
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        inflater.inflate(R.layout.fragment_details, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(detailsRC) {
            layoutManager = LinearLayoutManager(context)
            adapter = this@DetailsFragment.adapter.apply {
                onItemClick = { id -> viewModel.showDetail(id) }
            }
        }
    }

    private fun handleDataState(dState: DState<List<Detail>>) {
        when (dState) {
            is DState.Loading -> updateLoading(true)
            is DState.Error -> {
                updateLoading(false)
                snackbar(dState.description) {
                    viewModel.updateScan()
                }
            }
            is DState.Success -> {
                updateLoading(false)
                dState.data.let {
                    adapter.submitList(it)
                }
            }
        }
    }

    private fun updateLoading(isLoading: Boolean) {
        swipeRL.post {
            if (isLoading != swipeRL.isRefreshing) {
                swipeRL.isRefreshing = isLoading
            }
        }
    }

    companion object {
        fun newInstance(): DetailsFragment = DetailsFragment()
    }
}