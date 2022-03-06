package com.e.yorizori.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView.OnItemClickListener
import android.widget.ImageButton
import android.widget.ListView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.e.yorizori.Adapter.Community_SortedListViewAdapter
import com.e.yorizori.Activity.HomeActivity
import com.e.yorizori.Interface.BackBtnPressListener
import com.e.yorizori.R
import com.e.yorizori.explainFrag

class Community_SortedList(position : Int, activity: HomeActivity, fragment: Community): BackBtnPressListener,Fragment(){
    val position = position
    val activity = activity
    val fragment = fragment
    var savedFragment: Array<Fragment?> = arrayOf(null,null)
    var goto = -1
    private var titles = arrayOf(null,"#지금 당장만들어 보자","#이 세상 맛이 아니다","#배고프니 빨리빨리","#이 가격 실화냐!?","#다들 이거 만들던데....","#이런건 어때요?")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fragment.saveInfo(0,this)
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.activity_community,container,false)
        val add_recipe_button = view.findViewById(R.id.add_recipe) as ImageButton
        add_recipe_button.setOnClickListener{
            activity.changeFragment(Add_Recipe(this,1))
        }
        val titleview = view.findViewById(R.id.page_title) as TextView
        val adapter = Community_SortedListViewAdapter(
            this.context!!,
            activity,
            this,
            position
        )
        val backBtn = view.findViewById(R.id.commu_backBtn) as ImageButton
        backBtn.visibility =View.VISIBLE

        val SearchBtn = view.findViewById(R.id.commu_search) as ImageButton
        SearchBtn.visibility = View.GONE

        backBtn.setOnClickListener{
            (fragment as Community).saveInfo(0,null)
            var ft = (activity as HomeActivity).supportFragmentManager
            ft.beginTransaction().remove(this).commit()
            ft.popBackStack()
        }
        titleview.text = titles[position]
        val listview = view.findViewById(R.id.listview1) as ListView

        listview.adapter = adapter

        if(goto != -1){
            val prev_frag = savedFragment[goto]!!
            (this.activity as HomeActivity).changeFragment(prev_frag)

        }


        return view
    }
    override fun onResume(){
        super.onResume()
        (activity as HomeActivity).setOnBackBtnListener(this)

    }

    override fun onBack() {
        (fragment as Community).saveInfo(0,null)
        var ft = (activity as HomeActivity).supportFragmentManager
        ft.beginTransaction().remove(this).commit()
        ft.popBackStack()
    }

    fun saveInfo(idx: Int, fragment : Fragment?){
        savedFragment[idx] = fragment
        if(fragment == null)
            goto = -1
        else
            goto = idx
    }
}