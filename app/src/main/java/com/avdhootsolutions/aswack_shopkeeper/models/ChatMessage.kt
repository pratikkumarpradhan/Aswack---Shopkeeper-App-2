package com.avdhootsolutions.aswack_shopkeeper.models

import java.util.*

class ChatMessage{

    var id : String = ""
    var text: String = ""
    var user: String = ""
    var imageId = ""
    var imageUrl = ""
    var videoId = ""
    var videoUrl = ""
    val timestamp: Date = Calendar.getInstance().time
    var showDate = false

    var latitude = ""
    var longitude = ""
}