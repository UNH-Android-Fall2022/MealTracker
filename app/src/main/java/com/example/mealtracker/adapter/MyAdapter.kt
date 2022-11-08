package com.example.mealtracker.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mealtracker.R
import com.example.mealtracker.userProfie.Time

class MyAdapter : RecyclerView.Adapter<MyAdapter.MyViewholder>() {

    private val timeList = ArrayList<Time>()

    class MyViewholder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val mealname: TextView = itemView.findViewById(R.id.mealName)
        val mealtime: TextView = itemView.findViewById(R.id.time)
        val mealQuantity: TextView = itemView.findViewById(R.id.mealQuantity)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewholder {
        val timeView = LayoutInflater.from(parent.context).inflate(
            R.layout.time_item,
            parent,
            false
        )
        return MyViewholder(timeView)
    }

    override fun onBindViewHolder(holder: MyViewholder, position: Int) {
        val currentItem = timeList[position]
        holder.mealname.text = currentItem.mealName
        holder.mealtime.text = currentItem.time
        holder.mealQuantity.text = currentItem.quantity


    }

    fun updateTimeList(timeList: List<Time>) {
        this.timeList.clear()
        this.timeList.addAll(timeList)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return timeList.size
    }
}