package com.example.callingapplication

import android.os.Build
import android.telecom.Call
import android.telecom.VideoProfile
import androidx.annotation.RequiresApi
import androidx.coordinatorlayout.widget.CoordinatorLayout
import io.reactivex.subjects.BehaviorSubject
import timber.log.Timber

object OngoingCall {
    val state: BehaviorSubject<Int> = BehaviorSubject.create()

    private val callback = @RequiresApi(Build.VERSION_CODES.M)
    object:Call.Callback(){
        override fun onStateChanged(call: Call?, newState: Int) {
            state.onNext(newState)
        }

    }

    var call:Call?=null

    @RequiresApi(Build.VERSION_CODES.M)
    set(value) {
        field?.unregisterCallback(callback)
        value?.let {
        it.registerCallback(callback)
            state.onNext(it.state)
        }
        field = value
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun answer(){
        call!!.answer(VideoProfile.STATE_AUDIO_ONLY)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun hangup(){
        call!!.disconnect()
    }
    fun mute(){
        call!!.
    }
}
