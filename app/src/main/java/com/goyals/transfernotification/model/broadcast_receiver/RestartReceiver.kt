package com.goyals.transfernotification.model.broadcast_receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import android.util.Log
import com.goyals.transfernotification.model.service.NotificationService

class RestartReceiver : BroadcastReceiver() {
  override fun onReceive(context: Context,
    intent: Intent?) {
    Log.i("Broadcast Listened", "Service tried to stop")
    if (VERSION.SDK_INT >= VERSION_CODES.O) {
      context.startForegroundService(
        Intent(context, NotificationService::class.java))
    } else {
      context.startService(Intent(context, NotificationService::class.java))
    }
  }
}