package com.example.projectempty

class MphotoModel {
    var title:String? = null
    var Image:String? = null
    constructor(title:String?,Image:String?){
        this.title = title
        this.Image = Image
    }
    fun toMap(): Map<String, Any>{
        val result = HashMap<String, Any>()
        result.put("title",title!!)
        result.put("Image",Image!!)
        return  result
    }
}