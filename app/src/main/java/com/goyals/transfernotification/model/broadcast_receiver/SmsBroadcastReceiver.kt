package com.goyals.transfernotification.model.broadcast_receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import com.google.firebase.database.FirebaseDatabase
import com.goyals.transfernotification.model.schema.Notification
import com.goyals.transfernotification.utils.AppUtils

class SmsBroadcastReceiver : BroadcastReceiver() {
  companion object {
    private const val FIREBASE_REFERENCE = "notifications"
  }

  /**
   * called by default whenever a new message is received
   */
  override fun onReceive(context: Context?,
    intent: Intent?) {
    startSmsContentProvider(context)
  }

  /**
   * read the message from content provider and add that to firebase realtime
   * as @Notification object
   */
  private fun startSmsContentProvider(context: Context?) {
    val notification: Notification =
      AppUtils.getNotificationFromMessage(getFirstMessageFromProvider(context))
    addNotificationToFirebase(notification)
  }

  /**
   * reading only the last received message as that's the only important
   * message here
   */
  private fun getFirstMessageFromProvider(context: Context?): String {
    val cursor: Cursor? =
      context?.contentResolver?.query(Uri.parse("content://sms/inbox"), null,
        null, null, null)
    var message = ""
    if (cursor?.moveToFirst()!!) {
      for (i in 0 until cursor.columnCount) {
        message += " " + cursor.getColumnName(i)
          .toString() + ":" + cursor.getString(i) + "\n"
      }
    }
    cursor.close()
    return message
  }

  /**
   * notification : object needs to be added to firebase realtime
   */
  private fun addNotificationToFirebase(notification: Notification) {
    val myFirebaseRef = FirebaseDatabase.getInstance()
      .getReference(FIREBASE_REFERENCE)
    myFirebaseRef.child(notification.time)
      .setValue(notification)
  }
}
