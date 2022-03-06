package com.e.yorizori.Fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.fragment.app.Fragment
import com.e.yorizori.Activity.HomeActivity
import com.e.yorizori.Activity.OpeningActivity
import com.e.yorizori.Interface.BackBtnPressListener
import com.e.yorizori.R
import kotlinx.android.synthetic.main.activity_community_search.view.auto_search_community

class Community_Search(context: Context, fragment : Fragment) : BackBtnPressListener,Fragment(){
    private var recipes : ArrayList<String> = ArrayList()
    private var fragment = fragment
    var savedFragment : Fragment? = null

    init{
        recipes = OpeningActivity.recipe_list.map{
            it.recipe.cook_title
        } as ArrayList<String>
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view=  inflater.inflate(R.layout.activity_community_search,container,false)
        val searchView = view.findViewById(R.id.auto_search_community) as AutoCompleteTextView
        if(savedFragment != null){
            (activity as HomeActivity).changeFragment(savedFragment!!)
        }

        val searchAdapter = ArrayAdapter<String>(requireContext(),
            android.R.layout.simple_list_item_1,
            recipes
            )
        searchView.threshold = 0
        searchView.setAdapter(searchAdapter)


        searchView.setOnItemClickListener { parent, view, position, id ->
            val clicked = parent.getItemAtPosition(position).toString()
            (activity as HomeActivity).changeFragment(SearchResult(this, clicked))
        }
        searchView.setOnEditorActionListener { v, actionId, event ->
            val text = v.text.toString()
            (activity as HomeActivity).changeFragment(SearchResult(this,text))
            true
        }
        (fragment as Community).saveInfo(4,this)
        (activity as HomeActivity).setOnBackBtnListener(this)
        return view
    }

    fun saveInfo(f: Fragment?){
        savedFragment = f
    }

    override fun onBack() {
        (fragment as Community).saveInfo(4,null)
        var ft = (activity as HomeActivity).supportFragmentManager
        ft.beginTransaction().remove(this).commit()
        ft.popBackStack()
    }
}