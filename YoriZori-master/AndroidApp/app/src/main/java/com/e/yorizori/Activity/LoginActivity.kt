package com.e.yorizori.Activity

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.KeyEvent
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.e.yorizori.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_login.*


class LoginActivity : AppCompatActivity() {
    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        database = FirebaseDatabase.getInstance().reference
        auth = FirebaseAuth.getInstance()

        setContentView(R.layout.activity_login)

        val cur_user = auth.currentUser
        if (cur_user != null) {
            // User is signed in
            val i = Intent(this@LoginActivity, HomeActivity::class.java)
            i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(i)
        }

        login_button.setOnClickListener(object : View.OnClickListener{
            override fun onClick(v: View?) {

                val email = login_email.text.toString()
                val pw = login_pw.text.toString()

                if (validateForm()) {
                    logIn(email, pw)
                }
            }
        })

        //email 입력 칸에서 enter
        login_email.setOnKeyListener { v, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP){
                val email = login_email.text.toString()
                val pw = login_pw.text.toString()

                if (validateForm()) {
                    logIn(email, pw)
                }
            }
            true
        }

        //pw 입력 칸에서 enter
        login_pw.setOnKeyListener { v, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP){
                val email = login_email.text.toString()
                val pw = login_pw.text.toString()

                if (validateForm()) {
                    logIn(email, pw)
                }
            }
            true
        }


        login_register.setOnClickListener(object : View.OnClickListener{
            override fun onClick(v: View?) {
                startActivity(Intent(applicationContext, RegisterActivity::class.java))
            }
        })

        login_find.setOnClickListener(object : View.OnClickListener{
            override fun onClick(v: View?) {
                Toast.makeText(applicationContext, "로그인 안 한 채로 들어옴", Toast.LENGTH_SHORT).show()
                startActivity(Intent(applicationContext, HomeActivity::class.java))
                finish()
            }
        })
    }

    private fun logIn(email: String, password: String) {
        // [START sign_in_with_email]
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {

                    Toast.makeText(applicationContext, R.string.login_succeed, Toast.LENGTH_SHORT).show()
                    startActivity(Intent(applicationContext, HomeActivity::class.java))
                    finish()
                }
            }
            .addOnFailureListener(this) {
                Toast.makeText(applicationContext, R.string.login_failed, Toast.LENGTH_SHORT).show()
            }
    }

    private fun validateForm(): Boolean {
        var valid = true

        val email = login_email.text.toString()
        if (TextUtils.isEmpty(email)) {
            login_email.error = "이메일을 입력해주세요!"
            valid = false
        } else {
            login_email.error = null
        }

        val password = login_pw.text.toString()
        if (TextUtils.isEmpty(password)) {
            login_pw.error = "비밀번호를 입력해주세요!"
            valid = false
        }
        else {
            login_pw.error = null
        }
        return valid
    }
}