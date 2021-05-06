package com.goyals.transfernotification.view

import android.Manifest
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.google.firebase.database.*
import com.goyals.transfernotification.R
import com.goyals.transfernotification.databinding.ActivityMainBinding
import com.goyals.transfernotification.model.schema.Notification
import com.permissionx.guolindev.PermissionX

class MainActivity : AppCompatActivity() {
  private lateinit var binding: ActivityMainBinding
  private lateinit var notificationAdapter: NotificationAdapter

  companion object {
    private const val FIREBASE_REFERENCE = "notifications"
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
    initViews()
  }

  private fun initViews() {
    notificationAdapter = NotificationAdapter(this)
    binding.rvNotifications.apply {
      adapter = notificationAdapter
    }
    checkPermissions()
    readMessagesFromFirebase()
  }

  private fun checkPermissions() {
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

  private fun readMessagesFromFirebase() {
    val notificationList: MutableList<Notification> = ArrayList()
    val myFirebaseRef = FirebaseDatabase.getInstance()
      .getReference(FIREBASE_REFERENCE)
    myFirebaseRef.addValueEventListener(object : ValueEventListener {
      override fun onDataChange(dataSnapshot: DataSnapshot) {
        notificationList.clear()
        for (postSnapshot in dataSnapshot.children) {
          notificationList.add(
            postSnapshot.getValue(Notification::class.java) as Notification)
        }
        notificationAdapter.setData(notificationList.reversed())
      }

      override fun onCancelled(databaseError: DatabaseError) {
        Toast.makeText(this@MainActivity,
          "getting difficulty in reading messages. Please restart the app",
          Toast.LENGTH_SHORT)
          .show()
      }
    })
  }
}