package com.e.yorizori.Adapter

import android.content.Context
import android.util.DisplayMetrics
import android.util.Log
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
import com.e.yorizori.Class.Community_ListViewItem
import com.e.yorizori.Class.Recipe
import com.e.yorizori.Class.Server_recipe
import com.e.yorizori.Enum.Like
import com.e.yorizori.R
import com.e.yorizori.explainFrag
import com.squareup.picasso.Picasso

class Community_SortedListViewAdapter(context : Context, activity : FragmentActivity, fragment : Fragment, p : Int): BaseAdapter(){
    private var listViewItemList = ArrayList<Community_ListViewItem>()
    private var context = context
    private var activity = activity
    private var fragment = fragment
    private var parent_position = p
    init{
        var tmp_recipes: List<Server_recipe> = listOf()
        when(parent_position){
            0 -> {
                // Error! It's event tab
            }
            1-> {
                val my_tmp_set = OpeningActivity.my_ing.map{it.item}
                tmp_recipes = OpeningActivity.recipe_list.filter{
                    val tmp_set = it.recipe.ings.map{ it.first}
                    my_tmp_set.containsAll(tmp_set)
                }
            }
            2->{
                tmp_recipes = OpeningActivity.recipe_list.sortedByDescending {
                    it.recipe.like_num[Like.DELICIOUS.idx]
                }
            }
            3->{
                tmp_recipes = OpeningActivity.recipe_list.sortedByDescending{it.recipe.like_num[Like.QUICK.idx] }
            }
            4->{
                tmp_recipes = OpeningActivity.recipe_list.sortedByDescending{ it.recipe.like_num[Like.CHEAP.idx] }
            }
            5->{
                tmp_recipes = OpeningActivity.recipe_list.sortedByDescending{ it.recipe.scrap_num }
            }
            else->{
                tmp_recipes = OpeningActivity.recipe_list.sortedByDescending { it.recipe.date }
            }
        }
        tmp_recipes = tmp_recipes.filter{
            it.recipe.cook_title != ""
        }
        for(i in tmp_recipes) {
            addItem(i)
        }
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

        titleView.text = listViewItemList[position].titleStr
        tagView.text = listViewItemList[position].tagStr
        Picasso.get().load(listViewItemList[position].iconurl).resize(px,px).into(imageView)

        view.setOnClickListener {
            (activity as HomeActivity).changeFragment(explainFrag(fragment,1,listViewItemList[position].argRecipe, listViewItemList[position].tagStr))
        }

        return view
    }

    override fun getItem(position: Int): Any {
        return listViewItemList[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return listViewItemList.size
    }

    fun addItem(srecipe: Server_recipe){
        val item = Community_ListViewItem()
        item.iconurl = srecipe.recipe.pics[0]
        item.titleStr = srecipe.recipe.cook_title
        item.tagStr = mktag(srecipe.recipe)
        item.argRecipe = srecipe

        listViewItemList.add(item)
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