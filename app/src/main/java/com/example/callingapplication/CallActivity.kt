package com.example.callingapplication

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.telecom.Call
import androidx.annotation.RequiresApi
import androidx.core.view.isVisible
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import kotlinx.android.synthetic.main.activity_call.*
import timber.log.Timber
import java.util.concurrent.TimeUnit

class CallActivity : AppCompatActivity() {
    private val disposable = CompositeDisposable()
    private lateinit var number : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_call)
        number = intent.data!!.schemeSpecificPart
    }
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onStart() {
        super.onStart()

//
        answer.setOnClickListener {
            OngoingCall.answer()
        }

        hangup.setOnClickListener {
            OngoingCall.hangup()
        }
        mute.setOnClickListener{
            OngoingCall.mute()
        }

        OngoingCall.state
            .subscribe(::updateUi)
            .addTo(disposable)

        OngoingCall.state
            .filter { it == Call.STATE_DISCONNECTED }
            .delay(1, TimeUnit.SECONDS)
            .firstElement()
            .subscribe { finish() }
            .addTo(disposable)
    }

    @SuppressLint("SetTextI18n")
    private fun updateUi(state: Int) {
        callInfo.text = number
        callUI.text = "${state.asString().toLowerCase().capitalize()}"

        answer.isVisible = state == Call.STATE_RINGING
        hangup.isVisible = state in listOf(
            Call.STATE_ACTIVE,
            Call.STATE_DIALING,
            Call.STATE_CONNECTING,
            Call.STATE_RINGING

        )
        mute.isVisible = state in listOf(
            Call.STATE_ACTIVE,
            Call.STATE_DIALING


        )
        record.isVisible = state in listOf(
            Call.STATE_ACTIVE,
            Call.STATE_DIALING


        )
        contact.isVisible = state in listOf(
            Call.STATE_ACTIVE,
            Call.STATE_DIALING


        )
        addCall.isVisible = state in listOf(

            Call.STATE_DIALING,
            Call.STATE_ACTIVE

        )
        dialPad.isVisible = state in listOf(
            Call.STATE_ACTIVE,
            Call.STATE_DIALING


        )
        speaker.isVisible = state in listOf(
            Call.STATE_ACTIVE,
            Call.STATE_DIALING


        )
    }

    override fun onStop() {
        super.onStop()
        disposable.clear()
    }

    companion object {
        @RequiresApi(Build.VERSION_CODES.M)
        fun start(context: Context, call: Call) {
            Intent(context, CallActivity::class.java)
                .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                .setData(call.details.handle)
                .let(context::startActivity)
        }
    }
    fun Int.asString(): String = when (this) {
        Call.STATE_NEW -> "NEW"
        Call.STATE_RINGING -> "RINGING"
        Call.STATE_DIALING -> "DIALING"
        Call.STATE_ACTIVE -> "ACTIVE"
        Call.STATE_HOLDING -> "HOLDING"
        Call.STATE_DISCONNECTED -> "DISCONNECTED"
        Call.STATE_CONNECTING -> "CONNECTING"
        Call.STATE_DISCONNECTING -> "DISCONNECTING"
        Call.STATE_SELECT_PHONE_ACCOUNT -> "SELECT_PHONE_ACCOUNT"
        else -> {
            Timber.w("Unknown state ${this}")
            "UNKNOWN"
        }
    }

}
