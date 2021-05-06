package com.goyals.transfernotification.model.service

import android.app.Notification
import android.app.Service
import android.content.Intent
import android.content.IntentFilter
import android.database.Cursor
import android.net.Uri
import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import android.os.IBinder
import androidx.annotation.RequiresApi
import com.google.android.gms.auth.api.phone.SmsRetriever
import com.google.firebase.database.FirebaseDatabase
import com.goyals.transfernotification.model.broadcast_receiver.RestartReceiver
import com.goyals.transfernotification.model.broadcast_receiver.SmsBroadcastReceiver
import com.goyals.transfernotification.utils.AppUtils

class NotificationService : Service() {
  private lateinit var smsReceiver: SmsBroadcastReceiver
  override fun onCreate() {
    super.onCreate()
    if (VERSION.SDK_INT > VERSION_CODES.O) startMyOwnForeground() else startForeground(
      1, Notification())
  }

  //@RequiresApi(VERSION_CODES.O) private fun startMyOwnForeground() {
  //  val cursor: Cursor? =
  //    contentResolver.query(Uri.parse("content://sms/inbox"), null, null, null,
  //      null)
  //  if (cursor?.moveToFirst()!!) { // must check the result to prevent exception
  //    do {
  //      var message = ""
  //      for (idx in 0 until cursor.columnCount) {
  //        message += " " + cursor.getColumnName(idx)
  //          .toString() + ":" + cursor.getString(idx) + "\n"
  //      }
  //      val notification = AppUtils.getNotificationFromMessage(message)
  //      addNotificationToFirebase(notification)
  //    } while (cursor.moveToNext())
  //  }
  //}
  @RequiresApi(VERSION_CODES.O) private fun startMyOwnForeground() {
    startSMSListener()
  }

  private fun startSMSListener() {
    try {
      smsReceiver = SmsBroadcastReceiver()
      val intentFilter = IntentFilter()
      intentFilter.addAction(SmsRetriever.SMS_RETRIEVED_ACTION)
      registerReceiver(smsReceiver, intentFilter)
      val client = SmsRetriever.getClient(this)
      val task = client.startSmsRetriever()
    } catch (e: Exception) {
      e.printStackTrace()
    }
  }

  private fun addNotificationToFirebase(notification: com.goyals.transfernotification.model.schema.Notification) {
    val database = FirebaseDatabase.getInstance()
    val myRef = database.getReference("notifications")
    myRef.child(notification.time)
      .setValue(notification)
  }

  override fun onStartCommand(intent: Intent?,
    flags: Int,
    startId: Int): Int {
    super.onStartCommand(intent, flags, startId)
    return START_STICKY
  }

  override fun onDestroy() {
    super.onDestroy()
    unregisterReceiver(smsReceiver)
    val broadcastIntent = Intent()
    broadcastIntent.action = "RestartReceiver"
    broadcastIntent.setClass(this, RestartReceiver::class.java)
    this.sendBroadcast(broadcastIntent)
  }

  override fun onBind(intent: Intent?): IBinder? {
    return null
  }
}