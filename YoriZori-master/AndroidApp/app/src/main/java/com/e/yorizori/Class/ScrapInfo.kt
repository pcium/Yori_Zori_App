package com.e.yorizori.Class

class ScrapInfo(key : String = "", title : String = "", writer : String = "") {
    var key = key
    var title = title
    var writer = writer

    fun has(title : String, writer :String): Boolean{
        return (this.title == title && this.writer == writer)
    }
    fun toSave(): String{
        return "$title,$writer"
    }
}