package com.hiscycleguide.android.model

data class ArticleModel(
    var imageUrl: String = "imageUrl",
    var description: String = "",
    var title: String = "",
    var isMarked: Boolean = false,
    var status: String  = "Un Published",
    var byname: String  = "By Jone Doe",
    var tag: List<String> = arrayListOf(),
    var type: String = "Her body",
    var day: String = "Day 1"
) {
    fun toJson(): HashMap<String, String> {
        val map = hashMapOf<String, String>()

        map["imageUrl"] = imageUrl
        map["description"] = description
        map["title"] = title
        map["tag"] = tag.toString()
        map["type"] = type

        return map
    }
}