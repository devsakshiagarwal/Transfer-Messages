package com.goyals.transfernotification.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object AppUtils {
  fun getDateFromMillis(timestamp: Long): String {
    return SimpleDateFormat("dd-MMM hh:mm", Locale.getDefault()).format(
      Date(timestamp))
      .toString()
  }
}