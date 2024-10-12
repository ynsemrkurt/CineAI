package com.example.cineai.ui.classes

import android.app.Activity
import com.example.cineai.BuildConfig.AD_UNIT_ID
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback

class AdManager(private val activity: Activity) {
    private var rewardedAd: RewardedAd? = null

    fun loadRewardedAd(onAdLoaded: () -> Unit) {
        val adRequest = AdRequest.Builder().build()

        RewardedAd.load(activity, AD_UNIT_ID, adRequest, object : RewardedAdLoadCallback() {
            override fun onAdLoaded(ad: RewardedAd) {
                rewardedAd = ad
                showRewardedAd {
                    onAdLoaded.invoke()
                }
            }

            override fun onAdFailedToLoad(adError: LoadAdError) {
                rewardedAd = null
                onAdLoaded.invoke()
            }
        })
    }

    fun showRewardedAd(onRewarded: () -> Unit) {
        rewardedAd?.let { ad ->
            ad.show(activity) {
                onRewarded.invoke()
            }
        } ?: run {
            onRewarded.invoke()
        }
    }
}
