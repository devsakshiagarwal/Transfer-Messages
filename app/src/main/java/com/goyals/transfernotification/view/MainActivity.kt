package com.goyals.transfernotification.view

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.goyals.transfernotification.R
import com.goyals.transfernotification.databinding.ActivityMainBinding
import com.goyals.transfernotification.model.schema.Notification

class MainActivity : AppCompatActivity() {
  private lateinit var binding: ActivityMainBinding
  private lateinit var notificationAdapter: NotificationAdapter
  private var notificationList: MutableList<Notification> = ArrayList()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
    initViews()
    if (checkSelfPermission(Manifest.permission.READ_SMS) == PackageManager.PERMISSION_DENIED) {
      val permissions = arrayOf(Manifest.permission.READ_SMS)
      requestPermissions(permissions, 101)
    } else {
      startSMSListener()
    }
  }

  override fun onActivityResult(requestCode: Int,
    resultCode: Int,
    data: Intent?) {
    super.onActivityResult(requestCode, resultCode, data)
    if (resultCode == Activity.RESULT_OK) {
      when (requestCode) {
        101 -> {
          startSMSListener()
        }
      }
    }
  }

  private fun initViews() {
    notificationAdapter = NotificationAdapter(this)
    binding.rvNotifications.apply {
      adapter = notificationAdapter
    }
  }

  private fun startSMSListener() {
    val cursor: Cursor? =
      contentResolver.query(Uri.parse("content://sms/inbox"), null, null, null,
        null)
    if (cursor?.moveToFirst()!!) { // must check the result to prevent exception
      do {
        var msgData = ""
        for (idx in 0 until cursor.columnCount) {
          msgData += " " + cursor.getColumnName(idx)
            .toString() + ":" + cursor.getString(idx) + "\n"
        }
        Log.d("tag", msgData)
        notificationList.add(Notification(msgData.substringAfter("body:")
          .substringBefore("service_center"), msgData.substringAfter("date:")
          .substringBefore("\n"), msgData.substringAfter("service_center:")
          .substringBefore("\n"), msgData.substringAfter("address:")
          .substringBefore("\n")))
        notificationAdapter.setData(notificationList)
      } while (cursor.moveToNext())
    }
  }
}