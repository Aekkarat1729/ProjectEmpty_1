package com.example.projectempty

class MphotoModel(var title: String?, var Image: String?,var key: String?
    ,var detail: String?,var email: String?) {
    // Default constructor
    constructor() : this(null, null, null, null,null)

    fun toMap(): Map<String, Any> {
        val result = HashMap<String, Any>()
        result.put("title", title!!)
        result.put("Image", Image!!)
        result.put("key", key!!)
        result.put("detail", detail!!)
        result.put("email",email!!)
        return result
    }
}
