package com.awesome.flickrsearch.components

import android.util.Log
import com.googlecode.flickrjandroid.Flickr
import com.googlecode.flickrjandroid.photos.PhotosInterface
import com.googlecode.flickrjandroid.photos.SearchParameters

class FlickrWrapper(
) {
    private val photosInterface: PhotosInterface


    init {
        val flickr = Flickr("27e8dae64914822e279060de8a182f62")
        photosInterface = flickr.photosInterface
    }

    suspend fun getPhotosByTag(tags: String, page: Int, numImagePerPage: Int, onPhotoUrlResults: (photoList: ArrayList<PhotoUrlResult>) -> Unit) {
        val params = SearchParameters()
        params.tags = arrayOf(tags)
        val photoList = ArrayList<PhotoUrlResult>()
        val photoListFromNetwork = photosInterface.search(params, numImagePerPage, page)
        for (photo in photoListFromNetwork) {
            Log.d("Photo","OnPhoto Result ${photo.url}")
            val id = photo.id
            photoList.add(
                PhotoUrlResult(
                    id,
                    photo.mediumUrl.toString(),
                    photo.title,
                )
            )
        }
        onPhotoUrlResults(photoList)
    }

    fun getPhotoInfo(photoId: String?) {
        TODO("not implemented")
        val photo = photosInterface.getInfo(photoId, null)
//        photo.id,
//        photo.largeUrl,
//        photo.title,
//        photo.description,
//        photo.dateTaken,
//        photo.datePosted

    }
}

data class PhotoUrlResult(val id: String, val url: String, val title: String)