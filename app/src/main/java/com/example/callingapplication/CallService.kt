package com.example.callingapplication

import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.telecom.Call
import android.telecom.InCallService
import androidx.annotation.RequiresApi

@RequiresApi(Build.VERSION_CODES.M)
class CallService : InCallService() {

    override fun onCallAdded(call: Call) {
        super.onCallAdded(call)
        OngoingCall.call=call
        CallActivity.start(this,call)
    }

    override fun onCallRemoved(call: Call?) {
        super.onCallRemoved(call)
        OngoingCall.call=null
    }
}
