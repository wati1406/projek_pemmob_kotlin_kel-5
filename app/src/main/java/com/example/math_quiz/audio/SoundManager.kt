package com.example.math_quiz.audio

import android.content.Context
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.media.SoundPool
import com.example.math_quiz.R

/**
 * SoundManager — pengelola audio terpusat untuk seluruh aplikasi.
 *
 * - [bgmPlayer]   : MediaPlayer untuk background music (looping, global_audio.mp3).
 *                   Di-stop/replace ketika halaman result muncul (result.mp3).
 * - [soundPool]   : SoundPool untuk SFX pendek (button, correct, incorrect, level).
 *
 * Cara pakai:
 *   SoundManager.init(context)
 *   SoundManager.playBgm()
 *   SoundManager.playSfx(SoundManager.SFX.BUTTON)
 *   SoundManager.release()  // di onDestroy
 */
object SoundManager {

    enum class SFX { BUTTON, CORRECT, INCORRECT, LEVEL }

    private var bgmPlayer: MediaPlayer? = null
    private var resultPlayer: MediaPlayer? = null

    private var soundPool: SoundPool? = null
    private val sfxIds = mutableMapOf<SFX, Int>()
    private var isInitialized = false

    fun init(context: Context) {
        if (isInitialized) return
        isInitialized = true

        // ── SoundPool untuk SFX pendek
        val attrs = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_GAME)
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .build()

        soundPool = SoundPool.Builder()
            .setMaxStreams(4)
            .setAudioAttributes(attrs)
            .build()

        soundPool?.let { sp ->
            sfxIds[SFX.BUTTON]    = sp.load(context, R.raw.button, 1)
            sfxIds[SFX.CORRECT]   = sp.load(context, R.raw.correct_answer, 1)
            sfxIds[SFX.INCORRECT] = sp.load(context, R.raw.incorrect_answer, 1)
            sfxIds[SFX.LEVEL]     = sp.load(context, R.raw.pilih_level, 1)
        }

        // ── MediaPlayer untuk BGM global (looping)
        bgmPlayer = MediaPlayer.create(context, R.raw.global_audio)?.apply {
            isLooping = true
            setVolume(0.6f, 0.6f)
        }
    }

    /** Mulai / lanjutkan background music global */
    fun playBgm() {
        stopResult()
        if (bgmPlayer?.isPlaying == false) {
            bgmPlayer?.start()
        }
    }

    /** Pause background music (misalnya saat app ke background) */
    fun pauseBgm() {
        bgmPlayer?.pause()
    }

    /** Resume background music */
    fun resumeBgm() {
        bgmPlayer?.start()
    }

    /**
     * Ganti BGM dengan result.mp3 (satu kali putar, tidak looping).
     * Setelah selesai, kembali ke BGM global.
     */
    fun playResult(context: Context) {
        bgmPlayer?.pause()
        resultPlayer?.release()
        resultPlayer = MediaPlayer.create(context, R.raw.result)?.apply {
            isLooping = false
            setVolume(0.8f, 0.8f)
            setOnCompletionListener {
                it.release()
                resultPlayer = null
                // Kembali ke BGM global setelah result selesai
                bgmPlayer?.start()
            }
            start()
        }
    }

    /** Hentikan result player (jika user navigasi sebelum selesai) */
    fun stopResult() {
        resultPlayer?.stop()
        resultPlayer?.release()
        resultPlayer = null
    }

    /** Main SFX pendek */
    fun playSfx(sfx: SFX, volume: Float = 1f) {
        val id = sfxIds[sfx] ?: return
        soundPool?.play(id, volume, volume, 1, 0, 1f)
    }

    /** Panggil di Activity.onDestroy() */
    fun release() {
        bgmPlayer?.stop()
        bgmPlayer?.release()
        bgmPlayer = null

        resultPlayer?.stop()
        resultPlayer?.release()
        resultPlayer = null

        soundPool?.release()
        soundPool = null

        sfxIds.clear()
        isInitialized = false
    }
}
