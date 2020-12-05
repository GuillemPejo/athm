package me.guillem.athm2app.image_viewer

class EasyImageException(message: String, cause: Throwable?) : Throwable(message, cause) {
    constructor(cause: Throwable) : this(cause.message ?: "", cause)
    constructor(message: String) : this(message, null)
}