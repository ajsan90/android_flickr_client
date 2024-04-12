

import com.googlecode.flickrjandroid.Flickr
import com.googlecode.flickrjandroid.photos.PhotosInterface
import com.googlecode.flickrjandroid.photos.SearchParameters

class FlickrWrapper(
) {
    private val photosInterface: PhotosInterface

    init {
        val flickr = Flickr("YOUR-FLICKR-API-KEY")
        photosInterface = flickr.photosInterface
    }

    fun getPhotosByTag(tags: String, page: Int, numImagePerPage: Int) {
       // TODO("not implemented")
        val params = SearchParameters()
        params.tags = arrayOf(tags)


        val photoListFromNetwork = photosInterface.search(params, numImagePerPage, page)
        val photoList = ArrayList<Triple<String, String, String>>()
        for (photo in photoListFromNetwork) {
            val id = photo.id
            photoList.add(
                Triple(
                    id,
                    photo.mediumUrl.toString(),
                    photo.title,
                )
            )
        }

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