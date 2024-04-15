package com.awesome.flickrsearch.components

import com.awesome.flickrsearch.di.repos.ImageSearcher
import com.googlecode.flickrjandroid.Flickr
import com.googlecode.flickrjandroid.photos.PhotosInterface
import com.googlecode.flickrjandroid.photos.SearchParameters
import java.util.Date

class FlickrWrapper : ImageSearcher {
    private val photosInterface: PhotosInterface
    private var cachedPhotoResult: PhotoInfoResult? = null
    private var cachedScrollIndex: Int = 0
    private var cachedSearchTags = ""
    private var cachedPhotoResultsList = mutableListOf<PhotoInfoResult>()


    init {
        val flickr = Flickr("27e8dae64914822e279060de8a182f62")
        photosInterface = flickr.photosInterface
    }

    override suspend fun getPhotosByTag(tags: String, page: Int, numImagePerPage: Int, onPhotoUrlResults: (photoList: ArrayList<PhotoInfoResult>) -> Unit) {
        val params = SearchParameters()
        params.tags = arrayOf(tags)
        if(cachedSearchTags !== tags ) {
            cachedPhotoResultsList.clear()
        }
        cachedSearchTags = tags
        val photoList = ArrayList<PhotoInfoResult>()
        val photoListFromNetwork = photosInterface.search(params, numImagePerPage, page)
        for (photo in photoListFromNetwork) {
            //Log.d("Photo","OnPhoto Result ${photo.url}")
            val id = photo.id
            photoList.add(
                PhotoInfoResult(
                    id,
                    photo.dateTaken,
                    photo.datePosted,
                    photo.description,
                    photo.largeUrl.toString(),
                    photo.mediumUrl.toString(),
                    photo.title,
                )
            )
        }
        cachedPhotoResultsList.addAll(photoList)
        onPhotoUrlResults(photoList)
    }

    override fun cachePhotoResult(result: PhotoInfoResult, scrollIndex: Int) {
        cachedPhotoResult = result
        cachedScrollIndex = scrollIndex
    }
    override fun getCachedPhotoResult(): PhotoInfoResult? {
        return cachedPhotoResult
    }

    override fun getCachedPhotoResultsList(): List<PhotoInfoResult> {
        return cachedPhotoResultsList
    }

    override fun getCachedSearchTerms(): String {
        return cachedSearchTags
    }

    override fun getCachedScrollIndex(): Int {
        return cachedScrollIndex
    }
}

data class PhotoInfoResult(val id: String, val dateTaken: Date?, val datePosted: Date?, val description: String?, val largeImageUrl: String, val mediumImageUrl: String, val title: String?)