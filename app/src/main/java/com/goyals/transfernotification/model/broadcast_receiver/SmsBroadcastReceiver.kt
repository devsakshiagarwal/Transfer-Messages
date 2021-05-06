package com.goyals.transfernotification.model.broadcast_receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.telephony.SmsMessage
import android.util.Log

class SmsBroadcastReceiver : BroadcastReceiver() {
  companion object {
    private const val TAG = "SmsBroadcastReceive"
    private const val pdu_type = "pdus"
  }

  override fun onReceive(context: Context,
    intent: Intent) {
    Log.d(TAG, "onReceive: ")
    val bundle = intent.extras
    val messages: Array<SmsMessage?>
    var strMessage = ""
    val format = bundle!!.getString("format")
    val pduList = bundle[pdu_type] as Array<*>?
    if (!pduList.isNullOrEmpty()) {
      messages = arrayOfNulls(pduList.size)
      for (i in messages.indices) {
        messages[i] = SmsMessage.createFromPdu(pduList[i] as ByteArray, format)
        strMessage += "SMS from  ${messages[i]?.originatingAddress}"
        strMessage += " : ${messages[i]?.messageBody}"
        Log.d(TAG, "onReceive: $strMessage")
      }
    }
  }
}
