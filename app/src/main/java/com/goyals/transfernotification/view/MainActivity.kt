package com.goyals.transfernotification.view

import android.Manifest
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.google.firebase.database.FirebaseDatabase
import com.goyals.transfernotification.R
import com.goyals.transfernotification.databinding.ActivityMainBinding
import com.goyals.transfernotification.model.schema.Notification
import com.goyals.transfernotification.utils.AppUtils
import com.permissionx.guolindev.PermissionX

class MainActivity : AppCompatActivity() {
  private lateinit var binding: ActivityMainBinding
  private lateinit var notificationAdapter: NotificationAdapter
  private var notificationList: MutableList<Notification> = ArrayList()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
    initViews()
  }

  private fun initViews() {
    notificationAdapter = NotificationAdapter(this)
    notificationAdapter.setData(notificationList)
    binding.rvNotifications.apply {
      adapter = notificationAdapter
    }
    readMessages()
  }

  private fun readMessages() {
    PermissionX.init(this)
      .permissions(Manifest.permission.READ_SMS)
      .request { allGranted, _, deniedList ->
        if (allGranted) {
          startSmsContentProvider()
        } else {
          Toast.makeText(this, "These permissions are denied: $deniedList",
            Toast.LENGTH_LONG)
            .show()
        }
      }
  }

  private fun startSmsContentProvider() {
    val cursor: Cursor? =
      contentResolver.query(Uri.parse("content://sms/inbox"), null, null, null,
        null)
    if (cursor?.moveToFirst()!!) { // must check the result to prevent exception
      do {
        var message = ""
        for (idx in 0 until cursor.columnCount) {
          message += " " + cursor.getColumnName(idx)
            .toString() + ":" + cursor.getString(idx) + "\n"
        }
        val notification = AppUtils.getNotificationFromMessage(message)
        addNotificationToFirebase(notification)
        notificationList.add(notification)
        notificationAdapter.notifyDataSetChanged()
      } while (cursor.moveToNext())
      cursor.close()
    }
  }

  private fun addNotificationToFirebase(notification: Notification) {
    val database = FirebaseDatabase.getInstance()
    val myRef = database.getReference("notifications")
    myRef.child(notification.time)
      .setValue(notification)
  }
}