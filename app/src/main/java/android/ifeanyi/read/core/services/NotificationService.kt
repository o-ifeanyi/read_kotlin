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
const val CHANNEL_ID = "TTS_NOTIFICATION_CHANNEL"

object NotificationService {
    private lateinit var _appContext: Context
    private lateinit var _session: MediaSessionCompat

    fun init(context: Context) {
        _appContext = context
        _session = MediaSessionCompat(_appContext, "MediaSession")
    }

    fun destroy() {
        val notificationManagerCompat = NotificationManagerCompat.from(_appContext)
        notificationManagerCompat.cancel(0)
        _session.release()
    }

    fun showMediaStyleNotification() {
        val state = SpeechService.state.value
        if (state.model == null) return

        if (ActivityCompat.checkSelfPermission(
                _appContext,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            println("NOT GRANTED")
            return
        }

        val callback = object : MediaSessionCompat.Callback() {
            override fun onCustomAction(action: String?, extras: Bundle?) {
                when (action) {
                    ACTION_FORWARD -> {
                        SpeechService.forward()
                    }

                    ACTION_REWIND -> {
                        SpeechService.rewind()
                    }
                }
            }

            override fun onPlay() {
                SpeechService.play()
            }

            override fun onPause() {
                SpeechService.pause()
            }
        }

        val playbackStateBuilder = PlaybackStateCompat.Builder()
            .setActions(PlaybackStateCompat.ACTION_PLAY_PAUSE)
            .setState(
                if (state.isPlaying) PlaybackStateCompat.STATE_PLAYING else PlaybackStateCompat.STATE_PAUSED,
                (state.progress * 100).toLong(),
                1f
            )
            .addCustomAction(
                ACTION_REWIND,
                "Rewind",
                R.drawable.round_replay_10_24
            )
            .addCustomAction(
                ACTION_FORWARD,
                "Forward",
                R.drawable.round_forward_10_24
            )
            .build()

        val file = state.model
        val metadata = MediaMetadataCompat.Builder()
            .putString(MediaMetadataCompat.METADATA_KEY_TITLE, file.name)
            .putString(MediaMetadataCompat.METADATA_KEY_ARTIST, file.type.name.lowercase())
            .putLong(MediaMetadataCompat.METADATA_KEY_DURATION, 100)
            .build()

        _session.setPlaybackState(playbackStateBuilder)
        _session.setCallback(callback)
        _session.setMetadata(metadata)

        val builder = NotificationCompat.Builder(_appContext, CHANNEL_ID)
            .setSmallIcon(R.drawable.round_record_voice_over_24)
            .setStyle(MediaNotification.MediaStyle().setMediaSession(_session.sessionToken))
            .setOngoing(true)
            .setAutoCancel(false)
            .setShowWhen(false)

        val notification = builder.build()
        val notificationManagerCompat = NotificationManagerCompat.from(_appContext)

        createNotificationChannel(notificationManagerCompat)

        notificationManagerCompat.notify(0, notification)
    }

    private fun createNotificationChannel(notificationManagerCompat: NotificationManagerCompat) {
        val channelName = "Text To Speech Notification Channel"
        val channelDescription = "Channel for TTS notifications"
        val importance = NotificationManager.IMPORTANCE_LOW
        val notificationChannel = NotificationChannel(CHANNEL_ID, channelName, importance).apply {
            description = channelDescription
        }

        notificationManagerCompat.createNotificationChannel(notificationChannel)
    }
}