package org.videolan.vlc.util

import java.util.Locale

object JavCodeExtractor {
    private val noisePrefixes = setOf(
        "H264", "H265", "X264", "X265", "HEVC", "AVC", "WEB", "DL", "HD", "FHD",
        "UHD", "HDR", "SD", "AAC", "DTS", "AC", "MP4", "MKV", "SRT", "SUB"
    )
    private val codePattern = Regex("""(?<![A-Z0-9])([A-Z]{2,10})[\s._-]*([0-9]{2,6})(?![0-9])""", RegexOption.IGNORE_CASE)
    private val nonCodeChars = Regex("""[^A-Z0-9]""")

    fun extract(source: String?): String? {
        if (source.isNullOrBlank()) return null
        val upperText = source.uppercase(Locale.US)
        findCode(upperText)?.let { return it }
        return findCode(nonCodeChars.replace(upperText, ""))
    }

    fun buildJdbCollectionUrl(source: String?): String? {
        return extract(source)?.let { code ->
            "http://fuixote:1234/collection?q=${code.lowercase(Locale.US)}&filter=all"
        }
    }

    private fun findCode(source: String): String? {
        for (match in codePattern.findAll(source)) {
            normalize(match.groupValues[1], match.groupValues[2])?.let { return it }
        }
        return null
    }

    private fun normalize(prefix: String, number: String): String? {
        val cleanPrefix = prefix.uppercase(Locale.US).filter { it in 'A'..'Z' }
        val cleanNumber = number.filter { it in '0'..'9' }
        if (cleanPrefix.isEmpty() || cleanNumber.isEmpty() || cleanPrefix in noisePrefixes) return null
        return "$cleanPrefix-$cleanNumber"
    }
}
