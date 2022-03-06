package com.e.yorizori.Adapter

import com.e.yorizori.R
import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.e.yorizori.Activity.OpeningActivity
import com.e.yorizori.CheckListPicker
import com.e.yorizori.Fragment.Add_Recipe
import kotlinx.android.synthetic.main.ingredientlist_item.view.*
import android.view.LayoutInflater as LayoutInflater1


class WRIngListViewAdapter(context: Context?, fragment : Fragment) : BaseAdapter() {
    var mContext = context
    var fragment = fragment
    var sample : ArrayList<Pair<String,String>> = arrayListOf(Pair("",""))
    override fun getCount(): Int {
        return sample.size
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItem(position: Int): Pair<String,String> {
        return sample[position]
    }


    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var view = convertView
        val context = parent.context

        if (view == null){
            val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as android.view.LayoutInflater
            view = inflater.inflate(R.layout.ingredientlist_item,parent,false)
        }
        val ing_auto = view!!.findViewById<AutoCompleteTextView>(R.id.ingText)
        val searchAdapter = ArrayAdapter<String>(mContext!!,
            android.R.layout.simple_list_item_1,
            OpeningActivity.server_ing
        )
        ing_auto.threshold=0
        ing_auto.setAdapter(searchAdapter)

        ing_auto.onItemClickListener = AdapterView.OnItemClickListener{
                parent, view, position, id ->
            val clicked = parent.getItemAtPosition(position).toString()
            ing_auto.setText(clicked)
        }

        val del_button = view.findViewById<ImageButton>(R.id.ingListDel)
        del_button.setOnClickListener {
            sample.removeAt(position)
            this.notifyDataSetChanged()
            (fragment as Add_Recipe).resizeListView(0)
        }
        return view
    }
    fun add_item(){
        sample.add(Pair("",""))
    }

}