package com.e.yorizori

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.ListView
import androidx.fragment.app.Fragment
import com.e.yorizori.Activity.HomeActivity
import com.e.yorizori.Adapter.ingAdapter
import com.e.yorizori.Class.Recipe
import com.e.yorizori.Class.ScrapInfo
import com.e.yorizori.Class.Server_recipe
import com.e.yorizori.Fragment.Community
import com.e.yorizori.Fragment.Community_SortedList
import com.e.yorizori.Interface.BackBtnPressListener
import com.e.yorizori.Fragment.SearchResult
import com.e.yorizori.MyPage.Scrap
import com.e.yorizori.MyPage.Wrote
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_explain.*
import kotlinx.android.synthetic.main.activity_explain.view.*

class explainFrag(parent : Fragment, option : Int, item : Server_recipe?, tag : String?) : BackBtnPressListener,Fragment() {
    private val parent = parent
    private val option = option
    private val recipeAll = item
    private val tagStr = tag
    val firebaseAuth = FirebaseAuth.getInstance()
    val userUID = firebaseAuth.currentUser!!.uid
    private var rootRef = FirebaseDatabase.getInstance().reference
    var recommend = arrayOf(false,false, false, false)
    private val path = arrayOf("scrap","delicious","simple","cheap")
    var finding: ArrayList<ArrayList<ScrapInfo>> = arrayListOf(arrayListOf(),arrayListOf(),arrayListOf(),arrayListOf())
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        finding = HomeActivity.recommend_info.map {
            it.filter {
                it.has(recipeAll!!.recipe.cook_title, recipeAll!!.recipe.writer_UID)
            }
        } as ArrayList<ArrayList<ScrapInfo>>

        recommend[0] = finding[0].isNotEmpty()
        recommend[1] = finding[1].isNotEmpty()
        recommend[2] = finding[2].isNotEmpty()
        recommend[3] = finding[3].isNotEmpty()

        val view = inflater.inflate(R.layout.activity_explain, container, false)
        // Inflate the layout for this fragment
        // 뒤로가기버튼을 누를시 community로 돌아가기
        view.returnBtn.setOnClickListener {
            onBack()
        }

        view!!.scrapBtn.isSelected = recommend[0]
        view.del_btn.isSelected = recommend[1]
        view.simple_btn.isSelected = recommend[2]
        view.price_btn.isSelected = recommend[3]

        // 레시피클래스 가져오기
        var recipe1 = recipeAll!!.recipe

        //초기값 설정
        view.foodName.text = recipe1?.cook_title
        view.price_num.text = recipe1?.like_num!![2].toString()
        view.simple_num.text = recipe1?.like_num[1].toString()
        view.del_num.text = recipe1?.like_num[0].toString()
        view.scrapNum.text = recipe1?.scrap_num.toString()
        view.scrapTag.text = tagStr
        val picc = view.findViewById<ImageView>(R.id.photoView)
        Picasso.get().load(recipe1?.pics[0]).into(picc)

        // 재료 리스트뷰
        val adapter1 = ingAdapter(this.context!!, recipe1.ings)
        val listview1 = view.findViewById<ListView>(R.id.ing_ListView)
        listview1.setAdapter(adapter1)

        val LIST_MENU2 = recipe1.recipe
        val adapter2 = ArrayAdapter(this.context!!, android.R.layout.simple_list_item_1, LIST_MENU2)
        val listview2 = view.findViewById<ListView>(R.id.recipe_listview)
        listview2.setAdapter(adapter2)


        //Listview의 높이를 Item의 갯수에 따라 조정하는 함수
        fun setListViewHeightBasedOnItems(listView: ListView): Boolean {
            val listAdapter = listView.getAdapter()
            if (listAdapter != null) {
                val numberOfItems = listAdapter.getCount()
                // Get total height of all items.
                var totalItemsHeight = 0
                for (itemPos in 0 until numberOfItems) {
                    val item = listAdapter.getView(itemPos, null, listView)
                    val px = 500 * (listView.getResources().getDisplayMetrics().density)
                    item.measure(
                        View.MeasureSpec.makeMeasureSpec(
                            px.toInt(),
                            View.MeasureSpec.AT_MOST
                        ), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
                    )
                    totalItemsHeight += item.getMeasuredHeight()
                }
                // Get total height of all item dividers.
                val totalDividersHeight = (listView.getDividerHeight() * (numberOfItems - 1))
                // Get padding
                val totalPadding = listView.getPaddingTop() + listView.getPaddingBottom()
                // Set list height.
                val params = listView.getLayoutParams()
                params.height = totalItemsHeight + totalDividersHeight + totalPadding
                listView.setLayoutParams(params)
                listView.requestLayout()
                return true
            } else {
                return false
            }
        }
        setListViewHeightBasedOnItems(listview1)
        setListViewHeightBasedOnItems(listview2)

        view.scrapBtn.setOnClickListener {
            scrapBtn.isSelected = !recommend[0]
            recommend[0] = !recommend[0]
            if (recommend[0]) {
                recipe1.scrap_num += 1
            } else {
                recipe1.scrap_num -= 1
            }
            scrapNum.text = recipe1.scrap_num.toString()
            rootRef.child("recipe/${recipe1.cook_title}/${recipeAll.key}").setValue(Gson().toJson(recipe1))
        }
        // delicious
        view.del_btn.setOnClickListener {
            del_btn.isSelected = !recommend[1]
            recommend[1] = !recommend[1]
            if (recommend[1]) {
                recipe1.like_num[0] += 1
            } else {
                recipe1.like_num[0] -= 1
            }
            del_num.text = recipe1.like_num[0].toString()
            rootRef.child("recipe/${recipe1.cook_title}/${recipeAll.key}").setValue(Gson().toJson(recipe1))
        }
        view.simple_btn.setOnClickListener {
            simple_btn.isSelected = !recommend[2]
            recommend[2] = !recommend[2]
            if (recommend[2]) {
                recipe1.like_num[1] += 1
            } else {
                recipe1.like_num[1] -= 1
            }
            simple_num.text = recipe1.like_num[1].toString()
            rootRef.child("recipe/${recipe1.cook_title}/${recipeAll.key}").setValue(Gson().toJson(recipe1))
        }
        // 가성비버튼 누를 때
        view.price_btn.setOnClickListener {
            price_btn.isSelected = !recommend[3]
            recommend[3] = !recommend[3]
            if (recommend[3]) {
                recipe1.like_num[2] += 1
            } else {
                recipe1.like_num[2] -= 1
            }
            price_num.text = recipe1.like_num[2].toString()
            rootRef.child("recipe/${recipe1.cook_title}/${recipeAll.key}").setValue(Gson().toJson(recipe1))
        }

        when (option) {
            0 -> {
                (parent as Community).saveInfo(2, this)
            }
            1 -> {
                (parent as Community_SortedList).saveInfo(1, this)
            }
            2 -> {
                (parent as Scrap).save_info(this)
            }
            3 -> {
                (parent as Wrote).save_info(this)
            }
            else -> {
                (parent as SearchResult).saveInfo(this)
            }
        }


        (activity as HomeActivity).setOnBackBtnListener(this)
        return view
    }

    override fun onBack() {
        when (option) {
            0 -> {
                (parent as Community).saveInfo(2, null)
            }
            1 -> {
                (parent as Community_SortedList).saveInfo(1, null)
            }
            2 -> {
                (parent as Scrap).save_info(null)
            }
            3 -> {
                (parent as Wrote).save_info(null)
            }
            else -> {
                (parent as SearchResult).saveInfo(null)
            }
        }
        var ft = (activity as HomeActivity).supportFragmentManager
        ft.beginTransaction().remove(this).commit()
        ft.popBackStack()
    }

    override fun onPause() {
        super.onPause()
        for(i in 0 .. finding.size - 1){
            if(recommend[i]){
                if(finding[i].isEmpty()){
                    rootRef.child("users/$userUID/${path[i]}").push().setValue("${recipeAll!!.recipe.cook_title},${recipeAll.recipe.writer_UID}")
                }
            }
            else{
                if(finding[i].isNotEmpty()){
                    rootRef.child("users/$userUID/${path[i]}/${finding[i].last().key}").removeValue()
                }
            }
        }
    }

}
