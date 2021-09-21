package com.clonecoding.clouldmessagealarm

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService : FirebaseMessagingService() {

    /**
     * 토큰 갱신됐을 때 토큰 변경을 서버에 알려주거나 함
     * 수명이 긴 서비스를 운영할려면 신경 써야 함
     * 짧으므로 따로 처리하지 않음
     */
    override fun onNewToken(p0: String) {
        super.onNewToken(p0)
    }

    override fun onMessageReceived(p0: RemoteMessage) {
        super.onMessageReceived(p0)

        createNotificationChannel()

        val type = p0.data["type"]?.let {
            NotificationType.valueOf(it)
        }
        val title = p0.data["title"]
        val message = p0.data["message"]

        type ?: return
        title ?: return
        message ?: return

        NotificationManagerCompat.from(this)
            .notify(type.id, this.createNotification(type, title, message))
    }

    /**
     * Create notification channel
     */
    private fun createNotificationChannel() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            )
            channel.description = CHANNEL_DESCRIPTION

            (getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager)
                .createNotificationChannel(channel)
        }
    }

    /**
     * Create notification
     */
    private fun createNotification(
        type: NotificationType,
        title: String,
        text: String
    ): Notification {

        val intent = Intent(this, MainActivity::class.java).apply {
            putExtra("notificationType", "${type.title} 타입")
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent: PendingIntent = PendingIntent.getActivity(this, type.id, intent, 0)

        val notificationBuilder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_baseline_notifications_24)
            .setContentTitle(title)
            .setContentText(text)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        when (type) {

            NotificationType.NORMAL -> Unit
            NotificationType.EXPANDABLE -> {
                notificationBuilder.setStyle(
                    NotificationCompat.BigTextStyle()
                        .bigText(
                            "😀 😃 😄 😁 😆 😅 😂 🤣 🥲 ☺" +
                                    "😊 😇 🙂 🙃 😉 😌 😍 🥰 😘 😗 😙 😚" +
                                    " 😋 😛 😝 😜 🤪 🤨 🧐 🤓 😎 🥸 🤩" +
                                    " 🥳 😏 😒 😞 😔 😟 😕 🙁 😣 😖 😫 😩" +
                                    " 🥺 😢 😭 😤 😠 😡 🤬 🤯 😳 🥵 🥶" +
                                    " 😱 😨 😰 😥 😓 🤗 🤔 🤭 🤫 🤥 😶 😐 😑"
                        )
                )
            }
            NotificationType.CUSTOM -> {
                notificationBuilder
                    .setStyle(
                        NotificationCompat.DecoratedCustomViewStyle()
                    )
                    .setCustomContentView(
                        RemoteViews(packageName, R.layout.view_custom_notification)
                            .apply {
                                setTextViewText(R.id.title, title)
                                setTextViewText(R.id.message, text)
                            }
                    )
            }
        }
        return notificationBuilder.build()
    }

    // 다중 언어일 경우 이 값을 string 에 저장하고 호출
    companion object {
        private const val CHANNEL_NAME = "Emoji Party"
        private const val CHANNEL_DESCRIPTION = "Emoji Party를 위한 채널"
        private const val CHANNEL_ID = "Channel Id"
    }
}