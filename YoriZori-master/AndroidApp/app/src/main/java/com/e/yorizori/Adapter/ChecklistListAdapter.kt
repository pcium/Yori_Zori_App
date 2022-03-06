package com.e.yorizori.Adapter

import android.content.Context
import android.graphics.Color
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.CheckBox
import android.widget.TextView
import com.e.yorizori.Class.RefrigItem
import com.e.yorizori.R
import androidx.core.view.isVisible
import java.text.SimpleDateFormat
import java.util.*
import android.view.LayoutInflater as LayoutInflater1

class ChecklistListAdapter (val context: Context, val _items: MutableList<RefrigItem>): BaseAdapter() {

    val mContext: Context = context
    val simpleDate : SimpleDateFormat = SimpleDateFormat("yyyy-MM-dd")
    val items : MutableList<RefrigItem> = _items
    companion object{
        val selected : MutableList<RefrigItem> = mutableListOf()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

        val view: View = LayoutInflater1.from(context).inflate(R.layout.checklist_item, parent, false)


        val item_name = view.findViewById<TextView>(R.id.item_name)
        val due_date = view.findViewById<TextView>(R.id.due_date)

        RefrigItem.sort(items)

        val item = items[position]
        item_name.text = item.item
        due_date.text = item.print_due()

        //var tmp = simpleDate.format(item.due).split("-")[0].toInt()

        var tmp : Date = item.due
        val cal : Date = Calendar.getInstance().time

        val c1 = Calendar.getInstance()
        val c2 = Calendar.getInstance()

        c1.setTime(tmp)
        c2.setTime(cal)

        c2.add(Calendar.DATE, 10)
        val ten_days_later = (c1.compareTo(c2) <= 0)

        val due_end = tmp.before(cal)

        if (due_end)
            due_date.setTextColor(Color.rgb(255, 0, 6))
        else if (ten_days_later)
            due_date.setTextColor(Color.MAGENTA)

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
