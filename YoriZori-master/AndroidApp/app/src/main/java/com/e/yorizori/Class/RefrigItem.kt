package com.e.yorizori.Class

import com.e.yorizori.Class.DateFormatter.Companion.simpleDate


class RefrigItem(){
    /*구조를 만들어 주세요~!*/
    var item : String = "" // 재료 이름
    var due = simpleDate.parse("9999-12-31")

    constructor(item_: String): this() {
        item = item_
        due = simpleDate.parse("9999-12-31")
    }

    constructor(item_: String, due_ : String): this(item_) {
        item = item_
        this.due = simpleDate.parse(due_)
    }

    fun print_due() : String {
        var return_me : String
        if (due == simpleDate.parse("9999-12-31"))
            return_me = ""
        else
            return_me = simpleDate.format(due)
        return return_me
    }

    fun get_val() : Int {
        if (due == simpleDate.parse("9999-12-31"))
            return 100000000
        else{
            var arr = simpleDate.format(due).split("-")
            return arr[0].toInt()*10000 + arr[1].toInt()*100 + arr[2].toInt()
        }
    }

    companion object {
        fun sort(items : MutableList<RefrigItem>) {
            items.sortWith(compareBy({it.get_val()}, {it.item}))
        }
    }
}