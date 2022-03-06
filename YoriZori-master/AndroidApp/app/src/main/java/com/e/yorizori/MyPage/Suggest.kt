package com.e.yorizori.MyPage

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.e.yorizori.Activity.HomeActivity
import com.e.yorizori.Fragment.MyPage
import com.e.yorizori.Interface.BackBtnPressListener
import com.e.yorizori.R

class Suggest(parent : Fragment) : BackBtnPressListener,Fragment() {
    val parent = parent
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.activity_suggest,container,false)
        val go_intent = view.findViewById(R.id.sug_sendBtn) as ImageButton
        go_intent.setOnClickListener {

            Toast.makeText(this.context, "운영진에게 메세지를 보냈습니다. 서비스 이용에 반영하도록 하겠습니다!", Toast.LENGTH_LONG).show();
            onBack()
        }

        val go2_intent = view.findViewById(R.id.sug_backBtn) as ImageButton
        go2_intent.setOnClickListener {
            onBack()
        }

        (parent as MyPage).saveInfo(4,this)
        (activity as HomeActivity).setOnBackBtnListener(this)

        return view
    }

    override fun onBack() {
        (parent as MyPage).saveInfo(4,null)
        var ft = (activity as HomeActivity).supportFragmentManager
        ft.beginTransaction().remove(this).commit()
        ft.popBackStack()
    }
}
