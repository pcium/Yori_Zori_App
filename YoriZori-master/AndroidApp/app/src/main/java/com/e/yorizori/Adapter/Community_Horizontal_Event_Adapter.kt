package com.e.yorizori.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.e.yorizori.Activity.HomeActivity
import com.e.yorizori.Class.Community_Event_Item
import com.e.yorizori.Fragment.Event_View
import com.e.yorizori.R

class Community_Horizontal_Event_Adapter(
    context: Context,
    activity: HomeActivity,
    fragment: Fragment
) :
    RecyclerView.Adapter<Community_Horizontal_Event_Adapter.EventViewHolder>() {
    private var EventItemList=ArrayList<Community_Event_Item>()
    private val context: Context
    private val activity = activity
    private val fragment = fragment

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): EventViewHolder { // context 와 parent.getContext() 는 같다.
        val view: View = LayoutInflater.from(context)
            .inflate(R.layout.community_event_item, parent, false)
        return EventViewHolder(view)
    }

    override fun getItemCount(): Int {
        return EventItemList.size
    }

    inner class EventViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var rpicview: ImageView
        init {
            rpicview=itemView.findViewById(R.id.Event_img)
            rpicview.setOnClickListener {
                (activity as HomeActivity).changeFragment(Event_View(fragment))
            }

        }
    }

    init {
        this.context = context
        this.EventItemList = EventItemList
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        val item = EventItemList[position]
        if(item.EventDrawable == null) {
            val drawable_tmp = ContextCompat.getDrawable(context, R.drawable.event_image)
            holder.rpicview.setImageDrawable(drawable_tmp)
        }
        else
            holder.rpicview.setImageDrawable(item.EventDrawable)
        holder.itemView.setOnClickListener{
            /* TODO: Event 사진 크게 보기? 자세히 보기? 하는 view*/
        }
    }
    fun fill(){
        for(i in 0..3){
            var item = Community_Event_Item()
            item.EventDrawable = ContextCompat.getDrawable(context, R.drawable.event_image)
            EventItemList.add(item)
        }
    }
}