package com.e.yorizori.Activity

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.FrameLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.e.yorizori.Class.RefrigItem
import com.e.yorizori.Class.ScrapInfo
import com.e.yorizori.Fragment.CheckList
import com.e.yorizori.Fragment.Community
import com.e.yorizori.Fragment.MyPage
import com.e.yorizori.Interface.BackBtnPressListener
import com.e.yorizori.R
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_writing_recipe.*


class HomeActivity : AppCompatActivity(){
    lateinit var ft : FragmentTransaction
    var backBtnListener : BackBtnPressListener? = null
    var fragments : Array<Fragment?> = arrayOf(Community(), CheckList(), MyPage())
    var position = 0

    companion object {
        var hate_edit : SharedPreferences.Editor? = null
        var hate_prefs : SharedPreferences? = null
        var hate_items = arrayListOf<String>()

        var picsuc = 0
        var str : String = ""
        var recommend_info : ArrayList<ArrayList<ScrapInfo>> = arrayListOf(arrayListOf(),arrayListOf(),arrayListOf(),arrayListOf())


        fun set_prefs(_context : Context) {
            hate_prefs = _context.getSharedPreferences("hate_data", Context.MODE_PRIVATE)
            hate_edit = _context.getSharedPreferences("hate_data", Context.MODE_PRIVATE).edit()
        }

        fun add_hate(name: String) {
            hate_items.add(name)
            hate_edit?.putString(name,name)
            hate_edit?.commit()
        }


        fun load_hate() {
            var tmp = hate_prefs?.all
            for(name in tmp!!.keys){
                hate_items.add(name)
            }
        }

        fun delete_hate(item : String) {
            hate_edit?.remove(item)
            hate_edit?.commit()
        }

        // Image Pick Code
        val IMAGE_PICK_CODE = 1000
        // Permission Code
        val PERMISSION_CODE = 1001

    }
    // Image Pick Code
    val IMAGE_PICK_CODE = 1000
    // Permission Code
    val PERMISSION_CODE = 1001

    fun setOnBackBtnListener(listener:BackBtnPressListener?){
        backBtnListener = listener
    }

    fun changeFragment(f: Fragment,name: String?=null, cleanStack:Boolean=false){
        ft = supportFragmentManager.beginTransaction()
        ft.replace(R.id.tab_frame,f)
        ft.addToBackStack(name)
        ft.commit()
        true
    }

    fun perCheck() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if(checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                //Permission Denied
                val permissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
                requestPermissions(permissions, PERMISSION_CODE)
            }
            else {
                pickImageFromGallery()
            }
        }
        else {
            pickImageFromGallery()
        }
    }

    fun pickImageFromGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, IMAGE_PICK_CODE)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode) {
            PERMISSION_CODE -> {
                if(grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    pickImageFromGallery()
                }
                else {
                    Toast.makeText(this, "Permission Denied!", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == IMAGE_PICK_CODE) {
            val uri = data!!.data
            str = uri.toString()
            picsuc = 1
            recipeImage.setImageURI(data?.data)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        var user = FirebaseAuth.getInstance().currentUser
        var rootRef = FirebaseDatabase.getInstance().reference
        var userUID = user!!.uid
        var scraped =  rootRef.child("users/$userUID/scrap")
        var price = rootRef.child("users/$userUID/cheap")
        var simple = rootRef.child("users/$userUID/simple")
        var delicious = rootRef.child("users/$userUID/delicious")

        var listener1 = object: ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(p0: DataSnapshot) {
                var tmp_list : ArrayList<ScrapInfo> = arrayListOf()
                for (child in p0.children){
                    var key = child.key
                    var list = child.getValue(String::class.java)
                    var tmp = list!!.split(",")
                    tmp_list.add(ScrapInfo(key!!,tmp[0],tmp[1]))
                }
                recommend_info[0] = tmp_list
            }
        }
        var listener2 = object: ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(p0: DataSnapshot) {
                var tmp_list : ArrayList<ScrapInfo> = arrayListOf()
                for (child in p0.children){
                    var key = child.key
                    var list = child.getValue(String::class.java)
                    var tmp = list!!.split(",")
                    tmp_list.add(ScrapInfo(key!!,tmp[0],tmp[1]))
                }
                recommend_info[1] = tmp_list
            }
        }
        var listener3 = object: ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(p0: DataSnapshot) {
                var tmp_list : ArrayList<ScrapInfo> = arrayListOf()
                for (child in p0.children){
                    var key = child.key
                    var list = child.getValue(String::class.java)
                    var tmp = list!!.split(",")
                    tmp_list.add(ScrapInfo(key!!,tmp[0],tmp[1]))
                }
                recommend_info[2] = tmp_list
            }
        }
        var listener4 = object: ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(p0: DataSnapshot) {
                var tmp_list : ArrayList<ScrapInfo> = arrayListOf()
                for (child in p0.children){
                    var key = child.key
                    var list = child.getValue(String::class.java)
                    var tmp = list!!.split(",")
                    tmp_list.add(ScrapInfo(key!!,tmp[0],tmp[1]))
                }
                recommend_info[3] = tmp_list
            }
        }
        set_prefs(applicationContext)
        load_hate()
        scraped.addValueEventListener(listener1)
        simple.addValueEventListener(listener3)
        delicious.addValueEventListener(listener2)
        price.addValueEventListener(listener4)
    }

    override fun onResume(){
        super.onResume()
        setContentView(R.layout.activity_home)
        val bottomNavigation =findViewById<BottomNavigationView>(R.id.tab)
        val upperView = findViewById<FrameLayout>(R.id.tab_frame)
        if(fragments[position] != null) {
                changeFragment(fragments[position]!!)
        }

        bottomNavigation.setOnNavigationItemSelectedListener {
            upperView.removeAllViews()

            var selected:Fragment
            when(it.itemId){
                R.id.tab_community -> {
                    if(position == 0){
                        selected = Community()
                    }
                    else selected = fragments[0]!!
                }
                R.id.tab_check ->{
                    if(position == 1){
                        selected = CheckList()
                    }
                    else selected = fragments[1]!!
                }
                else ->{
                    if(position == 2) {
                        selected = MyPage()
                    }
                    else selected = fragments[2]!!
                }
            }
            changeFragment(selected)
            true
        }

    }

    override fun onBackPressed() {
        if(backBtnListener != null){
            backBtnListener!!.onBack()
        }
        else {
            Toast.makeText(this,"no back Button Listener!",Toast.LENGTH_SHORT).show()
            super.onBackPressed()
        }
    }

    fun saveFragment(p : Int,f: Fragment){
        fragments[p] = f
        position = p
    }

}
