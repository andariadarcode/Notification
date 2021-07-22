package com.andariadar.notification

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.RemoteInput


class NotificationReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {

        val text = RemoteInput.getResultsFromIntent(intent)?.getCharSequence(context?.getString(R.string.key_text_reply))
        Toast.makeText(context, "Message: $text", Toast.LENGTH_LONG).show()

        val repliedNotification  = context?.getString(R.string.channel_id)?.let {
            NotificationCompat.Builder(context, it)
                .setSmallIcon(R.drawable.ic_message)
                .setContentTitle("Message sent")
                .build()
        }

        val notificationManager = context!!.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(MainActivity.NOTIFICATION_ID, repliedNotification)
    }
}



