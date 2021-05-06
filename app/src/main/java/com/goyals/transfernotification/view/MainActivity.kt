package com.goyals.transfernotification.view

import android.Manifest
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.goyals.transfernotification.R
import com.goyals.transfernotification.databinding.ActivityMainBinding
import com.goyals.transfernotification.model.schema.Notification
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
      .permissions(Manifest.permission.READ_SMS,
        Manifest.permission.RECEIVE_SMS)
      .request { allGranted, _, deniedList ->
        if (!allGranted) {
          Toast.makeText(this, "These permissions are denied: $deniedList",
            Toast.LENGTH_LONG)
            .show()
        }
      }
  }
}