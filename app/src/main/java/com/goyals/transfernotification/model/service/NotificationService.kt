package com.goyals.transfernotification.model.service

import android.app.Service
import android.content.Intent
import android.content.IntentFilter
import android.os.IBinder
import com.goyals.transfernotification.model.broadcast_receiver.RestartReceiver
import com.goyals.transfernotification.model.broadcast_receiver.SmsBroadcastReceiver

class NotificationService : Service() {
  private lateinit var smsBroadcastReceiver: SmsBroadcastReceiver

  override fun onCreate() {
    super.onCreate()
    val filter = IntentFilter()
    filter.addAction("android.provider.Telephony.SMS_RECEIVED")
    smsBroadcastReceiver = SmsBroadcastReceiver()
    registerReceiver(smsBroadcastReceiver, filter)
  }

  override fun onStartCommand(intent: Intent?,
    flags: Int,
    startId: Int): Int {
    super.onStartCommand(intent, flags, startId)
    return START_STICKY
  }

  override fun onDestroy() {
    super.onDestroy()
    unregisterReceiver(smsBroadcastReceiver)
    val broadcastIntent = Intent()
    broadcastIntent.action = "RestartReceiver"
    broadcastIntent.setClass(this, RestartReceiver::class.java)
    this.sendBroadcast(broadcastIntent)
  }

  override fun onBind(intent: Intent?): IBinder? {
    return null
  }
}