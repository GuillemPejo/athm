package me.guillem.athm2app.image_viewer

/**
 * Stas Parshin
 * 05 November 2015
 */
abstract class DefaultCallback : EasyImage.Callbacks {

    override fun onImagePickerError(error: Throwable, source: MediaSource) {}

    override fun onCanceled(source: MediaSource) {}
}
