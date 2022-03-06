package com.e.yorizori.Adapter

import android.content.Context
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.e.yorizori.Activity.HomeActivity
import com.e.yorizori.Activity.OpeningActivity
import com.e.yorizori.Class.Recipe
import com.e.yorizori.Class.Server_recipe
import com.e.yorizori.R
import com.e.yorizori.explainFrag
import com.squareup.picasso.Picasso

class SearchedAdapter(keyword : String, activity : FragmentActivity, fragment: Fragment) : BaseAdapter() {
    private var SearchedRecipe : ArrayList<Server_recipe> = arrayListOf()
    private val activity = activity
    private val parent = fragment

    init{
        SearchedRecipe = OpeningActivity.recipe_list.filter{
            it.recipe.cook_title.contains(keyword) || it.recipe.ings.map{ it.first}.contains(keyword)
        } as ArrayList<Server_recipe>
    }
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var view = convertView
        val context = parent.context

        if (view == null){
            val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            view = inflater.inflate(R.layout.community_list_item_2,parent,false)
        }
        val titleView = view!!.findViewById(R.id.list_title) as TextView
        val tagView  = view.findViewById(R.id.list_tag1) as TextView
        val imageView = view.findViewById(R.id.list_imageView1) as ImageView

        val metrics = DisplayMetrics()
        activity.windowManager.defaultDisplay.getMetrics(metrics)
        val px = (130 * metrics.density).toInt()

        titleView.text = SearchedRecipe[position].recipe.cook_title
        tagView.text = mktag(SearchedRecipe[position].recipe)
        Picasso.get().load(SearchedRecipe[position].recipe.pics[0]).resize(px,px).into(imageView)

        view.setOnClickListener {
            (activity as HomeActivity).changeFragment(explainFrag(this.parent,4, SearchedRecipe[position],mktag(SearchedRecipe[position].recipe)))
        }


        return view
    }

    override fun getItem(position: Int): Any {
        return SearchedRecipe[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return SearchedRecipe.size
    }
    fun mktag(recipe: Recipe): String{
        var ret=""
        val like = arrayOf("맛있어요","간단해요","저렴해요")
        var tags = recipe.tag
        for (i in 0 until 3){
            val tmp = like[i]
            if(recipe.like_num[i] > 50)
                ret +=("#$tmp ")
        }
        for (i in tags){
            ret += ("#$i ")
        }
        return ret

    }
}