package android.ifeanyi.read.core.services

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.ifeanyi.read.R
import android.os.Bundle
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.media.app.NotificationCompat as MediaNotification

const val ACTION_FORWARD = "ACTION_FORWARD"
const val ACTION_REWIND = "ACTION_REWIND"

object NotificationService {
    fun showMediaStyleNotification(context: Context) {
        println("SHOWING========")
        val channelId = "TTS_NOTIFICATION_CHANNEL"
        val session = MediaSessionCompat(context, "MediaSession")

        val callback = object : MediaSessionCompat.Callback() {
            override fun onCustomAction(action: String?, extras: Bundle?) {
                when (action) {
                    "ACTION_FORWARD" -> {
                        println("Forward")
                    }
                    "ACTION_REWIND" -> {
                        println("Rewind")
                    }
                }
            }

            override fun onPlay() {
                println("Play")
            }

            override fun onPause() {
                println("Pause")
            }

            override fun onFastForward() {
                println("Forward")
            }
        }

        val playbackStateBuilder = PlaybackStateCompat.Builder()
            .setActions(PlaybackStateCompat.ACTION_PLAY_PAUSE)
            .setState(PlaybackStateCompat.STATE_PAUSED, 10000, 1f)
            .addCustomAction(ACTION_REWIND,
                "Rewind",
                R.drawable.round_fast_rewind_24)
            .addCustomAction(ACTION_FORWARD,
                "Forward",
                R.drawable.round_fast_forward_24)

        val metadata = MediaMetadataCompat.Builder()
            .putString(MediaMetadataCompat.METADATA_KEY_TITLE, "File Title")
            .putString(MediaMetadataCompat.METADATA_KEY_ARTIST, "File Subtitle")
            .putLong(MediaMetadataCompat.METADATA_KEY_DURATION, 30000)
            .build()

        session.setPlaybackState(playbackStateBuilder.build())
        session.setCallback(callback)
        session.setMetadata(metadata)

        val builder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.round_record_voice_over_24)
            .setStyle(MediaNotification.MediaStyle().setMediaSession(session.sessionToken))
            .setOngoing(true)
            .setAutoCancel(false)
            .setShowWhen(false)


        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            println("NOT GRANTED")
            return
        }

        val notification = builder.build()
        val notificationManagerCompat = NotificationManagerCompat.from(context)

        createNotificationChannel(notificationManagerCompat)

        notificationManagerCompat.notify(0, notification)
    }

    private fun createNotificationChannel(notificationManagerCompat: NotificationManagerCompat) {
        val channelId = "TTS_NOTIFICATION_CHANNEL"
        val channelName = "Text To Speech Notification Channel"
        val channelDescription = "Channel for TTS notifications"
        val importance = NotificationManager.IMPORTANCE_LOW
        val notificationChannel = NotificationChannel(channelId, channelName, importance).apply {
            description = channelDescription
        }

        notificationManagerCompat.createNotificationChannel(notificationChannel)
    }
}