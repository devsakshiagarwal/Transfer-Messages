package com.goyals.transfernotification.view

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.goyals.transfernotification.R
import com.goyals.transfernotification.databinding.ItemNotificationBinding
import com.goyals.transfernotification.model.schema.Notification
import com.goyals.transfernotification.view.NotificationAdapter.ItemViewHolder

class NotificationAdapter(private val context: Context) : RecyclerView.Adapter<ItemViewHolder>() {
  private var list: List<Notification> = ArrayList()

  fun setData(list: List<Notification>) {
    this.list = list
    notifyDataSetChanged()
  }

  override fun onCreateViewHolder(parent: ViewGroup,
    viewType: Int): ItemViewHolder {
    val binding: ItemNotificationBinding =
      DataBindingUtil.inflate(LayoutInflater.from(context),
        R.layout.item_notification, parent, false)
    return ItemViewHolder(binding.root)
  }

  class ItemViewHolder internal constructor(itemView: View) : RecyclerView.ViewHolder(
    itemView) {
    var binding: ItemNotificationBinding? = DataBindingUtil.bind(itemView)
  }

  override fun getItemCount(): Int = list.size

  override fun onBindViewHolder(holder: ItemViewHolder,
    position: Int) {
    holder.binding?.item = list[position]
  }
}