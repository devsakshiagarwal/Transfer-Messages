package com.goyals.transfernotification.model.schema

import com.goyals.transfernotification.utils.AppUtils

data class Notification(val name: String = "",
  val time: String = "",
  val sentFromNumber: String = "",
  val sendFromName: String = "") {
  val timeToShow get() : String = AppUtils.getDateFromMillis(time.toLong())
}