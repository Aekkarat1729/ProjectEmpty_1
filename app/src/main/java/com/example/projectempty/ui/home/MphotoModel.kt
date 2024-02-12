package com.example.projectempty

class MphotoModel(var title: String?, var Image: String?) {
    // Default constructor
    constructor() : this(null, null)

    fun toMap(): Map<String, Any> {
        val result = HashMap<String, Any>()
        result.put("title", title!!)
        result.put("image", Image!!)
        return result
    }
}
