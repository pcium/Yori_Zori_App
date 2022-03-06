package com.e.yorizori

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.e.yorizori.Activity.HomeActivity
import com.e.yorizori.Activity.OpeningActivity
import com.e.yorizori.Class.RefrigItem
import com.e.yorizori.Fragment.CheckList
import com.google.gson.Gson
import kotlinx.android.synthetic.main.checklist_date_picker.view.*

class CheckListPicker(input : String) : DialogFragment() {
    private val name = input

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.checklist_date_picker, container, false)
        view.title_picker.text = "$name 유통기한 설정"

        // to use shared preference.
        val pref = requireActivity().getSharedPreferences("having", 0)
        val editor = pref.edit()

        view.btn_ok_picker.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                val year = view.spinner_picker.year.toString()
                val month = view.spinner_picker.month.toString()
                val day = view.spinner_picker.dayOfMonth.toString()

                // add to the array
                OpeningActivity.add_item(name, "$year-$month-$day")

                // for json
                val json = Gson()
                val put_me = json.toJson(RefrigItem(name,"$year-$month-$day"))

                // put into the shared preference.
                editor.putString(name, put_me).commit()

                (requireActivity() as HomeActivity).changeFragment(CheckList())
                dismiss()
            }
        })

        view.btn_no_picker.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                // add to the array
                OpeningActivity.add_item(name)

                // for json
                val json = Gson()
                val put_me = json.toJson(RefrigItem(name))

                // put into the shared preference.
                editor.putString(name, put_me).commit()

                (requireActivity() as HomeActivity).changeFragment(CheckList())
                dismiss()
            }
        })
        return view
    }

}