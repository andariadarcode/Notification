package com.andariadar.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.app.RemoteInput
import com.andariadar.notification.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        createNotificationChannel()

        binding.showNotification.setOnClickListener {
            createNotification()
        }
    }

    private fun createNotification() {
        val remoteInput: RemoteInput = RemoteInput.Builder(getString(R.string.key_text_reply)).run {
            setLabel(getString(R.string.reply_label))
            build()
        }
        val intent = Intent(this, NotificationReceiver::class.java)

        val replyPendingIntent: PendingIntent =
                PendingIntent.getBroadcast(this,
                        REQUEST_CODE_REPLAY,
                        intent,
                        PendingIntent.FLAG_UPDATE_CURRENT)

        val action: NotificationCompat.Action =
                NotificationCompat.Action.Builder(R.drawable.ic_message,
                        getString(R.string.reply_label), replyPendingIntent)
                        .addRemoteInput(remoteInput)
                        .build()

        val builder = NotificationCompat.Builder(this, getString((R.string.channel_id)))
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle(getString(R.string.title_notification))
                .setContentText(getString(R.string.text_notification))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(replyPendingIntent)
                .setAutoCancel(true)
                .addAction(action)

        with(NotificationManagerCompat.from(this)) {
            notify(NOTIFICATION_ID, builder.build())
        }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelId = getString(R.string.channel_id)
            val channelName = getString(R.string.channel_name)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(channelId, channelName, importance).apply {
                description = getString(R.string.notification_channel_description)
            }
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    companion object {
        const val NOTIFICATION_ID = 101
        const val REQUEST_CODE_REPLAY = 1
    }
}

