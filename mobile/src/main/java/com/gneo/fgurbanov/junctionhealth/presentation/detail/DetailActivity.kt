package com.gneo.fgurbanov.junctionhealth.presentation.detail

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v4.content.ContextCompat
import com.gneo.fgurbanov.junctionhealth.R
import com.gneo.fgurbanov.junctionhealth.data.Detail
import com.gneo.fgurbanov.junctionhealth.utils.DState
import com.gneo.fgurbanov.junctionhealth.utils.showOrGone
import com.gneo.fgurbanov.junctionhealth.utils.snackbar
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.layout_progressbar.*
import net.alexandroid.utils.exoplayerhelper.ExoPlayerHelper
import javax.inject.Inject


class DetailActivity : DaggerAppCompatActivity() {
    @Inject
    lateinit var viewModel: DetailViewModel
    lateinit var mExoPlayerHelper: ExoPlayerHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        viewModel.detailScreenLD.observe(this, Observer { dState ->
            dState?.let {
                handleDataState(it)
            }
        })
        mainScreenBtn.setOnClickListener { viewModel.toMainScreen() }
    }


    private fun handleDataState(dState: DState<Detail>) {
        when (dState) {
            is DState.Loading -> updateLoading(true)
            is DState.Error -> {
                updateLoading(false)
                snackbar(container, dState.description)
            }
            is DState.Success -> {
                updateLoading(false)
                descriptionTv.text = dState.data.mistakes.joinToString(",\n") { it.title }
                mExoPlayerHelper = ExoPlayerHelper.Builder(this, exoPlayerView)
                    .setUiControllersVisibility(true)
                    .setAutoPlayOn(true)
                    .setVideoUrls(dState.data.renderedVideo)
                    .addProgressBarWithColor(ContextCompat.getColor(this, R.color.colorAccent))
                    .createAndPrepare()
                mExoPlayerHelper.playerPlay()
            }
        }
    }

    private fun updateLoading(isLoading: Boolean) {
        progressBar.post {
            progressBar.showOrGone(isLoading)
        }
    }
}