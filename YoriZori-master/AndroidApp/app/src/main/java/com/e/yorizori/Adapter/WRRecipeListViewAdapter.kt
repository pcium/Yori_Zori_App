package com.e.yorizori.Adapter

import com.e.yorizori.R
import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.e.yorizori.Fragment.Add_Recipe


class WRRecipeListViewAdapter(context: Context?, fragment : Fragment) : BaseAdapter() {
    var mContext = context
    var sample: ArrayList<String> = arrayListOf("")
    var fragment = fragment
    override fun getCount(): Int {
        return sample.size
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItem(position: Int): String {
        return sample[position]
    }


    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var view = convertView
        val context = parent.context

        if (view == null){
            val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as android.view.LayoutInflater
            view = inflater.inflate(R.layout.recipelist_item,parent,false)
        }
        val recipeNumText = view!!.findViewById(R.id.recipeNumText) as TextView
        recipeNumText.setText((position + 1).toString())
        val del_button = view.findViewById(R.id.recipe_del_button) as ImageButton
        del_button.setOnClickListener {
            if (sample.size > 1) {
                sample.removeAt(position)
                this.notifyDataSetChanged()
                (fragment as Add_Recipe).resizeListView(1)
            }
            else{
                Toast.makeText(mContext!!,"재료는 1가지 이상이 필요합니다",Toast.LENGTH_SHORT).show()
            }
        }
        return view
    }

    fun addItem(){
        sample.add("")
    }
}