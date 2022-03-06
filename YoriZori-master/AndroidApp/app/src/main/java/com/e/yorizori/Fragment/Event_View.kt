package com.e.yorizori.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.e.yorizori.Activity.HomeActivity
import com.e.yorizori.Interface.BackBtnPressListener
import com.e.yorizori.R

class Event_View(fragment : Fragment): BackBtnPressListener,Fragment(){
    private val fragment = fragment

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.activity_event_view, container,false)
        val event_img = view.findViewById(R.id.event_view) as ImageView
        val event_title = view.findViewById(R.id.event_bar_text) as TextView
        val event_back = view.findViewById(R.id.event_bar_back) as ImageButton

        event_back.setOnClickListener {
            (fragment as Community).saveInfo(3,null)
            var ft = (activity as HomeActivity).supportFragmentManager
            ft.beginTransaction().remove(this).commit()
            ft.popBackStack()
        }
        event_title.text = "#날뛰는 소세지"
        event_img.setImageDrawable(ContextCompat.getDrawable(this.context!!, R.drawable.event_poster))

        (fragment as Community).saveInfo(3,this)
        (activity as HomeActivity).setOnBackBtnListener(this)

        return view
    }

    override fun onBack() {
        (fragment as Community).saveInfo(3,null)
        var ft = (activity as HomeActivity).supportFragmentManager
        ft.beginTransaction().remove(this).commit()
        ft.popBackStack()
    }
}