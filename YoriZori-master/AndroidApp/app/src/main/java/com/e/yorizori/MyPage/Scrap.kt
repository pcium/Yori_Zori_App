package com.e.yorizori.MyPage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import androidx.fragment.app.Fragment
import com.e.yorizori.Activity.HomeActivity
import com.e.yorizori.Adapter.Scraped_Adapter
import com.e.yorizori.Fragment.MyPage
import com.e.yorizori.Interface.BackBtnPressListener
import com.e.yorizori.R
import kotlinx.android.synthetic.main.activity_scrap.*
import kotlinx.android.synthetic.main.activity_scrap.view.*

class Scrap(parent : Fragment) : BackBtnPressListener,Fragment() {
    val parent = parent
    var saved_Fragment : Fragment? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if(saved_Fragment!= null){
            (activity as HomeActivity).changeFragment(saved_Fragment!!)
        }
        val view = inflater.inflate(R.layout.activity_scrap, container, false)
        val listView  = view.findViewById<ListView>(R.id.scr_checklist)
        view.scr_backBtn.setOnClickListener {
            onBack()
        }
        val adapter = Scraped_Adapter(this.context!!, activity!!,this)
        listView.adapter = adapter
        (parent as MyPage).saveInfo(0,this)
        (activity as HomeActivity).setOnBackBtnListener(this)

        if (adapter.getCount() == 0) {
            emptyScrap?.visibility = View.VISIBLE
        }
        else{
            emptyScrap?.visibility = View.INVISIBLE
        }

        return view
    }

    override fun onBack() {
        (parent as MyPage).saveInfo(0,null)
        var ft = (activity as HomeActivity).supportFragmentManager
        ft.beginTransaction().remove(this).commit()
        ft.popBackStack()
    }

    fun save_info(f:Fragment?){
        saved_Fragment = f
    }
}
