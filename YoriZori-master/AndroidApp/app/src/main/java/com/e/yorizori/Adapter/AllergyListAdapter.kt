package com.e.yorizori.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.CheckBox
import android.widget.TextView
import com.e.yorizori.Class.RefrigItem
import com.e.yorizori.R
import java.text.SimpleDateFormat

class AllergyListAdapter (val context: Context, val _items: ArrayList<String>): BaseAdapter() {

    val mContext: Context = context
    val simpleDate : SimpleDateFormat = SimpleDateFormat("yyyy-MM-dd")
    val items = _items
    companion object{
        val selected : ArrayList<String> = arrayListOf()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

        val view: View = LayoutInflater.from(context).inflate(R.layout.hatelist_item, parent, false)

        val item_name = view.findViewById<TextView>(R.id.item_name)


        val item = items[position]
        item_name.text = item

        val checkBox = view.findViewById<CheckBox>(R.id.item_check)

        checkBox.setOnCheckedChangeListener { buttonView, isChecked ->
            var check = true
            if (isChecked){
                for (i in 0 until selected.size){
                    if (selected[i] == items[position]){
                        check = false
                        break
                    }
                }
                if (check)
                    selected.add(items[position])
            }
            else{
                for (i in 0 until selected.size){
                    if (selected[i] == items[position]){
                        selected.removeAt(i)
                        break
                    }
                }
            }
//            if (selected.size == 0){
//                del_button.visibility = View.INVISIBLE
//            }
//            else{
//                del_button.visibility = View.VISIBLE
//            }
        }

        var check = true
        for (i in 0 until selected.size){
            if (selected[i] == items[position]){
                checkBox.isChecked = true
                check = false
                break
            }
        }
        if (check){
            checkBox.isChecked = false
        }

        return view
    }

    override fun getItem(position: Int): Any {
        return items[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return items.size
    }

}