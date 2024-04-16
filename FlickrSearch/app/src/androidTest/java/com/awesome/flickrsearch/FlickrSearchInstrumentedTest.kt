package com.awesome.flickrsearch

import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.awesome.flickrsearch.components.FlickrWrapper
import com.awesome.flickrsearch.components.PhotoInfoResult
import kotlinx.coroutines.runBlocking

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
    fun confirmPhotoResultCache() {
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
            ), 0
        )
        //simulate travel between results page and details page
        //which is reliant upon cached photo capability
        assertEquals(imageSearcher.getCachedPhotoResult()!!.id, resultId)
    }

    @Test
    fun confirmPhotoResultsListGrowsWithSameSearchTerms() {
        var imageSearcher = FlickrWrapper()
        var id1 = ""
        var id2 = ""

        runBlocking {
            imageSearcher.getPhotosByTag(
                tags = "dogs",
                page = 0,
                numImagePerPage = 10,
                onPhotoUrlResults = { photoList ->
                    id1 = photoList.first().id
                })
            imageSearcher.getPhotosByTag(
                tags = "dogs",
                page = 1,
                numImagePerPage = 10,
                onPhotoUrlResults = { photoList ->
                    id2 = photoList.first().id
                })
        }

        //simulate travel between results page and details page
        //which is reliant upon cached photo capability
        assertEquals(id1, id2)
    }

    @Test
    fun confirmPhotoResultsListResetsWithDifferentSearchTerms() {
        var imageSearcher = FlickrWrapper()
        var id1 = ""
        var id2 = ""

        runBlocking {
            imageSearcher.getPhotosByTag(
                tags = "dogs",
                page = 0,
                numImagePerPage = 10,
                onPhotoUrlResults = { photoList ->
                    id1 = photoList.first().id
                })
            imageSearcher.getPhotosByTag(
                tags = "cats",
                page = 1,
                numImagePerPage = 10,
                onPhotoUrlResults = { photoList ->
                    id2 = photoList.first().id
                })
        }

        //simulate travel between results page and details page
        //which is reliant upon cached photo capability
        assertNotEquals(id1, id2)
    }

    @Test
    fun confirmPhotoResultsListSize() {
        var imageSearcher = FlickrWrapper()
        var pageSize = 10;
        var listSize = 0;
        runBlocking {
            imageSearcher.getPhotosByTag(
                tags = "dogs",
                page = 0,
                numImagePerPage = pageSize,
                onPhotoUrlResults = { photoList ->
                    listSize = photoList.size
                })
        }

        //simulate travel between results page and details page
        //which is reliant upon cached photo capability
        assertEquals(pageSize, listSize)
    }
}