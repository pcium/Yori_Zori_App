package com.e.yorizori.Class

import java.util.*

class Recipe(
    cook_title: String,
    recipe: ArrayList<String>,
    tag: ArrayList<String>,
    pics: ArrayList<String>,
    writer_UID: String,
    ings: ArrayList<Pair<String,String>>,
    like_num: ArrayList<Int>,
    scrap_num: Int
){
    /*구조를 만들어 주세요~!*/
    var cook_title : String = cook_title // 제목
        get() = field
        set(value) {
            field = value
        }
    var recipe : ArrayList<String> = recipe// 조리법
        get() = field
        set(value) {
            field = value
        }
    var tag : ArrayList<String> = tag//태그
        get() = field
        set(value) {
            field = value
        }
    var pics : ArrayList<String> = pics//사진
        get() = field
        set(value) {
            field = value
        }
    var writer_UID : String = writer_UID//작성자 식별자
        get() = field
        set(value) {
            field = value
        }
    var ings : ArrayList<Pair<String,String>> = ings//재료
        get() = field
        set(value) {
            field = value
        }
    var scrap_num :Int = scrap_num//스크랩 수
        get() = field
        set(value) {
            field = value
        }
    var like_num : ArrayList<Int> = like_num//추천수
    get() = field
        set(value) {
            field = value
        }

    var date : Date? = Date()//작성시간

    constructor() : this("",arrayListOf<String>(),arrayListOf<String>(),arrayListOf<String>(),"",arrayListOf<Pair<String,String>>(),arrayListOf<Int>(0,0,0),0)
    constructor(cook_title:String,
                recipe:ArrayList<String>,
                tag:ArrayList<String>,
                pics:ArrayList<String>,
                writer_UID:String,
                ings: ArrayList<Pair<String,String>>) :
            this(cook_title,recipe,tag, pics, writer_UID, ings, arrayListOf<Int>(0,0,0), 0)

    fun isValid(): Int{
        if(cook_title == "")
            return 1
        if(recipe.isEmpty())
            return 2
        if(pics.isEmpty())
            return 3
        if(ings.isEmpty())
            return 4
        return 0
    }

}
