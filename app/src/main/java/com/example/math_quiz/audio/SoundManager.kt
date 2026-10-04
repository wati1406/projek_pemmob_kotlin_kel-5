package com.example.math_quiz.audio

import android.content.Context
import android.content.SharedPreferences
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.media.SoundPool
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
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

    enum class SFX { BUTTON, CORRECT, INCORRECT, LEVEL, TIME_UP }

    private const val PREFS_NAME = "math_quiz_audio_prefs"
    private const val KEY_SFX_ENABLED = "sfx_enabled"
    private const val KEY_MUSIC_ENABLED = "music_enabled"

    private var appContext: Context? = null
    private var prefs: SharedPreferences? = null

    /** State reaktif untuk Sound Effects */
    private var _isSoundEffectsEnabled by mutableStateOf(true)
    val isSoundEffectsEnabled: Boolean
        get() = _isSoundEffectsEnabled

    /** State reaktif untuk Background Music */
    private var _isMusicEnabled by mutableStateOf(true)
    val isMusicEnabled: Boolean
        get() = _isMusicEnabled

    private var bgmPlayer: MediaPlayer? = null
    private var resultPlayer: MediaPlayer? = null

    private var soundPool: SoundPool? = null
    private val sfxIds = mutableMapOf<SFX, Int>()
    private var isInitialized = false

    fun init(context: Context) {
        appContext = context.applicationContext

        if (prefs == null) {
            val sp = context.applicationContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            prefs = sp
            _isSoundEffectsEnabled = sp.getBoolean(KEY_SFX_ENABLED, true)
            _isMusicEnabled = sp.getBoolean(KEY_MUSIC_ENABLED, true)
        }

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
            sfxIds[SFX.TIME_UP]   = sp.load(context, R.raw.time_up, 1)
        }

        // ── MediaPlayer untuk BGM global (looping)
        bgmPlayer = MediaPlayer.create(context, R.raw.global_audio)?.apply {
            isLooping = true
            setVolume(0.6f, 0.6f)
        }
    }

    /** Ubah state Sound Effects dan simpan ke SharedPreferences */
    fun setSoundEffectsEnabled(enabled: Boolean) {
        _isSoundEffectsEnabled = enabled
        prefs?.edit()?.putBoolean(KEY_SFX_ENABLED, enabled)?.apply()
    }

    /** Ubah state Background Music dan simpan ke SharedPreferences */
    fun setMusicEnabled(enabled: Boolean) {
        _isMusicEnabled = enabled
        prefs?.edit()?.putBoolean(KEY_MUSIC_ENABLED, enabled)?.apply()
        if (!enabled) {
            pauseBgm()
            stopResult()
        } else {
            playBgm()
        }
    }

    /** Mulai / lanjutkan background music global */
    fun playBgm() {
        if (!isMusicEnabled) return
        stopResult()
        if (bgmPlayer == null && appContext != null) {
            bgmPlayer = MediaPlayer.create(appContext, R.raw.global_audio)?.apply {
                isLooping = true
                setVolume(0.6f, 0.6f)
            }
        }
        if (bgmPlayer?.isPlaying == false) {
            bgmPlayer?.start()
        }
    }

    /** Pause background music (misalnya saat app ke background atau music dimatikan) */
    fun pauseBgm() {
        if (bgmPlayer?.isPlaying == true) {
            bgmPlayer?.pause()
        }
    }

    /** Resume background music */
    fun resumeBgm() {
        if (!isMusicEnabled) return
        if (bgmPlayer == null && appContext != null) {
            bgmPlayer = MediaPlayer.create(appContext, R.raw.global_audio)?.apply {
                isLooping = true
                setVolume(0.6f, 0.6f)
            }
        }
        if (bgmPlayer?.isPlaying == false) {
            bgmPlayer?.start()
        }
    }

    /**
     * Ganti BGM dengan result.mp3 (satu kali putar, tidak looping).
     * Setelah selesai, kembali ke BGM global jika musik masih aktif.
     */
    fun playResult(context: Context) {
        if (!isMusicEnabled) return
        pauseBgm()
        stopResult()
        resultPlayer = MediaPlayer.create(context, R.raw.result)?.apply {
            isLooping = false
            setVolume(0.8f, 0.8f)
            setOnCompletionListener {
                it.release()
                resultPlayer = null
                // Kembali ke BGM global setelah result selesai (hanya jika music masih ON)
                if (isMusicEnabled) {
                    bgmPlayer?.start()
                }
            }
            start()
        }
    }

    /** Hentikan result player (jika user navigasi sebelum selesai) */
    fun stopResult() {
        try {
            if (resultPlayer?.isPlaying == true) {
                resultPlayer?.stop()
            }
            resultPlayer?.release()
        } catch (_: Exception) {}
        resultPlayer = null
    }

    /** Main SFX pendek */
    fun playSfx(sfx: SFX, volume: Float = 1f) {
        if (!isSoundEffectsEnabled) return
        val id = sfxIds[sfx] ?: return
        soundPool?.play(id, volume, volume, 1, 0, 1f)
    }

    /** Panggil di Activity.onDestroy() */
    fun release() {
        try {
            if (bgmPlayer?.isPlaying == true) {
                bgmPlayer?.stop()
            }
            bgmPlayer?.release()
        } catch (_: Exception) {}
        bgmPlayer = null

        try {
            if (resultPlayer?.isPlaying == true) {
                resultPlayer?.stop()
            }
            resultPlayer?.release()
        } catch (_: Exception) {}
        resultPlayer = null

        soundPool?.release()
        soundPool = null

        sfxIds.clear()
        isInitialized = false
    }
}
