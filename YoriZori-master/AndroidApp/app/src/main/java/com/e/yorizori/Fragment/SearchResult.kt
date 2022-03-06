package com.e.yorizori.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ListView
import androidx.fragment.app.Fragment
import com.e.yorizori.Activity.HomeActivity
import com.e.yorizori.Adapter.SearchedAdapter
import com.e.yorizori.Interface.BackBtnPressListener
import com.e.yorizori.R

class SearchResult(fragment : Fragment, title: String) : BackBtnPressListener, Fragment() {
    private val fragment = fragment
    private val title = title
    private var savedFragment :Fragment? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if(savedFragment != null){
            (activity as HomeActivity).changeFragment(savedFragment!!)
        }
        val view = inflater.inflate(R.layout.commu_search_result,container,false)
        val search_bar = view.findViewById(R.id.fake_auto_search) as EditText
        (fragment as Community_Search).saveInfo(this)
        search_bar.setOnTouchListener { v, event ->
            onBack()
            true
        }
        val listview = view.findViewById(R.id.commu_searched_result) as ListView
        val adapter = SearchedAdapter(title,activity!!,this)
        listview.adapter= adapter
        (activity as HomeActivity).setOnBackBtnListener(this)
        return view
    }

    override fun onBack(){
        (fragment as Community_Search).saveInfo(null)
        var ft = (activity as HomeActivity).supportFragmentManager
        ft.beginTransaction().remove(this).commit()
        ft.popBackStack()
    }
    fun saveInfo(fragment : Fragment?){
        savedFragment = fragment
    }
}