package com.example.mealtracker.adapter

import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mealtracker.R
import com.example.mealtracker.userProfie.Time

class MyAdapter : RecyclerView.Adapter<MyAdapter.MyViewholder>() {

    private lateinit var mlistner: onItemClickListner

    interface onItemClickListner {
        fun onItemClick(position: Int)
    }


    fun setOnItemCLickListner(listner: onItemClickListner) {
        mlistner = listner
    }

    private val timeList = ArrayList<Time>()

    class MyViewholder(itemView: View, listner: onItemClickListner) :
        RecyclerView.ViewHolder(itemView) {

        init {
            itemView.setOnClickListener {
                listner.onItemClick(adapterPosition)
            }
        }

        val mealname: TextView = itemView.findViewById(R.id.mealName)
        val mealtime: TextView = itemView.findViewById(R.id.time)
        val mealQuantity: TextView = itemView.findViewById(R.id.mealQuantity)
        val imageViewL: ImageView = itemView.findViewById(R.id.card_imageView)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewholder {
        val timeView = LayoutInflater.from(parent.context).inflate(
            R.layout.time_item,
            parent,
            false
        )
        return MyViewholder(timeView, mlistner)
    }

    override fun onBindViewHolder(holder: MyViewholder, position: Int) {
        val currentItem = timeList[position]
        holder.mealname.text = currentItem.mealName
        holder.mealtime.text = currentItem.time
        holder.mealQuantity.text = currentItem.quantity
        Glide.with(holder.itemView.context)
            .load(currentItem.image).fitCenter()
            .into(holder.imageViewL)

        holder.itemView.setOnClickListener {
            val dialog = AlertDialog.Builder(holder.itemView.context)
            val dialogueview = LayoutInflater.from(holder.itemView.context)
                .inflate(R.layout.custome_dialogue, null)

            val imageI: ImageView = dialogueview.findViewById(R.id.imageViewDialogue)
            val calories: TextView = dialogueview.findViewById(R.id.caloriesText)
            val cholestrol: TextView = dialogueview.findViewById(R.id.cholestrolText)
            val fiber: TextView = dialogueview.findViewById(R.id.fiberText)
            val fat: TextView = dialogueview.findViewById(R.id.fatText)
            val protiencount: TextView = dialogueview.findViewById(R.id.protienText)


            Glide.with(holder.itemView.context)
                .load(currentItem.image).fitCenter()
                .into(imageI)

            cholestrol.text = "Cholestrol  " + currentItem.foodNutrients?.chocdf.toString()
            fiber.text = "Fiber  " + currentItem.foodNutrients?.fibtg.toString()
            fat.text = "Fat  " + currentItem.foodNutrients?.fat.toString()
            calories.text = "Calories  " + currentItem.foodNutrients?.enerc_KCAL.toString()
            protiencount.text = "Protien  " + currentItem.foodNutrients?.procnt.toString()

            dialog.setView(dialogueview)
            dialog.setCancelable(true)
            dialog.show()
        }
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