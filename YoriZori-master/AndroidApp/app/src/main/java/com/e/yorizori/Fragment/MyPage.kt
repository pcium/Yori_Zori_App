package com.e.yorizori.Fragment

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat.finishAffinity
import androidx.fragment.app.Fragment
import com.e.yorizori.Activity.LoginActivity
import com.e.yorizori.R
import com.e.yorizori.Activity.HomeActivity
import com.e.yorizori.Interface.BackBtnPressListener
import com.e.yorizori.MyPage.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_my.view.*



class MyPage: BackBtnPressListener, Fragment(){
    lateinit var database : DatabaseReference
    lateinit var firebaseAuth : FirebaseAuth
    private var savedFragment : Array<Fragment?> = arrayOf(null,null,null,null,null,null)
    private var goto = -1
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View?{
        super.onCreateView(inflater, container, savedInstanceState)
        val view = inflater.inflate(R.layout.activity_my, container, false)

        if(goto != -1){
            val fragment = savedFragment[goto]!!
            (activity as HomeActivity).changeFragment(fragment)
        }
        view.text_scrap.setOnClickListener {
            (activity as HomeActivity).changeFragment(Scrap(this))
        }

        view.text_wrote.setOnClickListener {
            (activity as HomeActivity).changeFragment(Wrote(this))
        }

        view.text_allergy.setOnClickListener {
            (activity as HomeActivity).changeFragment(Allergy(this))
        }

        view.text_account_set.setOnClickListener {
            (activity as HomeActivity).changeFragment(AccountSetting(this))
        }

        view.text_suggest.setOnClickListener {
            (activity as HomeActivity).changeFragment(Suggest(this))
        }

        view.text_donate.setOnClickListener {
            (activity as HomeActivity).changeFragment(Donate(this))
        }
        (activity as HomeActivity).setOnBackBtnListener(this)

        // get the user's id
        database = FirebaseDatabase.getInstance().getReference("Users")
        firebaseAuth = FirebaseAuth.getInstance()
        val user = firebaseAuth.currentUser

        // Account Setting 에서 진행 예정 (제거 가능)
        // set the text view
        val textView = view.findViewById<TextView>(R.id.my_page_title)
        textView.text = "마이페이지"

        // for logout
        textView.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                firebaseAuth.signOut()
                Toast.makeText(requireContext(), R.string.logout, Toast.LENGTH_SHORT).show()
                val i = Intent(requireContext(), LoginActivity::class.java)
                i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(i)
            }
        })

        (activity as HomeActivity).saveFragment(2,this)

        return view
    }
    override fun onResume(){
        super.onResume()
    }


    fun saveInfo(idx: Int, fragment : Fragment?){
        savedFragment[idx] = fragment
        if(fragment == null)
            goto = -1
        else
            goto = idx
    }

    override fun onBack() {
        dialog()
    }
    fun dialog(){
        var builder = AlertDialog.Builder(this.context)
        builder.setTitle("YoriZori")
        builder.setMessage("종료하시겠습니까?")
        builder.setPositiveButton("예", DialogInterface.OnClickListener { dialog, which ->
            finishAffinity(requireActivity());
            System.runFinalization();
            System.exit(0);
        })
        builder.setNegativeButton("아니요", DialogInterface.OnClickListener { dialog, which ->
            dialog.cancel()
        })
        builder.show()
        true
    }
}