package org.videolan.vlc.util

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class JavCodeExtractorTest {

    @Test
    fun extractJavCode_normalizesCommonCodes() {
        assertEquals("IPX-001", JavCodeExtractor.extract("IPX001"))
        assertEquals("IPX-001", JavCodeExtractor.extract("IPX-001"))
        assertEquals("SSIS-123", JavCodeExtractor.extract("SSIS 123"))
        assertEquals("ABP-123", JavCodeExtractor.extract("[THZU.CC]ABP123C"))
    }

    @Test
    fun extractJavCode_ignoresNoisePrefixes() {
        assertEquals("IPX-001", JavCodeExtractor.extract("h264-1080p-ipx001"))
        assertEquals("ABP-123", JavCodeExtractor.extract("ABP_123_4K"))
    }

    @Test
    fun extractJavCode_doesNotMatchFc2StyleMultisegmentCode() {
        assertNull(JavCodeExtractor.extract("fc2-ppv-1234567"))
    }

    @Test
    fun buildJdbCollectionUrl_usesNormalizedCodeAsQuery() {
        assertEquals("http://fuixote:1234/collection?q=ktb-111&filter=all", JavCodeExtractor.buildJdbCollectionUrl("KTB-111.mp4"))
    }
}
