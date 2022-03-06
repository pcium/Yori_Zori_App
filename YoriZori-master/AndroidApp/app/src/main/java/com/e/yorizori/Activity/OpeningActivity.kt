package com.e.yorizori.Activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.WindowManager
import android.view.animation.Animation
import android.view.animation.Animation.AnimationListener
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.e.yorizori.Class.Recipe
import com.e.yorizori.Class.RefrigItem
import com.e.yorizori.Class.Server_recipe
import com.e.yorizori.R
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.*
import com.google.gson.Gson

class OpeningActivity : AppCompatActivity(){

    companion object {
        var recipe_list = arrayListOf<Server_recipe>()
        var server_ing = arrayListOf<String>()
        var my_ing = mutableListOf<RefrigItem>()

        fun add(str : String) {
            server_ing.add(str)
        }
        fun add_item(name: String, date: String) {
            my_ing.add(RefrigItem(name, date))
        }
        fun add_item(name: String) {
            my_ing.add(RefrigItem(name))
        }
        fun add_item(ref : RefrigItem){
            my_ing.add(ref)

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_opening)
        val metrics = DisplayMetrics()
        val windowManager: WindowManager =
            applicationContext.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val mTurkey: ImageView = findViewById(R.id.Turkey)
        val mBar : ImageView = findViewById(R.id.Bar)
        val animSlide = AnimationUtils.loadAnimation(applicationContext,
            R.anim.slide
        )
        val animScale = AnimationUtils.loadAnimation(applicationContext,
            R.anim.scale
        )
        var params = mTurkey.layoutParams

        /* get ingredients from the server. START */
        val rootRef = FirebaseDatabase.getInstance().reference
        val child = rootRef.child("재료")
        val listener = object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                Toast.makeText(applicationContext, R.string.connection_err, Toast.LENGTH_SHORT).show()
            }

            override fun onDataChange(shot: DataSnapshot) {
                var tmp_list: ArrayList<String> = arrayListOf()
                for (ing in shot.children) {
                    val child = ing.value.toString()
                    tmp_list.add(child)
                }
                server_ing = tmp_list
            }
        }
        child.addListenerForSingleValueEvent(listener)
        /* get ingredients from the server. DONE */


        val recipes = rootRef.child("recipe")
        val rlistener = object: ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
                Toast.makeText(applicationContext,R.string.connection_err,Toast.LENGTH_SHORT)
            }

            override fun onDataChange(p0: DataSnapshot) {
                var tmp_recipe : ArrayList<Server_recipe> = arrayListOf()
                for(recipe_title_cat in p0.children){
                    for(recipe in recipe_title_cat.children) {
                        val key = recipe.key
                        val recipe_str = recipe.getValue(String::class.java)
                        val trimmed = recipe_str!!.trim()
                        val gson = Gson()
                        val recipe = gson.fromJson(trimmed, Recipe::class.java)
                        tmp_recipe.add(Server_recipe(key!!,recipe))
                    }
                }
                recipe_list = tmp_recipe
            }

        }
        recipes.addValueEventListener(rlistener)
        /* get ingredients from the server. DONE */
        /* get ingredients from the server. DONE */


        /* get my own ingredients. START */
        val pref = getSharedPreferences("having", 0)
        val get_json = pref.all

        // change the format and add to the list
        val json = Gson()
        for (entry in get_json.entries){
            val ref_item = json.fromJson(entry.value.toString(),RefrigItem::class.java)
            add_item(ref_item)
        }

        /* get my own ingeredients. DONE */

        windowManager.defaultDisplay.getMetrics(metrics)
        val length = (maxOf(metrics.widthPixels, metrics.heightPixels) * 0.1).toInt()
        params.width = length
        params.height = length
        mTurkey.layoutParams = params

        animSlide.setAnimationListener(object : AnimationListener {
            override fun onAnimationStart(arg0: Animation) {
                mBar.startAnimation(animScale)
            }
            override fun onAnimationRepeat(arg0: Animation) {}
            override fun onAnimationEnd(arg0: Animation) {
                startActivity(Intent(this@OpeningActivity, LoginActivity::class.java))
                finish()
            }
        })
        mTurkey.startAnimation(animSlide)
    }
}
