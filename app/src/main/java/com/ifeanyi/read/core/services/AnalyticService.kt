package com.ifeanyi.read.core.services

import android.annotation.SuppressLint
import android.content.Context
import com.ifeanyi.read.BuildConfig
import com.mixpanel.android.mpmetrics.MixpanelAPI

object AnalyticService {
    @SuppressLint("StaticFieldLeak")
    private  var _mixpanel: MixpanelAPI? = null

    fun init(context: Context) {
        _mixpanel = MixpanelAPI.getInstance(context, BuildConfig.MIX_PANEL_KEY, true)
    }

    fun track(event: String) {
        if (BuildConfig.DEBUG) return
        _mixpanel?.track(event)
    }
}