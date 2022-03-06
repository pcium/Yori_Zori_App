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

class Donate(parent: Fragment) :BackBtnPressListener, Fragment() {
    val parent = parent
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.activity_donate,container,false)
        val go_intent = view.findViewById(R.id.donate_btn) as Button
        go_intent.setOnClickListener {
            Toast.makeText(this.context, "후원 감사드립니다. 더 열심히 하겠습니다!", Toast.LENGTH_LONG).show();
            onBack()
        }

        val go2_intent = view.findViewById(R.id.don_backBtn) as ImageButton
        go2_intent.setOnClickListener {
            onBack()
        }

        (parent as MyPage).saveInfo(5,this)
        (activity as HomeActivity).setOnBackBtnListener(this)

        return view
    }

    override fun onBack() {
        (parent as MyPage).saveInfo(5,null)
        var ft = (activity as HomeActivity).supportFragmentManager
        ft.beginTransaction().remove(this).commit()
        ft.popBackStack()
    }
}
