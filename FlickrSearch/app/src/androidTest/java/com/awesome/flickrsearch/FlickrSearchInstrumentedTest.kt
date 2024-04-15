package com.awesome.flickrsearch

import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.awesome.flickrsearch.components.FlickrWrapper
import com.awesome.flickrsearch.components.PhotoInfoResult

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import java.time.Instant
import java.util.Date

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class FlickrSearchInstrumentedTest {
    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.awesome.flickrsearch", appContext.packageName)
    }

    @Test
    fun testFlickrWrapperCacheAsImageSearcherCache() {
        val resultId = "999"
        var imageSearcher = FlickrWrapper()
        imageSearcher.cachePhotoResult(
            PhotoInfoResult(
                resultId, datePosted = Date.from(Instant.now()),
                dateTaken = Date.from(Instant.now()),
                description = "description",
                largeImageUrl = "image.com/large",
                mediumImageUrl = "image.com/medium",
                title = "title1"
            )
        )
        //simulate travel between results page and details page
        //which is reliant upon cached photo capability
        assertEquals(imageSearcher.getCachedPhotoResult()!!.id, resultId)
    }
}