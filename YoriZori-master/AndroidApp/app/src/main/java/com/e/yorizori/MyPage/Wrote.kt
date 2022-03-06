package com.e.yorizori.MyPage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ListView
import androidx.fragment.app.Fragment
import com.e.yorizori.Activity.HomeActivity
import com.e.yorizori.Adapter.WroteAdapter
import com.e.yorizori.Fragment.MyPage
import com.e.yorizori.Interface.BackBtnPressListener
import com.e.yorizori.R
import kotlinx.android.synthetic.main.activity_wrote.*
import kotlinx.android.synthetic.main.activity_wrote.view.*
import kotlinx.android.synthetic.main.activity_wrote.view.emptyWrote

class Wrote(parent : Fragment) : BackBtnPressListener,Fragment() {
    val parent = parent
    var saved_Fragment : Fragment? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if(saved_Fragment!=null){
            (activity as HomeActivity).changeFragment(saved_Fragment!!)
        }
        val view = inflater.inflate(R.layout.activity_wrote, container, false)
        val listView  = view.findViewById<ListView>(R.id.wrt_checklist)
        view.wrt_backBtn.setOnClickListener {
           onBack()
        }
        val button = view.findViewById(R.id.wrt_deleteBtn) as ImageButton
        val adapter = WroteAdapter(this,activity!!)
        (parent as MyPage).saveInfo(1,this)
        (activity as HomeActivity).setOnBackBtnListener(this)
        listView.adapter = adapter


        if(listView.adapter.count == 0){
            view.no_written.visibility = View.VISIBLE
            emptyWrote?.visibility = View.VISIBLE
        }
        else{
            view.no_written.visibility = View.INVISIBLE
            emptyWrote?.visibility = View.INVISIBLE
        }
        return view
    }

    override fun onBack() {
        (parent as MyPage).saveInfo(1,null)
        var ft = (activity as HomeActivity).supportFragmentManager
        ft.beginTransaction().remove(this).commit()
        ft.popBackStack()
    }

    fun save_info(f:Fragment?){
        saved_Fragment = f
    }
}
