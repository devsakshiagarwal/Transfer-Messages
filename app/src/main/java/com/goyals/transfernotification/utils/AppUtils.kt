package com.goyals.transfernotification.utils

import com.goyals.transfernotification.model.schema.Notification
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object AppUtils {
  fun getDateFromMillis(timestamp: Long): String {
    return SimpleDateFormat("dd-MMM hh:mm", Locale.getDefault()).format(
      Date(timestamp))
      .toString()
  }

  fun getNotificationFromMessage(message: String) = Notification(
    message.substringAfter("body:")
      .substringBefore("service_center"), message.substringAfter("date:")
      .substringBefore("\n"), message.substringAfter("service_center:")
      .substringBefore("\n"), message.substringAfter("address:")
      .substringBefore("\n"))
}