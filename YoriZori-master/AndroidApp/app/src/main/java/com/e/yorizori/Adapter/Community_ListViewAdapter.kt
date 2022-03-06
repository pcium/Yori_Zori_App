package com.e.yorizori.Adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.e.yorizori.Class.Recipe
import com.e.yorizori.Fragment.Community_SortedList
import com.e.yorizori.Activity.HomeActivity
import com.e.yorizori.Activity.OpeningActivity
import com.e.yorizori.Class.RefrigItem
import com.e.yorizori.Enum.Like
import com.e.yorizori.Fragment.Community
import com.e.yorizori.R


class Community_ListViewAdapter(context: Context, activity: FragmentActivity?,fragment : Fragment): BaseAdapter(){
    private var listViewItemList=ArrayList<Community_HorizontalAdapter>()
    private var titles = arrayListOf(null,"#지금 당장만들어 보자","#이 세상 맛이 아니다","#배고프니 빨리빨리","#이 가격 실화냐!?","#다들 이거 만들던데....","#이런건 어때요?")
    private val activity = activity as HomeActivity
    private val fragment = fragment as Community

    init{
        if(OpeningActivity.recipe_list.isEmpty())
            Toast.makeText(context,"Empty", Toast.LENGTH_SHORT).show()
        for (i in 0 until 7) {
            listViewItemList.add(
                Community_HorizontalAdapter(
                    context,
                    activity as HomeActivity,
                    fragment,
                    i
                )
            )
            fillList(i)
        }

    }
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var view = convertView
        val context = parent.context
        if (view == null){
            val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            view = inflater.inflate(R.layout.community_list_item_1,parent,false)
        }

        val listTitleView=view!!.findViewById(R.id.item_title) as TextView
        val listView = view.findViewById(R.id.list_horizontal) as RecyclerView
        val layoutManager = LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false)
        listView.setLayoutManager(layoutManager)

        val ladapter = listViewItemList[position]
        if(titles[position] == null){
            listTitleView.visibility = View.GONE
            var eadapter = Community_Horizontal_Event_Adapter(context,activity,fragment)
            eadapter.fill()
            listView.adapter = eadapter
        }
        else {
            listTitleView.text = titles[position]
            listTitleView.visibility = View.VISIBLE
            listTitleView.setOnClickListener(View.OnClickListener {
                activity.changeFragment(Community_SortedList(position, activity, fragment))
            })
            listView.adapter = ladapter
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

    override fun isEnabled(position: Int): Boolean {
        return false
    }

    fun fillList(position:Int){
        when(position){
            0 -> {//Event Tab...!? 어떻게 채우지 ㄷㄷㄷㄷㄷ
                var count = 0
                for(i in OpeningActivity.recipe_list){
                    listViewItemList[position].addItem(null)
                    count+=1
                    if(count>=5) {
                        break
                    }
                }
            }
            1->{
                val possible = OpeningActivity.recipe_list.filter{
                    val require_ings = it.recipe.ings.map{
                        it.first
                    }.toSet()
                    val ing_set = OpeningActivity.my_ing.map{
                        it.item
                    }.toSet()
                    (require_ings - ing_set).isEmpty()
                }
                var count = 0
                for (i in possible){
                    listViewItemList[position].addItem(i)
                    count+=1
                    if(count>=5) {
                        break
                    }
                }
            }
            2->{
                OpeningActivity.recipe_list.sortWith(compareByDescending { it.recipe.like_num[Like.DELICIOUS.idx] })
                var count = 0
                for (i in OpeningActivity.recipe_list){
                    listViewItemList[position].addItem(i)
                    count+=1
                    if(count>=5) {
                        break
                    }
                }
            }
            3->{
                OpeningActivity.recipe_list.sortWith(compareByDescending { it.recipe.like_num[Like.QUICK.idx] })
                var count=0
                for (i in OpeningActivity.recipe_list){
                    listViewItemList[position].addItem(i)
                    count+=1
                    if(count>=5) {
                        break
                    }
                }
            }
            4-> {
                OpeningActivity.recipe_list.sortWith(compareByDescending { it.recipe.like_num[Like.CHEAP.idx] })
                var count=0
                for (i in OpeningActivity.recipe_list) {
                    listViewItemList[position].addItem(i)
                    count+=1
                    if(count>=5) {
                        break
                    }
                }
            }
            5->{
                OpeningActivity.recipe_list.sortWith(compareByDescending { it.recipe.scrap_num })
                var count = 0
                for (i in OpeningActivity.recipe_list){
                    listViewItemList[position].addItem(i)
                    count+=1
                    if(count>=5) {
                        break
                    }
                }
            }
            else->{
                OpeningActivity.recipe_list.sortWith(compareByDescending { it.recipe.date })
                var count = 0
                for (i in OpeningActivity.recipe_list){
                    listViewItemList[position].addItem(i)
                    count+=1
                    if(count>=5) {
                        break
                    }
                }
            }

        }
    }
}