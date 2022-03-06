package com.e.yorizori.MyPage

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.e.yorizori.Activity.HomeActivity
import com.e.yorizori.Activity.LoginActivity
import com.e.yorizori.Fragment.MyPage
import com.e.yorizori.Interface.BackBtnPressListener
import com.e.yorizori.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_account_setting.view.*

class AccountSetting(parent: Fragment) : BackBtnPressListener, Fragment() {
    val parent = parent
    lateinit var database : DatabaseReference
    lateinit var firebaseAuth : FirebaseAuth
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.activity_account_setting, container, false)
        lateinit var database : DatabaseReference
        lateinit var firebaseAuth : FirebaseAuth

        // get the user's id
        database = FirebaseDatabase.getInstance().getReference("Users")
        firebaseAuth = FirebaseAuth.getInstance()
        val user = firebaseAuth.currentUser

        // set the text view
        view.acn_name.text = user!!.displayName
        view.acn_email.text = user!!.email

        // for logout
        val logOutView = view.findViewById<TextView>(R.id.acn_text_logout)
        logOutView.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                firebaseAuth.signOut()
                Toast.makeText(requireContext(), R.string.logout, Toast.LENGTH_SHORT).show()
                val i = Intent(requireContext(), LoginActivity::class.java)
                i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(i)
            }
        })

        view.acn_backBtn.setOnClickListener {
            onBack()
        }
        (parent as MyPage).saveInfo(3,this)
        (activity as HomeActivity).setOnBackBtnListener(this)

        // for logout
        view.acn_text_logout.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                firebaseAuth.signOut()
                Toast.makeText(requireContext(), R.string.logout, Toast.LENGTH_SHORT).show()
                val i = Intent(requireContext(), LoginActivity::class.java)
                i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(i)
            }
        })

        return view
    }

    override fun onBack() {
        (parent as MyPage).saveInfo(3,null)
        var ft = (activity as HomeActivity).supportFragmentManager
        ft.beginTransaction().remove(this).commit()
        ft.popBackStack()
    }
}
