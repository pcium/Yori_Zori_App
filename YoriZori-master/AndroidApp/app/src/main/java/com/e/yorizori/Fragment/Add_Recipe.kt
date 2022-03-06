package com.e.yorizori.Fragment

import android.app.Activity
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.e.yorizori.Activity.HomeActivity
import com.e.yorizori.Activity.OpeningActivity.Companion.server_ing
import com.e.yorizori.Adapter.WRIngListViewAdapter
import com.e.yorizori.Adapter.WRRecipeListViewAdapter
import com.e.yorizori.Class.Recipe
import com.e.yorizori.Interface.BackBtnPressListener
import com.e.yorizori.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageMetadata
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_writing_recipe.*
import kotlinx.android.synthetic.main.activity_writing_recipe.view.*
import kotlinx.android.synthetic.main.footer.view.*
import kotlinx.android.synthetic.main.ingredientlist_item.view.*
import kotlinx.android.synthetic.main.recipelist_item.view.*
import java.util.*


class Add_Recipe(fragment: Fragment, option :Int = 0) :BackBtnPressListener, Fragment() {
    private lateinit var database: DatabaseReference
    lateinit var firebaseAuth: FirebaseAuth
    private var fragment = fragment
    private var fromwhere: Int = option
    lateinit var recipeListView : ListView
    lateinit var ingListView : ListView

    val Error = arrayOf("제목을 입력해주세요!", "레시피를 입력해 주세요!", "사진을 넣어주세요!", "재료를 추가해 주세요!")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.activity_writing_recipe, container, false)
        val storage = FirebaseStorage.getInstance()
        var storageRef = storage.reference
        firebaseAuth = FirebaseAuth.getInstance()
        val user = firebaseAuth.currentUser
        val userUID = user!!.uid

        activity!!.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)

        if (fromwhere == 0) {
            (fragment as Community).saveInfo(1, this)
        } else if (fromwhere == 1) {
            (fragment as Community_SortedList).saveInfo(0, this)
        }
        (activity as HomeActivity).setOnBackBtnListener(this)


        ingListView = view.findViewById<ListView>(R.id.ingredientList)

        var ingListViewAdapter =
            WRIngListViewAdapter(
                this.requireContext(),
                this
            )
        val ingFooter = inflater.inflate(R.layout.footer, container, false)
        ingFooter.list_footer_adder.text = "+ 재료 추가하기"
        ingFooter.list_footer_adder.setOnClickListener {
            ingListViewAdapter.add_item()
            ingListViewAdapter.notifyDataSetChanged()
            setListViewHeightBasedOnItems(ingListView)
        }

        ingListView.setAdapter(ingListViewAdapter)
        setListViewHeightBasedOnItems(ingListView)
        ingListView.addFooterView(ingFooter)


        recipeListView = view.findViewById<ListView>(R.id.recipeList)

        var recipeListViewAdapter =
            WRRecipeListViewAdapter(
                this.requireContext(),
                this
            )
        val recipeFooter = inflater.inflate(R.layout.footer, container, false)
        recipeFooter.list_footer_adder.text = "+ 과정 추가하기"
        recipeFooter.list_footer_adder.setOnClickListener {
            recipeListViewAdapter.addItem()
            recipeListViewAdapter.notifyDataSetChanged()
            setListViewHeightBasedOnItems(recipeListView)
        }
        recipeListView.setAdapter(recipeListViewAdapter)
        setListViewHeightBasedOnItems(recipeListView)
        recipeListView.addFooterView(recipeFooter)

        //back 버튼을 누르면 community뷰로 돌아감
        view.backBtn.setOnClickListener {
            if (fromwhere == 0)
                (fragment as Community).saveInfo(1, null)
            else
                (fragment as Community_SortedList).saveInfo(0, null)
            val fragmentManager: FragmentManager = activity!!.supportFragmentManager
            fragmentManager.beginTransaction().remove(this).commit()
            fragmentManager.popBackStack()
        }

        //done 버튼을 누르면 데이터를 저장하고 community뷰로 돌아감
        view.doneBtn.setOnClickListener {
            var ings: ArrayList<Pair<String, String>> = arrayListOf()
            for (i in 0 until ingListView.count - 1) {
                val view = ingListView.getChildAt(i)
                val ing_name = view.ingText.text.toString()
                val ing_amount = view.ingAmountText.text.toString()
                ings.add(Pair(ing_name, ing_amount))
            }
            ings = ings.filter {
                it.first != "" && it.second != ""
            } as ArrayList<Pair<String, String>>
            var recipes: ArrayList<String> = arrayListOf()
            for (i in 0 until recipeListView.count - 1) {
                val view = recipeListView.getChildAt(i)
                val methodNum = view.recipeNumText.text.toString()
                val method = view.recipeText.text.toString()
                recipes.add("${methodNum}. ${method}")
            }
            recipes = recipes.filter {
                it != ""
            } as ArrayList<String>
            val recipe = Recipe()
            recipe.cook_title = view.titleInput.text.toString()
            recipe.tag = arrayListOf(view.tagInput.text.toString())
            recipe.ings = ings
            recipe.recipe = recipes
            recipe.writer_UID = userUID
            if (HomeActivity.picsuc == 1) {
                var uri = Uri.parse(HomeActivity.str)
                var metadata = StorageMetadata.Builder().setContentType("image/jpg").build()
                val picRef = storageRef.child("음식사진/${view.titleInput.text.toString()}.jpg")
                val uploadTask = picRef.putFile(uri, metadata)
                val urlTask = uploadTask.continueWithTask {
                    if (!it.isSuccessful) {
                        it.exception?.let {
                            throw it
                        }
                    }
                    picRef.downloadUrl
                }.addOnCompleteListener {
                    recipe.pics.add(it.result.toString())

                    val ret = recipe.isValid()
                    if (ret != 0) {
                        Toast.makeText(activity, Error[ret - 1], Toast.LENGTH_SHORT).show()
                    } else {

                        database = FirebaseDatabase.getInstance().reference
                        database.child("recipe/${recipe.cook_title}").push()
                            .setValue(Gson().toJson(recipe))
                            .addOnSuccessListener {
                                val toast =
                                    Toast.makeText(activity, "저장되었습니다 :)", Toast.LENGTH_SHORT)
                                toast.show()
                                onBack()
                            }
                            .addOnFailureListener {
                                val toast =
                                    Toast.makeText(activity, "저장에 실패 :(", Toast.LENGTH_SHORT)
                                toast.show()
                                onBack()
                            }
                    }
                }
            } else {
                Toast.makeText(this.context, "사진을 추가해주세요!", Toast.LENGTH_SHORT).show()
            }
        }

        view.backBtn.setOnClickListener {
            onBack()
        }

        // Image Click
        view.recipeImage.setOnClickListener {
            (activity as HomeActivity).perCheck()
        }

        /* listview size */

        //setListViewHeightBasedOnItems(ingListView)
        //setListViewHeightBasedOnItems(recipeListView)

        return view
    }

    override fun onBack() {
        if (fromwhere == 0)
            (fragment as Community).saveInfo(1, null)
        else
            (fragment as Community_SortedList).saveInfo(0, null)
        var ft = activity!!.supportFragmentManager
        ft.beginTransaction().remove(this).commit()
        ft.popBackStack()
    }

    override fun onResume() {
        super.onResume()
        if (HomeActivity.picsuc == 1) {
            var uri = Uri.parse(HomeActivity.str)
            recipeImage.setImageURI(uri)
        }
    }

    fun setListViewHeightBasedOnItems(listView: ListView): Boolean {
        val listAdapter = listView.getAdapter()
        if (listAdapter != null) {
            val numberOfItems = listAdapter.getCount()
            // Get total height of all items.
            var totalItemsHeight = 0
            for (itemPos in 0 until numberOfItems) {
                val item = listAdapter.getView(itemPos, null, listView)
                val px = 500 * (listView.getResources().getDisplayMetrics().density)
                item.measure(
                    View.MeasureSpec.makeMeasureSpec(
                        px.toInt(),
                        View.MeasureSpec.AT_MOST
                    ), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
                )
                totalItemsHeight += item.getMeasuredHeight()
                if(itemPos == 0)
                    totalItemsHeight += item.measuredHeight
            }

            // Get total height of all item dividers.
            val totalDividersHeight = (listView.getDividerHeight() * (numberOfItems - 1))
            // Get padding
            val totalPadding = listView.getPaddingTop() + listView.getPaddingBottom()
            // Set list height.
            val params = listView.getLayoutParams()
            params.height = totalItemsHeight + totalDividersHeight + totalPadding
            listView.setLayoutParams(params)
            listView.requestLayout()
            return true
        } else {
            return false
        }
    }
    fun resizeListView(position : Int) {
        if (position == 0) {
            setListViewHeightBasedOnItems(ingListView)
        } else {
            setListViewHeightBasedOnItems(recipeListView)
        }
    }
}



