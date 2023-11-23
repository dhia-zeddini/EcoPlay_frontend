package com.example.ecoplay_front

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.IBinder

class MusicService : Service() {
    private lateinit var mediaPlayer: MediaPlayer

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        // Initialize your media player
        mediaPlayer = MediaPlayer.create(this, R.raw.background_music_1)
        mediaPlayer.isLooping = true // Set looping if you want the music to loop
        mediaPlayer.start() // Start playing
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (!mediaPlayer.isPlaying) {
            mediaPlayer.start()
        }
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        if (mediaPlayer.isPlaying) {
            mediaPlayer.stop()
        }
        mediaPlayer.release()
    }
}