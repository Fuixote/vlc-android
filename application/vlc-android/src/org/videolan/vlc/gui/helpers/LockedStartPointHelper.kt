package org.videolan.vlc.gui.helpers

import android.content.SharedPreferences
import android.net.Uri
import androidx.core.content.edit

object LockedStartPointHelper {
    private const val START_POINT_PREFIX = "locked_start_point:"

    fun getLockedStartTime(settings: SharedPreferences, uri: Uri?): Long {
        return uri?.let { settings.getLong(preferenceKey(it), 0L) } ?: 0L
    }

    fun saveLockedStartTime(settings: SharedPreferences, uri: Uri, time: Long) {
        settings.edit {
            putLong(preferenceKey(uri), time.coerceAtLeast(0L))
        }
    }

    fun applyLockedStartTime(startTime: Long, lockedStartTime: Long): Long {
        return if (lockedStartTime > startTime) lockedStartTime else startTime
    }

    fun parseTimeMillis(value: String): Long? {
        val parts = value.trim().split(":")
        if (parts.isEmpty() || parts.size > 3) return null
        val numbers = parts.map { part ->
            if (part.isBlank() || part.any { !it.isDigit() }) return null
            part.toLongOrNull() ?: return null
        }
        if (numbers.size > 1 && numbers.last() >= 60) return null
        if (numbers.size == 3 && numbers[1] >= 60) return null
        val seconds = when (numbers.size) {
            1 -> numbers[0]
            2 -> numbers[0] * 60L + numbers[1]
            else -> numbers[0] * 3600L + numbers[1] * 60L + numbers[2]
        }
        return seconds * 1000L
    }

    private fun preferenceKey(uri: Uri) = "$START_POINT_PREFIX$uri"
}
