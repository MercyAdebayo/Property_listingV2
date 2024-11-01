package com.example.homefinder.adapters

import android.view.LayoutInflater

import android.view.View

import android.view.ViewGroup

import android.widget.TextView

import androidx.recyclerview.widget.RecyclerView
import com.example.homefinder.R
import com.example.homefinder.model.NotificationDto

class NotificationsAdapter(private val notificationsList: List<NotificationDto>) :

    RecyclerView.Adapter<NotificationsAdapter.NotificationViewHolder>() {

    class NotificationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val titleTextView: TextView = itemView.findViewById(R.id.text_title)

        val messageTextView: TextView = itemView.findViewById(R.id.text_message)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationViewHolder {

        val itemView = LayoutInflater.from(parent.context)

            .inflate(R.layout.item_notification, parent, false)

        return NotificationViewHolder(itemView)

    }

    override fun onBindViewHolder(holder: NotificationViewHolder, position: Int) {

        val notification = notificationsList[position]

        holder.titleTextView.text = notification.title

        holder.messageTextView.text = notification.message

    }

    override fun getItemCount(): Int {

        return notificationsList.size

    }

}

